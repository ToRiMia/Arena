package torimia.arena.handlers;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleResult;
import torimia.arena.dto.FightStatus;
import torimia.arena.dto.MessageDtoMQ;
import torimia.arena.services.BattleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListenerMQ {

    private final BattleService service;

    @Value("${rabbitmq.queue.fight-status.name}")
    private String fightStatusQueueName;
    @Value("${rabbitmq.queue.battle-result.name}")
    private String battleResultQueueName;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "battle")
    public void sendResponseToSuperheroControllerRabbit(BattleDto dto) {
        try {
            log.info("Received BattleDto for starting battle: {}", dto);
            rabbitTemplate.convertAndSend(fightStatusQueueName, new MessageDtoMQ(dto.getId(), FightStatus.STARTED));
            BattleResult result = service.battle(dto);
            log.info("Battle result: {}", result);
            rabbitTemplate.convertAndSend(battleResultQueueName, result);

            if (((int) (Math.random() * 10)) == 5)
                throw new RuntimeException();
        } catch (Exception ex) {
            log.error("Error in sending battle result: {}, message: {}", ex, ex.getMessage());
            rabbitTemplate.convertAndSend(fightStatusQueueName, new MessageDtoMQ(dto.getId(), FightStatus.FINISHED_UNSUCCESSFUL));
        }
    }
}
