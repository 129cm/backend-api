package com.d129cm.backendapi.common.domain;

import com.d129cm.backendapi.common.domain.code.CodeName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCode {
    @EmbeddedId
    private CommonCodeId id;

    @MapsId("groupId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", columnDefinition = "char(3)")
    private CommonCodeGroup groupCode;

    @Column(columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private CodeName codeName;


    public CommonCode(CodeName codeName) {
        this.id = new CommonCodeId(codeName);
        this.groupCode = new CommonCodeGroup(codeName.getGroupName());
        this.codeName = codeName;
    }
}
