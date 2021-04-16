package torimia.arena.services;

import reactor.core.publisher.Mono;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;

public interface BattleService {

    Mono<BattleDtoResult> battle(Mono<BattleDto> dto);
}
