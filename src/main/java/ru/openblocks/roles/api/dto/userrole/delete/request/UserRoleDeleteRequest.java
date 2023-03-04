package ru.openblocks.roles.api.dto.userrole.delete.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRoleDeleteRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String roleCode;
}
