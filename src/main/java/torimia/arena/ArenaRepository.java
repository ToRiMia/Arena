package torimia.arena;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import torimia.arena.dto.BattleProgressDto;

@Repository
public interface ArenaRepository extends R2dbcRepository<BattleProgressDto, Long> {
}
