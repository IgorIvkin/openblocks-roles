package ru.openblocks.roles.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "roles-kafka")
public class RolesKafkaConfig {

    /**
     * Отправлять ли изменения по ролям в Кафку.
     */
    private Boolean replayChanges;

    private String bootstrapServers;

    private RolesKafkaSsl ssl;

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RolesKafkaSsl {

        private Boolean enabled;

        private String truststoreLocation;

        private String truststorePassword;

        private String keystoreLocation;

        private String keystorePassword;

        private String keyPassword;
    }
}
