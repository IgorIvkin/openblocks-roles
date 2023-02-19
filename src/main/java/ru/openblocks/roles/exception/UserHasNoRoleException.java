package ru.openblocks.roles.exception;

public class UserHasNoRoleException extends RuntimeException {

    public UserHasNoRoleException() {
        super();
    }

    public UserHasNoRoleException(String message) {
        super(message);
    }
}
