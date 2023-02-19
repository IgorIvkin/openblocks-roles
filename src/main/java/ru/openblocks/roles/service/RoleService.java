package ru.openblocks.roles.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.roles.api.dto.role.create.request.RoleCreateRequest;
import ru.openblocks.roles.api.dto.role.create.response.RoleCreateResponse;
import ru.openblocks.roles.api.dto.role.get.response.RoleGetResponse;
import ru.openblocks.roles.exception.RoleAlreadyExistsException;
import ru.openblocks.roles.exception.RoleNotFoundException;
import ru.openblocks.roles.exception.UserHasNoRoleException;
import ru.openblocks.roles.persistence.entity.RoleEntity;
import ru.openblocks.roles.persistence.repository.RoleRepository;
import ru.openblocks.roles.service.mapper.RoleMapper;

import java.util.List;

/**
 * Этот сервис предназначен для работы с ролями, добавления и изменения ролей.
 */
@Slf4j
@Service
public class RoleService {


    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private UserRoleService userRoleService;

    @Autowired
    public RoleService(RoleRepository roleRepository,
                       RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Lazy
    @Autowired
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * Добавляет новую роль в систему, если такой код роли еще не существовал.
     *
     * @param principal JWT-токен пользователя
     * @param request   запрос на добавление новой роли
     * @return код новой роли
     */
    @Transactional
    public RoleCreateResponse createRole(Jwt principal, RoleCreateRequest request) {

        // Проверяем, что текущий пользователь имеет нужную роль
        final String userName = userRoleService.getUserNameFromJwt(principal);
        if (!userRoleService.isRolesAdministrator(userName)) {
            throw new UserHasNoRoleException("User " + userName + " cannot add new roles");
        }

        log.info("User {} is trying to create new role {}", userName, request);

        // Проверяем существование роли по такому коду
        final String code = request.getCode();
        if (roleRepository.existsById(code)) {
            throw new RoleAlreadyExistsException("Role " + code + " already presented");
        }

        final RoleEntity role = roleMapper.toRole(request);
        roleRepository.saveAndFlush(role);

        return RoleCreateResponse.builder()
                .code(code)
                .build();
    }

    /**
     * Возвращает все возможные роли в системе.
     *
     * @return список возможных ролей в системе
     */
    @Transactional(readOnly = true)
    public List<RoleGetResponse> getAllRoles() {
        final List<RoleEntity> roles = roleRepository.findAll();
        return roleMapper.toListOfDto(roles);
    }

    /**
     * Возвращает роль по её коду.
     *
     * @param code код роли
     * @return роль по коду
     */
    @Transactional(readOnly = true)
    public RoleGetResponse findByCode(String code) {
        final RoleEntity role = roleRepository.findById(code)
                .orElseThrow(() -> new RoleNotFoundException("Cannot find role by code: " + code));
        return roleMapper.toDto(role);
    }

}
