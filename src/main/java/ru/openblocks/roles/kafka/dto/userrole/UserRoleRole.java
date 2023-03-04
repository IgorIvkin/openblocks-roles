package ru.openblocks.roles.kafka.dto.userrole;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRole {

    private String code;

    private String label;

    private Instant createdAt;
}
