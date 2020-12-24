package torimia.arena.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleResult;
import torimia.arena.dto.MessageDto;
import torimia.arena.services.BattleService;

@Slf4j
@RequiredArgsConstructor
@Component
public class BattleHandlerImpl implements BattleHandler{

    private static final String BATTLE_RESULT_URL = "/arena/battle/result";
    private static final String BATTLE_RESULT_ERROR_URL = "/arena/battle/result_error";

    private final RestTemplate restTemplate;
    private final BattleService service;

    @Async
    public void sendResponseToSuperheroController(BattleDto dto) {
        try {
            BattleResult result = service.battle(dto);
            restTemplate.postForEntity(BATTLE_RESULT_URL, result, String.class);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            restTemplate.postForEntity(BATTLE_RESULT_ERROR_URL, new MessageDto("Fight not finished"), String.class);
        }
    }
}
