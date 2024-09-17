package com.d129cm.backendapi.order.manager;

import com.d129cm.backendapi.common.domain.CommonCodeId;
import com.d129cm.backendapi.common.domain.code.CodeName;
import com.d129cm.backendapi.common.exception.ConflictException;
import com.d129cm.backendapi.common.exception.NotFoundException;
import com.d129cm.backendapi.fixture.MemberFixture;
import com.d129cm.backendapi.fixture.OrderFixture;
import com.d129cm.backendapi.member.domain.Member;
import com.d129cm.backendapi.order.domain.Order;
import com.d129cm.backendapi.order.dto.OrderDetailsDto;
import com.d129cm.backendapi.order.repository.OrderRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderManagerTest {

    @InjectMocks
    private OrderManager orderManager;

    @Mock
    private OrderRepository orderRepository;

    private static final int BACKNUMBER_LENGTH = 7;  // 36진법으로 7자리

    @Nested
    class searchOrder {
        @Test
        void 검색결과반환_검색어_없음_10개_조회() {
            // given
            int size = 10;
            int page = 0;
            LocalDate today = LocalDate.now();
            LocalTime nowTime = LocalTime.now();

            // when
            orderManager.searchResult(null, null, null, null, size, page);

            // then
            LocalDateTime endTime = LocalDateTime.of(today, LocalTime.of(nowTime.getHour(), nowTime.getMinute()));
            verify(orderRepository, times(1)).searchOrders(null, LocalDateTime.of(1900, 1, 1, 0, 0), endTime, null, size, page);
        }
    }

    @Nested
    class getOrderDetailsByOrderId {

        @Test
        void OrderDetailsDto반환_orderId로_조회() {
            // given
            Long orderId = 1L;
            OrderDetailsDto dto = mock(OrderDetailsDto.class);
            when(dto.getOrderId()).thenReturn(orderId);
            when(orderRepository.findOrderDetailsByOrderId(orderId)).thenReturn(dto);

            // when
            OrderDetailsDto detailsDto = orderManager.getOrderDetailsByOrderId(orderId);

            // then
            verify(orderRepository, times(1)).findOrderDetailsByOrderId(orderId);
            assertThat(detailsDto.getOrderId()).isEqualTo(orderId);
        }
    }

    @Nested
    class getOrderById {

        @Test
        void Order반환_id로_조회() {
            // given
            Long orderId = 1L;
            CommonCodeId oldCommonCodeId = new CommonCodeId(CodeName.주문대기);
            Member mockMember = mock(Member.class);
            Order order = Order.builder()
                    .commonCodeId(oldCommonCodeId)
                    .member(mockMember)
                    .build();
            when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

            // when
            Order result = orderManager.getOrderById(orderId);

            // then
            assertThat(result).isEqualTo(order);
        }

        @Test
        void 에러반환_Order가_없는_경우() {
            Long orderId = 1L;
            String message = "정보를 찾을 수 없습니다.";
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            // when & then
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> orderManager.getOrderById(orderId));
            assertThat(message).isEqualTo(exception.getMessage());
        }
    }

    @Nested
    class createOrder {

        @Test
        void Order반환_order_생성() {
            // given
            Member member = MemberFixture.createMember("abc@example.com");
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.주문대기);
            int FRONTNUMBER_LENGTH = "yyyyMMdd-".length();
            String orderSerial = "20240914-1234567";

            Order order = spy(Order.builder()
                    .member(member)
                    .commonCodeId(commonCodeId)
                    .build());
            order.setOrderSerial(orderSerial);

            when(orderRepository.existsByOrderSerial(any())).thenReturn(false);
            when(orderRepository.save(any(Order.class))).thenReturn(order);

            // when
            Order result = orderManager.createOrder(member);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result.getMember()).isEqualTo(member);
                softly.assertThat(result.getCommonCodeId()).isEqualTo(commonCodeId);
                softly.assertThat(result.getOrderSerial()).isNotNull();
                softly.assertThat(result.getOrderSerial()).hasSize(FRONTNUMBER_LENGTH + BACKNUMBER_LENGTH);
                verify(orderRepository).save(any(Order.class));
            });
        }

        @Test
        void 에러반환_주문번호_생성_실패() {
            // given
            Member member = MemberFixture.createMember("abc@example.com");
            String message = "주문번호 생성 실패: 최대 재시도 횟수를 초과했습니다.";

            when(orderRepository.existsByOrderSerial(any())).thenReturn(true);

            // when & then
            ConflictException exception = assertThrows(ConflictException.class,
                    () -> orderManager.createOrder(member));
            assertThat(message).isEqualTo(exception.getMessage());
        }
    }

    @Nested
    class getOrderByOrderSerial {

        @Test
        void 주문반환_주분_번호로_조회() {
            // given
            String orderSerial = "20240914-1234567";
            Order order = OrderFixture.makeOrderWithOrderSerial(mock(Member.class), orderSerial);

            when(orderRepository.findByOrderSerial(orderSerial)).thenReturn(Optional.of(order));

            // when
            Order result = orderManager.getOrderByOrderSerial(orderSerial);

            // then
            verify(orderRepository).findByOrderSerial(orderSerial);
        }

        @Test
        void 에러반환_주문_번호로_조회_실패() {
            // given
            String orderSerial = "20240914-1234567";
            Order order = OrderFixture.makeOrderWithOrderSerial(mock(Member.class), orderSerial);
            String message = "정보를 찾을 수 없습니다.";

            when(orderRepository.findByOrderSerial(orderSerial)).thenReturn(Optional.empty());

            // when & then
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> orderManager.getOrderByOrderSerial(orderSerial));
            assertThat(message).isEqualTo(exception.getMessage());
        }
    }
}
