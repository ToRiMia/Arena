package torimia.arena.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;
import torimia.arena.services.BattleService;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListenerMQ {

    private final BattleService service;

    @Value("${rabbitmq.queue.battle-status.name}")
    private final String battleStatusQueueName;
    @Value("${rabbitmq.queue.battle-result.name}")
    private final String battleResultQueueName;
    //    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    private final Sender sender;
    private final ObjectMapper mapper;

    @PostConstruct
    private void init() {
        receiver.consumeNoAck("battle")
                .publishOn(Schedulers.boundedElastic())
                .map(Delivery::getBody)
                .map(bytes -> {
                    try {
                        return mapper.readValue(bytes, BattleDto.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new BattleDto();
                    }
                })
                .publish(this::sendResponseToSuperheroControllerRabbit)
                .parallel()
                .subscribe();

//                .(sender::sendResult)
//                .subscribe(v -> {
//                    log.info("Received message from first listener {}", new String(v));
//                    try {
//                        sendResponseToSuperheroControllerRabbit(Mono.just(mapper.readValue(v, BattleDto.class)).publishOn(Schedulers.boundedElastic()));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }, Throwable::printStackTrace);
    }

    public Flux<BattleDtoResult> sendResponseToSuperheroControllerRabbit(Flux<BattleDto> reactiveDto) {
        return reactiveDto
                .publish(this::sendStartedStatus)
//                .publishOn(Schedulers.boundedElastic())
                .publish(service::battle)
                .publish(this::sendResult);
    }

    private Flux<BattleDtoResult> sendResult(Flux<BattleDtoResult> flux) {
        return flux
                .doOnNext(value -> {
                    try {
                        if (((int) (Math.random() * 10)) == 5) {
                            throw new BattleNotFinishedException("Generated exception for checking service work during fatal battle ending");
                        }
                        log.info("Result of battle: {}", value);
                        log.info("-----------Battle {} finished-------------", value.getId());
//                        sender
//                                .send(Flux.just(new OutboundMessage("", battleResultQueueName, value.toString().getBytes())))
//                                .subscribe();

                    } catch (Exception ex) {
                        log.error("Error in sending battle result: " + ex);
//            rabbitTemplate.convertAndSend(battleStatusQueueName, new BattleStatusDto(dto.getId(), BattleStatus.FINISHED_UNSUCCESSFUL));
                    }
                });
    }

    private Flux<BattleDto> sendStartedStatus(Flux<BattleDto> flux) {
        return flux.doOnNext(value -> {
            log.info("-----------Battle {} started-------------", value.getId());
            log.info("Received BattleDto for starting battle: {}", value);
//                        sender
//                                .send(Flux.just(new OutboundMessage("", battleStatusQueueName,
//                                        new BattleStatusDto(value.getId(), BattleStatus.STARTED).toString().getBytes())))
//                                .subscribe();
        });
    }


//    @RabbitListener(queues = "battle", concurrency = "5")
//    public void sendResponseToSuperheroControllerRabbit(BattleDto dto) {
//        try {
//
//            log.info("-----------Battle {} started-------------", dto.getId());
//            log.info("Received BattleDto for starting battle: {}", dto);
//            rabbitTemplate.convertAndSend(battleStatusQueueName, new BattleStatusDto(dto.getId(), BattleStatus.STARTED));
//
//            BattleDtoResult result =  service.battle(dto);
//
//            if (((int) (Math.random() * 10)) == 5)
//                throw new BattleNotFinishedException("Generated exception for checking service work during fatal battle ending");
//
//            log.info("Battle result: {}", result);
//            rabbitTemplate.convertAndSend(battleResultQueueName, result);
//            log.info("-----------Battle {} finished-------------", dto.getId());
//        } catch (Exception ex) {
//            log.error("Error in sending battle result: " + ex);
//            rabbitTemplate.convertAndSend(battleStatusQueueName, new BattleStatusDto(dto.getId(), BattleStatus.FINISHED_UNSUCCESSFUL));
//        }
//    }

}

