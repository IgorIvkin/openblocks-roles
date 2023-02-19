package ru.openblocks.roles.exception;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException() {
        super();
    }

    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
