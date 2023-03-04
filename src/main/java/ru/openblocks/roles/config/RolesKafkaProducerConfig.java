package ru.openblocks.roles.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.openblocks.roles.kafka.dto.userrole.UserRoleMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RolesKafkaProducerConfig {

    private final RolesKafkaConfig rolesKafkaConfig;

    private final ObjectMapper objectMapper;

    @Bean
    public ProducerFactory<String, UserRoleMessage> rolesKafkaProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, rolesKafkaConfig.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        if (Boolean.TRUE.equals(rolesKafkaConfig.getSsl().getEnabled())) {
            configProps.put("security.protocol", "SSL");
            configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                    rolesKafkaConfig.getSsl().getTruststoreLocation());
            configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
                    rolesKafkaConfig.getSsl().getTruststorePassword());
            configProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
                    rolesKafkaConfig.getSsl().getKeystoreLocation());
            configProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,
                    rolesKafkaConfig.getSsl().getKeystorePassword());
            configProps.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG,
                    rolesKafkaConfig.getSsl().getKeyPassword());
        }
        DefaultKafkaProducerFactory<String, UserRoleMessage> producerFactory =
                new DefaultKafkaProducerFactory<>(configProps);
        producerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));
        return producerFactory;
    }

    @Bean
    public KafkaTemplate<String, UserRoleMessage> kafkaTemplate() {
        return new KafkaTemplate<>(rolesKafkaProducerFactory());
    }
}
