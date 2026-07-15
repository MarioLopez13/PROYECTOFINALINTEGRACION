package ec.edu.udla.campusconnect.academic.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CampusRabbitConfig {

    public static final String EVENTS_EXCHANGE = "campus.events";
    public static final String DEAD_LETTER_EXCHANGE = "campus.dlx";
    public static final String ACADEMIC_PAYMENT_QUEUE = "academic.payment-confirmed";
    public static final String ACADEMIC_DEAD_LETTER_QUEUE = "academic.dead-letter";

    @Bean
    TopicExchange campusEventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    DirectExchange campusDeadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    Queue academicPaymentQueue() {
        return QueueBuilder.durable(ACADEMIC_PAYMENT_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "academic.dead")
                .build();
    }

    @Bean
    Queue academicDeadLetterQueue() {
        return QueueBuilder.durable(ACADEMIC_DEAD_LETTER_QUEUE).build();
    }

    @Bean
    Binding academicPaymentBinding(Queue academicPaymentQueue, TopicExchange campusEventsExchange) {
        return BindingBuilder.bind(academicPaymentQueue)
                .to(campusEventsExchange)
                .with("payment.confirmed");
    }

    @Bean
    Binding academicDeadLetterBinding(Queue academicDeadLetterQueue, DirectExchange campusDeadLetterExchange) {
        return BindingBuilder.bind(academicDeadLetterQueue)
                .to(campusDeadLetterExchange)
                .with("academic.dead");
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
