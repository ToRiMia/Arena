package torimia.arena.config;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.QueueBuilder;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.battle.name}")
    private String battleQueueName;

//    @Bean
//    Queue battleQueue() {
//        return QueueBuilder.durable(battleQueueName)
//                .autoDelete()
//                .build();
//    }

//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        return new Jackson2JsonMessageConverter(mapper);
//    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(jsonMessageConverter());
//        return template;
//    }
//
    @Bean
    public Receiver receiver() {
//        ReceiverOptions receiverOptions = new ReceiverOptions()
//                .connectionFactory(connectionFactoryRabbit());
////                .connectionSubscriptionScheduler(Schedulers.boundedElastic());

        return RabbitFlux.createReceiver();
    }

    @Bean
    public Sender sender() {
//        ReceiverOptions receiverOptions = new ReceiverOptions()
//                .connectionFactory(connectionFactoryRabbit());
////                .connectionSubscriptionScheduler(Schedulers.boundedElastic());

        return RabbitFlux.createSender();
    }

//    @Bean
//    Flux<Delivery> deliveryFlux(Receiver receiver) {
//        return receiver.consumeNoAck(battleQueueName);
//    }

//    @Bean
//    public Receiver receiver() {
//        return new Receiver();
//    }

//    @Bean
//    public SenderOptions senderOptions(ConnectionFactory connectionFactory) {
//        return new SenderOptions()
//                .connectionFactory(connectionFactory)
//                .resourceManagementScheduler(Schedulers.boundedElastic());
//    }

//    @Bean
//    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
//        return new AsyncRabbitTemplate(rabbitTemplate);
//    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter());
//        return factory;
//    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.useNio();
//        return factory;
//    }
}
