package torimia.arena.handlers;

import torimia.arena.dto.BattleDto;

public interface BattleHandler {

    void sendResponseToSuperheroController(BattleDto dto);
}
