package torimia.arena.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import torimia.arena.dto.BattleProgressDto;
import torimia.arena.services.BattleResultService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BattleController {

    private final BattleResultService service;

    @GetMapping(value = "/battle/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<BattleProgressDto> getByBattleId(@PathVariable Long id) {
        log.info("some");
        return service.getBattleResult(id);
    }
}
