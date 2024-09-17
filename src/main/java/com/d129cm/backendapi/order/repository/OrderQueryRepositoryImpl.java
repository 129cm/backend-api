package com.d129cm.backendapi.order.repository;

import com.d129cm.backendapi.common.domain.QAddress;
import com.d129cm.backendapi.item.domain.QItem;
import com.d129cm.backendapi.item.domain.QItemOption;
import com.d129cm.backendapi.member.domain.QMember;
import com.d129cm.backendapi.order.domain.QOrder;
import com.d129cm.backendapi.order.domain.QOrderItemOption;
import com.d129cm.backendapi.order.dto.*;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;

public class OrderQueryRepositoryImpl implements OrderQueryRepository {
    private final JPAQueryFactory queryFactory;

    public OrderQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public OrdersSearchResultDto searchOrders(String itemName, LocalDateTime startDate, LocalDateTime endDate, String orderState, int size, int page) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        QOrderItemOption orderItemOption = QOrderItemOption.orderItemOption;
        QItem item = QItem.item;
        QItemOption itemOption = QItemOption.itemOption;

        // 1단계: 페이징 처리를 위한 order.id 목록을 가져옴
        List<Long> orderIds = queryFactory
                .select(order.id)
                .from(order)
                .where(
                        itemNameContains(itemName),
                        orderDateBetween(startDate, endDate),
                        orderStateEq(orderState)
                )
                .limit(size)
                .offset(page * size)
                .fetch();

        if (orderIds.isEmpty()) {
            return new OrdersSearchResultDto(List.of(), 0L);  // 조회된 데이터가 없을 경우 빈 결과 반환
        }

        // 2단계: 가져온 order.id를 기준으로 다시 조회
        List<OrdersSearchResponseDto> orders = queryFactory
                .from(order)
                .join(order.member, member)
                .leftJoin(orderItemOption).on(orderItemOption.order.eq(order))
                .leftJoin(orderItemOption.itemOption, itemOption)
                .leftJoin(itemOption.item, item)
                .where(order.id.in(orderIds))
                .orderBy(order.id.desc())
                .transform(groupBy(order.id).list(
                        Projections.constructor(OrdersSearchResponseDto.class,
                                member.id,
                                member.name,
                                order.createdAt,
                                order.id,
                                GroupBy.list(
                                        Projections.constructor(OrderItemDto.class,
                                                orderItemOption.count,
                                                item.image,
                                                item.name,
                                                itemOption.id,
                                                itemOption.name
                                        )
                                ),
                                order.commonCodeId.codeId
                        )
                ));

        // 총 레코드 수를 조회
        long total = Optional.ofNullable(queryFactory
                .select(order.count())
                .from(order)
                .fetchOne()).orElse(0L);

        return new OrdersSearchResultDto(orders, total);
    }

    @Override
    public OrderDetailsDto findOrderDetailsByOrderId(Long orderId) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        QOrderItemOption orderItemOption = QOrderItemOption.orderItemOption;
        QItem item = QItem.item;
        QItemOption itemOption = QItemOption.itemOption;
        QAddress address = QAddress.address;


        return queryFactory
                .from(order)
                .join(order.member, member)
                .leftJoin(member.address, address)
                .leftJoin(orderItemOption).on(orderItemOption.order.eq(order))
                .leftJoin(orderItemOption.itemOption, itemOption)
                .leftJoin(itemOption.item, item)
                .where(order.id.eq(orderId))
                .transform(
                        groupBy(order.id).as(
                                Projections.constructor(OrderDetailsDto.class,
                                        order.id,
                                        order.commonCodeId.codeId,
                                        order.createdAt,
                                        GroupBy.list(Projections.constructor(OrderItemDetailsDto.class,
                                                item.id,
                                                item.name,
                                                item.image,
                                                itemOption.id,
                                                itemOption.name,
                                                orderItemOption.count,
                                                orderItemOption.salesPrice)),
                                        member.id,
                                        member.name,
                                        Projections.constructor(OrderAddressDto.class,
                                                address.zipCode,
                                                address.roadNameAddress,
                                                address.addressDetails))
                        )
                )
                .get(orderId);
    }


    private BooleanExpression itemNameContains(String itemName) {
        return itemName != null ? QItem.item.name.containsIgnoreCase(itemName) : null;
    }

    private BooleanExpression orderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        BooleanExpression predicate = null;
        if (startDate != null) {
            predicate = QOrder.order.createdAt.goe(startDate);
        }
        if (endDate != null) {
            predicate = predicate != null ? predicate.and(QOrder.order.createdAt.loe(endDate)) : QOrder.order.createdAt.loe(endDate);
        }
        return predicate;
    }

    private BooleanExpression orderStateEq(String orderState) {
        return orderState != null ? QOrder.order.commonCodeId.codeId.eq(orderState) : null;
    }
}
