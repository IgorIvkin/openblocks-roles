package ru.openblocks.roles.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.roles.api.dto.role.get.request.UserHasRoleRequest;
import ru.openblocks.roles.api.dto.userrole.create.request.UserRoleCreateRequest;
import ru.openblocks.roles.api.dto.userrole.delete.request.UserRoleDeleteRequest;
import ru.openblocks.roles.service.UserRoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    public void addRoleToUser(@AuthenticationPrincipal Jwt principal,
                              @RequestBody @Valid UserRoleCreateRequest request) {
        userRoleService.addRoleToUser(principal, request.getUserName(), request.getRoleCode());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/delete")
    public void deleteRoleFromUser(@AuthenticationPrincipal Jwt principal,
                                   @RequestBody @Valid UserRoleDeleteRequest request) {
        userRoleService.deleteRoleFromUser(principal, request.getUserName(), request.getRoleCode());
    }

    @PostMapping("/has-role")
    public boolean hasRole(@RequestBody @Valid UserHasRoleRequest request) {
        return userRoleService.hasRole(request.getUserName(), request.getRoleCode());
    }
}
