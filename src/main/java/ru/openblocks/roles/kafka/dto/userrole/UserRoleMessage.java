package ru.openblocks.roles.kafka.dto.userrole;

import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMessage {

    private UUID messageId;

    private Instant createdAt;

    private String userName;

    @Builder.Default
    private List<UserRoleRole> roles = new ArrayList<>();
}
