package ru.openblocks.roles.common;

/**
 * Список специальных ролей.
 */
public enum SpecialUserRoles {

    /**
     * Даёт возможность добавлять роли пользователям в системе.
     * Также даёт возможность заводить новые роли.
     */
    ROLES_ADMINISTRATOR("ROLES_ADMINISTRATOR");

    private final String code;

    SpecialUserRoles(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
