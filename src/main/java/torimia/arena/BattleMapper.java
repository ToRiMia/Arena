package torimia.arena;

import org.mapstruct.Mapper;
import torimia.arena.dto.BattleProgressDto;
import torimia.arena.entity.BattleProgress;

@Mapper(componentModel = "spring")
public interface BattleMapper {

    BattleProgressDto toDto(BattleProgress progress);
}
