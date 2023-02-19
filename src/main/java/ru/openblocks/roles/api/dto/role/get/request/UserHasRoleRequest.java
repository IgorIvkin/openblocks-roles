package ru.openblocks.roles.api.dto.role.get.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserHasRoleRequest {

    /**
     * Логин пользователя.
     */
    @NotBlank
    private String userName;

    /**
     * Код роли.
     */
    @NotBlank
    private String roleCode;
}
