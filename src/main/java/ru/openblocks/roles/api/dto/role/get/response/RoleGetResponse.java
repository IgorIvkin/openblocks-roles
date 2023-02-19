package ru.openblocks.roles.api.dto.role.get.response;

import lombok.Data;

@Data
public class RoleGetResponse {

    /**
     * Код роли.
     */
    private String code;

    /**
     * Название роли.
     */
    private String label;
}
