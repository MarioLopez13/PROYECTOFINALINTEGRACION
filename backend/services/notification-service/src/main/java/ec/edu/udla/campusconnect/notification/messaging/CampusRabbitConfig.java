package ec.edu.udla.campusconnect.notification.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CampusRabbitConfig {

    public static final String EVENTS_EXCHANGE = "campus.events";
    public static final String DEAD_LETTER_EXCHANGE = "campus.dlx";
    public static final String NOTIFICATION_EVENTS_QUEUE = "notification.business-events";
    public static final String NOTIFICATION_DEAD_LETTER_QUEUE = "notification.dead-letter";

    @Bean
    TopicExchange campusEventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    DirectExchange campusDeadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    Queue notificationEventsQueue() {
        return QueueBuilder.durable(NOTIFICATION_EVENTS_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "notification.dead")
                .build();
    }

    @Bean
    Queue notificationDeadLetterQueue() {
        return QueueBuilder.durable(NOTIFICATION_DEAD_LETTER_QUEUE).build();
    }

    @Bean
    Binding studentEnrolledBinding(Queue notificationEventsQueue, TopicExchange campusEventsExchange) {
        return BindingBuilder.bind(notificationEventsQueue).to(campusEventsExchange).with("student.enrolled");
    }

    @Bean
    Binding paymentConfirmedBinding(Queue notificationEventsQueue, TopicExchange campusEventsExchange) {
        return BindingBuilder.bind(notificationEventsQueue).to(campusEventsExchange).with("payment.confirmed");
    }

    @Bean
    Binding attendanceRecordedBinding(Queue notificationEventsQueue, TopicExchange campusEventsExchange) {
        return BindingBuilder.bind(notificationEventsQueue).to(campusEventsExchange).with("attendance.recorded");
    }

    @Bean
    Binding incidentReportedBinding(Queue notificationEventsQueue, TopicExchange campusEventsExchange) {
        return BindingBuilder.bind(notificationEventsQueue).to(campusEventsExchange).with("incident.reported");
    }

    @Bean
    Binding notificationDeadLetterBinding(Queue notificationDeadLetterQueue, DirectExchange campusDeadLetterExchange) {
        return BindingBuilder.bind(notificationDeadLetterQueue).to(campusDeadLetterExchange).with("notification.dead");
    }

    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
