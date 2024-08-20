package com.d129cm.backendapi.common.domain;

import com.d129cm.backendapi.common.domain.code.CodeName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonCodeIdTest {

    @Nested
    class equalsAndHashCode {

        @Test
        public void true_동일성_비교() {
            // given
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.결제완료);

            // when & then
            assertThat(commonCodeId.equals(commonCodeId)).isTrue();
            assertThat(commonCodeId.hashCode()).isEqualTo(commonCodeId.hashCode());
        }

        @Test
        public void true_동등성_비교() {
            // given
            CommonCodeId commonCodeId1 = new CommonCodeId(CodeName.결제완료);
            CommonCodeId commonCodeId2 = new CommonCodeId(CodeName.결제완료);

            // when & then
            assertThat(commonCodeId1.equals(commonCodeId2)).isTrue();
            assertThat(commonCodeId1.hashCode()).isEqualTo(commonCodeId2.hashCode());
        }

        @Test
        public void false_필드_값이_다른_객체_비교() {
            // given
            CommonCodeId commonCodeId1 = new CommonCodeId(CodeName.결제완료);
            CommonCodeId commonCodeId2 = new CommonCodeId(CodeName.배송완료);

            // when & then
            assertThat(commonCodeId1.equals(commonCodeId2)).isFalse();
            assertThat(commonCodeId1.hashCode()).isNotEqualTo(commonCodeId2.hashCode());
        }

        @Test
        public void false_Null과_비교() {
            // given
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.결제완료);

            // when & then
            assertThat(commonCodeId.equals(null)).isFalse();
        }

        @Test
        public void false_타입이_다른_객체와_비교() {
            // given
            CommonCodeId commonCodeId = new CommonCodeId(CodeName.결제완료);
            String differentType = "String";

            // when & then
            assertThat(commonCodeId.equals(differentType)).isFalse();
        }
    }
}
