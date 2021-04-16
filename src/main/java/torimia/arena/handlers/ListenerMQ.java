package torimia.arena.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleStatus;
import torimia.arena.dto.BattleStatusDto;
import torimia.arena.services.BattleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListenerMQ {

    private final BattleService service;

    @Value("${rabbitmq.queue.battle-status.name}")
    private final String battleStatusQueueName;
    @Value("${rabbitmq.queue.battle-result.name}")
    private final String battleResultQueueName;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "battle", concurrency = "5")
    public void sendResponseToSuperheroControllerRabbit(BattleDto dto) {
        try {
            Mono<BattleDto> reactiveDto = Mono.just(dto);

            reactiveDto
                    .doOnNext(value -> {
                        log.info("-----------Battle {} started-------------", value.getId());
                        log.info("Received BattleDto for starting battle: {}", value);
                        rabbitTemplate.convertAndSend(battleStatusQueueName, new BattleStatusDto(value.getId(), BattleStatus.STARTED));
                        System.out.println(Thread.currentThread().getName() + " ------------It's start");
                    })
                    .subscribe();

            if (((int) (Math.random() * 10)) == 5) {
                throw new BattleNotFinishedException("Generated exception for checking service work during fatal battle ending");
            }

            service.battle(reactiveDto).subscribe(value -> {
                log.info("Result of battle: {}", value);
                rabbitTemplate.convertAndSend(battleResultQueueName, value);
                log.info("-----------Battle {} finished-------------", value.getId());
                System.out.println(Thread.currentThread().getName() + " -----------It's result");
            });

        } catch (Exception ex) {
            log.error("Error in sending battle result: " + ex);
            rabbitTemplate.convertAndSend(battleStatusQueueName, new BattleStatusDto(dto.getId(), BattleStatus.FINISHED_UNSUCCESSFUL));
        }
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
