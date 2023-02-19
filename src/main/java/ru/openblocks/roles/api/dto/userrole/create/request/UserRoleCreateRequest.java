package ru.openblocks.roles.api.dto.userrole.create.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRoleCreateRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String roleCode;
}
