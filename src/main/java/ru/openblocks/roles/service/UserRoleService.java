package ru.openblocks.roles.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.roles.common.SpecialUserRoles;
import ru.openblocks.roles.exception.RoleNotFoundException;
import ru.openblocks.roles.exception.UserHasNoRoleException;
import ru.openblocks.roles.persistence.entity.RoleEntity;
import ru.openblocks.roles.persistence.entity.UserRoleEntity;
import ru.openblocks.roles.persistence.repository.RoleRepository;
import ru.openblocks.roles.persistence.repository.UserRoleRepository;
import ru.openblocks.roles.service.mapper.UserRoleMapper;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserRoleService {

    /**
     * Заголовок в JWT-токене, из которого извлекается username.
     */
    private static final String PREFERRED_USERNAME_HEADER = "preferred_username";

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private final UserRoleMapper userRoleMapper;

    private UserRoleService userRoleService;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository,
                           UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    @Lazy
    @Autowired
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * Добавляет новую роль пользователю от лица другого пользователя.
     * Если пользователь, который добавляет новую роль, не имеет таких прав, произойдет ошибка.
     *
     * @param principal JWT-токен пользователя, добавляющего роль
     * @param userName  логин пользователя, которому добавляется роль
     * @param roleCode  код роли
     */
    @Transactional
    public void addRoleToUser(Jwt principal, String userName, String roleCode) {

        // Проверяем основные параметры запроса
        if (Objects.isNull(userName)) {
            throw new IllegalArgumentException("Cannot add new role, userName is null");
        }
        if (Objects.isNull(roleCode)) {
            throw new IllegalArgumentException("Cannot add new role, code of role is null");
        }

        // Проверяем, что текущий пользователь имеет право добавлять новые роли пользователям
        final String issuerUserName = getUserNameFromJwt(principal);
        log.info("User {} is trying to add new role {} to user {}", issuerUserName, roleCode, userName);
        if (!userRoleService.isRolesAdministrator(issuerUserName)) {
            throw new UserHasNoRoleException("User " + issuerUserName + " has no rights to add new roles to users");
        }

        // Проверяем существование роли, если её нет, можно сразу возвращать ошибку
        final RoleEntity role = roleRepository.findById(roleCode)
                .orElseThrow(() -> new RoleNotFoundException("Cannot find role by code: " + roleCode));

        // Если текущий пользователь уже имеет такую роль, то не добавляем новую, при этом
        // не следует возвращать никаких ошибок, если требуется добавить роль, а она уже есть,
        // следует считать задачу успешно выполненной
        if (!userRoleService.hasRole(userName, roleCode)) {
            final UserRoleEntity userRole = userRoleMapper.toUserRole(issuerUserName, userName, role);
            userRoleRepository.saveAndFlush(userRole);
        }
    }

    /**
     * Проверяет, что у пользователя есть роль, заданная кодом. Пользователь задается его логином - userName.
     *
     * @param userName логин пользователя
     * @param roleCode код роли
     * @return true, если у пользователя есть указанная роль
     */
    @Transactional(readOnly = true)
    public boolean hasRole(String userName, String roleCode) {
        return userRoleRepository.userHasRole(userName, roleCode);
    }

    /**
     * Проверяет, является ли текущий пользователь администратором ролей.
     *
     * @param principal JWT-токен
     * @return true, если текущий пользователь является администратором ролей
     */
    @Transactional(readOnly = true)
    public boolean isRoleAdministrator(Jwt principal) {
        final String userName = getUserNameFromJwt(principal);
        return hasRole(userName, SpecialUserRoles.ROLES_ADMINISTRATOR.getCode());
    }

    /**
     * Проверяет, является ли текущий пользователь администратором ролей.
     *
     * @param userName логин пользователя
     * @return true, если текущий пользователь является администратором ролей
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Transactional(readOnly = true)
    public boolean isRolesAdministrator(String userName) {
        return hasRole(userName, SpecialUserRoles.ROLES_ADMINISTRATOR.getCode());
    }

    /**
     * Извлекает поле "preferred_username" из JWT-токена.
     *
     * @param principal JWT-токен
     * @return username пользователя из JWT-токена
     */
    public String getUserNameFromJwt(Jwt principal) {
        if (Objects.nonNull(principal)) {
            final Map<String, Object> claims = principal.getClaims();
            if (Objects.nonNull(claims)) {
                return (String) claims.get(PREFERRED_USERNAME_HEADER);
            }
        }
        return null;
    }
}
