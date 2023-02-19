package ru.openblocks.roles.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.openblocks.roles.api.dto.role.create.request.RoleCreateRequest;
import ru.openblocks.roles.api.dto.role.create.response.RoleCreateResponse;
import ru.openblocks.roles.api.dto.role.get.response.RoleGetResponse;
import ru.openblocks.roles.service.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public RoleCreateResponse createRole(@AuthenticationPrincipal Jwt principal,
                                         @RequestBody @Valid RoleCreateRequest request) {
        return roleService.createRole(principal, request);
    }

    @GetMapping("/{roleCode}")
    public RoleGetResponse findByCode(@PathVariable String roleCode) {
        return roleService.findByCode(roleCode);
    }

    @GetMapping
    public List<RoleGetResponse> findAll() {
        return roleService.getAllRoles();
    }
}
