package torimia.arena.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import torimia.arena.dto.BattleDtoResult;
import torimia.arena.dto.BattleProgressDto;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SocketBattleService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendProgress(BattleProgressDto battleProgress) {
        this.simpMessagingTemplate.convertAndSend("/topic/battle_progress", battleProgress);
    }

    public void sendResult(BattleDtoResult result) {
        this.simpMessagingTemplate.convertAndSend("/topic/battle_result", result);
    }

//    @SendTo("/topic/battle_progress")
//    public BattleProgressDto sendProgress(BattleProgressDto battleProgress) {
//        log.info("Send progress to topic: {}", battleProgress);
//        return battleProgress;
//    }
}
