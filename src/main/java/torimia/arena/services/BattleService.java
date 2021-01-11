package torimia.arena.services;

import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;

public interface BattleService {

    BattleDtoResult battle(BattleDto dto);
}
