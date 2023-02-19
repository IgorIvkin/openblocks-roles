package ru.openblocks.roles.service.mapper;

import org.springframework.stereotype.Component;
import ru.openblocks.roles.persistence.entity.RoleEntity;
import ru.openblocks.roles.persistence.entity.UserRoleEntity;

import java.time.Clock;
import java.time.Instant;

@Component
public class UserRoleMapper {

    private final Clock clock = Clock.systemDefaultZone();

    public UserRoleEntity toUserRole(String issuerUserName, String userName, RoleEntity role) {
        return UserRoleEntity.builder()
                .userName(userName)
                .roleCode(role)
                .grantBy(issuerUserName)
                .createdAt(Instant.now(clock))
                .build();
    }
}
