insert into common_code_group values ('010', '주문'),
                                     ('020', '배송');

insert into common_code values ('010', '010', '결제완료'),
                               ('020', '010', '주문완료'),
                               ('030', '010', '주문취소'),
                               ('010', '020', '배송중'),
                               ('020', '020', '배송완료');