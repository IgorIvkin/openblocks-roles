package ru.openblocks.roles.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.openblocks.roles.api.dto.error.ErrorResponse;
import ru.openblocks.roles.exception.RoleAlreadyExistsException;
import ru.openblocks.roles.exception.RoleNotFoundException;
import ru.openblocks.roles.exception.UserHasNoRoleException;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return handleDefaultException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentException(IllegalArgumentException ex) {
        return handleDefaultException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse roleNotFoundException(RoleNotFoundException ex) {
        return handleDefaultException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse userHasNoRoleException(UserHasNoRoleException ex) {
        return handleDefaultException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse roleAlreadyExistsException(RoleAlreadyExistsException ex) {
        return handleDefaultException(ex);
    }

    private ErrorResponse handleDefaultException(Throwable ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }
}
