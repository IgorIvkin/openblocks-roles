package ru.openblocks.roles.api.dto.role.create.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateResponse {

    /**
     * Код роли.
     */
    private String code;
}
