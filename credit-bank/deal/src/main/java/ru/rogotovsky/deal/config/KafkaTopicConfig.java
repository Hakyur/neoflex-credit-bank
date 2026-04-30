package ru.rogotovsky.deal.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static ru.rogotovsky.deal.util.KafkaTopics.COUNT_PARTITIONS;
import static ru.rogotovsky.deal.util.KafkaTopics.COUNT_REPLICAS;
import static ru.rogotovsky.deal.util.KafkaTopics.CREATE_DOCUMENTS;
import static ru.rogotovsky.deal.util.KafkaTopics.CREDIT_ISSUED;
import static ru.rogotovsky.deal.util.KafkaTopics.FINISH_REGISTRATION;
import static ru.rogotovsky.deal.util.KafkaTopics.SEND_DOCUMENTS;
import static ru.rogotovsky.deal.util.KafkaTopics.SEND_SES;
import static ru.rogotovsky.deal.util.KafkaTopics.STATEMENT_DENIED;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic finishRegistrationTopic() {
        return buildTopic(FINISH_REGISTRATION);
    }

    @Bean
    public NewTopic createDocumentsTopic() {
        return buildTopic(CREATE_DOCUMENTS);
    }

    @Bean
    public NewTopic sendDocumentsTopic() {
        return buildTopic(SEND_DOCUMENTS);
    }

    @Bean
    public NewTopic sendSesTopic() {
        return buildTopic(SEND_SES);
    }

    @Bean
    public NewTopic creditIssuedTopic() {
        return buildTopic(CREDIT_ISSUED);
    }

    @Bean
    public NewTopic statementDeniedTopic() {
        return buildTopic(STATEMENT_DENIED);
    }

    private NewTopic buildTopic(String name) {
        return TopicBuilder.name(name)
                .partitions(COUNT_PARTITIONS)
                .replicas(COUNT_REPLICAS)
                .build();
    }
}
