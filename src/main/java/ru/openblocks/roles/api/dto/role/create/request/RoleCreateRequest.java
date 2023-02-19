package ru.openblocks.roles.api.dto.role.create.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateRequest {

    /**
     * Код роли.
     */
    @NotBlank
    private String code;

    /**
     * Название роли.
     */
    @NotBlank
    private String label;
}
