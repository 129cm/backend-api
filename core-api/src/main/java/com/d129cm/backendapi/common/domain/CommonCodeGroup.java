package com.d129cm.backendapi.common.domain;

import com.d129cm.backendapi.common.domain.code.GroupName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCodeGroup {
    @Id
    @Column(columnDefinition = "char(3)")
    private String groupId;

    @Column(columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private GroupName groupName;

    public CommonCodeGroup(GroupName groupName) {
        this.groupId = groupName.getGroupId();
        this.groupName = groupName;
    }
}
