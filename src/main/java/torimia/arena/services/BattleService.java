package torimia.arena.services;

import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleResult;

public interface BattleService {

    BattleResult battle(BattleDto dto);
}
