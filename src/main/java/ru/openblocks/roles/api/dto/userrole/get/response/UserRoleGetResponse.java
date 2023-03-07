package ru.openblocks.roles.api.dto.userrole.get.response;

import lombok.*;

import java.time.Instant;

@Data
@Builder
public class UserRoleGetResponse {

    private String code;

    private String label;

    private String grantBy;

    private Instant createdAt;
}
