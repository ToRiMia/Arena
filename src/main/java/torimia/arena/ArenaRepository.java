package torimia.arena;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import torimia.arena.entity.BattleProgress;

@Repository
public interface ArenaRepository extends ReactiveCrudRepository<BattleProgress, Long> {

    Flux<BattleProgress> findByBattleId(Long id);
}
