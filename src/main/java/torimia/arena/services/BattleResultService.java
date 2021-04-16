package torimia.arena.services;

import reactor.core.publisher.Flux;
import torimia.arena.dto.BattleProgressDto;

public interface BattleResultService {

    Flux<BattleProgressDto> getBattleResult(Long id);
}
