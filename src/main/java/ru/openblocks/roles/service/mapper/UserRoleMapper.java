package ru.openblocks.roles.service.mapper;

import org.springframework.stereotype.Component;
import ru.openblocks.roles.api.dto.userrole.get.response.UserRoleGetResponse;
import ru.openblocks.roles.kafka.dto.userrole.UserRoleMessage;
import ru.openblocks.roles.kafka.dto.userrole.UserRoleRole;
import ru.openblocks.roles.persistence.entity.RoleEntity;
import ru.openblocks.roles.persistence.entity.UserRoleEntity;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public UserRoleMessage toUserRoleMessage(String userName, List<UserRoleEntity> userRoles) {
        return UserRoleMessage.builder()
                .messageId(UUID.randomUUID())
                .createdAt(Instant.now(clock))
                .userName(userName)
                .roles(toUserRoles(userRoles))
                .build();
    }

    public List<UserRoleGetResponse> toUserRolesGetResponse(List<UserRoleEntity> userRoles) {
        return userRoles.stream()
                .map(this::toUserRoleGetResponse)
                .collect(Collectors.toList());
    }

    private UserRoleGetResponse toUserRoleGetResponse(UserRoleEntity userRole) {
        return UserRoleGetResponse.builder()
                .code(userRole.getRoleCode().getCode())
                .label(userRole.getRoleCode().getLabel())
                .createdAt(userRole.getCreatedAt())
                .grantBy(userRole.getGrantBy())
                .build();
    }

    private List<UserRoleRole> toUserRoles(List<UserRoleEntity> userRoles) {
        return userRoles.stream()
                .map(this::toUserRole)
                .collect(Collectors.toList());
    }

    private UserRoleRole toUserRole(UserRoleEntity userRole) {
        return UserRoleRole.builder()
                .code(userRole.getRoleCode().getCode())
                .label(userRole.getRoleCode().getLabel())
                .createdAt(userRole.getCreatedAt())
                .build();
    }

}
