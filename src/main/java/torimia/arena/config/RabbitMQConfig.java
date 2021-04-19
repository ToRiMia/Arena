package torimia.arena.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.Delivery;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
import com.rabbitmq.client.Delivery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
//@EnableRabbit
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

    @Bean
    public Mono<Connection> connectionFactoryRabbit(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
//        return Mono.just(connectionFactory.newConnection());
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit"));
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(jsonMessageConverter());
//        return template;
//    }

    @Bean
    public Receiver receiver(Mono<Connection> connection) {
        ReceiverOptions receiverOptions = new ReceiverOptions()
                .connectionMono(connection)
                .connectionSubscriptionScheduler(Schedulers.boundedElastic());

        return RabbitFlux.createReceiver(receiverOptions);
    }

    @Bean
    Flux<Delivery> deliveryFlux(Receiver receiver) {
        return receiver.consumeNoAck(battleQueueName);
    } // TODO: 19.04.21 should google about  "consumeNoAck"

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
