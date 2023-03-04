package ru.openblocks.roles.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.roles.config.RolesKafkaConfig;
import ru.openblocks.roles.kafka.dto.userrole.UserRoleMessage;
import ru.openblocks.roles.persistence.entity.UserRoleEntity;
import ru.openblocks.roles.persistence.repository.UserRoleRepository;
import ru.openblocks.roles.service.mapper.UserRoleMapper;

import java.util.List;

@Slf4j
@Component
public class RolesKafkaSender {

    private static final String USERS_ROLES_TOPIC_NAME = "users-roles";

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    private final RolesKafkaConfig rolesKafkaConfig;

    private final KafkaTemplate<String, UserRoleMessage> kafkaTemplate;

    @Autowired
    public RolesKafkaSender(UserRoleRepository userRoleRepository,
                            UserRoleMapper userRoleMapper,
                            RolesKafkaConfig rolesKafkaConfig,
                            KafkaTemplate<String, UserRoleMessage> kafkaTemplate) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
        this.rolesKafkaConfig = rolesKafkaConfig;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправляет в Кафку текущий снапшот всех ролей пользователя.
     *
     * @param userName логин пользователя
     */
    @Transactional
    public void sendRolesSnapshotByUser(String userName) {

        // Фиксируем все изменения, чтобы получить самое актуальное состояние ролей
        userRoleRepository.flush();

        // Формируем сообщение в Кафку, если включен режим отображения изменений в Кафку
        if (Boolean.TRUE.equals(rolesKafkaConfig.getReplayChanges())) {

            List<UserRoleEntity> userRoles = userRoleRepository.findByUserName(userName);
            UserRoleMessage userRoleMessage = userRoleMapper.toUserRoleMessage(userName, userRoles);

            log.info("Send user roles snapshot to Kafka for user {}", userName);
            kafkaTemplate.send(USERS_ROLES_TOPIC_NAME, userName, userRoleMessage);
        }
    }
}
