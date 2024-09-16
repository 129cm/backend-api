package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.common.annotation.JpaSliceTest;
import com.d129cm.backendapi.order.dto.OrdersSearchResultDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
@Sql(value = {"/clean-up.sql", "/test-common-code.sql", "/test-order.sql"})
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    @Nested
    class searchOrders {

        @Test
        void OrdersSearchResponseDto반환_파라메터_없이_검색() {
            // given
            int size = 10;
            int page = 0;

            // when
            OrdersSearchResultDto responseDtos = repository.searchOrders(null, null, null, null, size, page);

            // then
            assertThat(responseDtos.getOrders()).isNotNull();
            assertThat(responseDtos.getOrders().size()).isEqualTo(size);
        }
    }

}
