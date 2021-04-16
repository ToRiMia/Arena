package torimia.arena.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import torimia.arena.ArenaRepository;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;
import torimia.arena.dto.SuperheroDtoForBattle;
import torimia.arena.entity.BattleProgress;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleServiceReactiveImpl implements BattleService {

    @Value("${battle-service.int.time-to-sleep}")
    private final int timeToSleep;

    private final ArenaRepository arenaRepository;

    @Override
    public Mono<BattleDtoResult> battle(Mono<BattleDto> dto) {
        return dto.map(value -> {
            BattleDtoResult result = new BattleDtoResult();
            result.setId(value.getId());
            result.setStartOfBattle(Instant.now());

            SuperheroDtoForBattle fighter1 = value.getSuperheroes().get(0);
            SuperheroDtoForBattle fighter2 = value.getSuperheroes().get(1);

//            Flux.fromIterable(value.getSuperheroes())
//                    .filter(value -> true)
//                    .flatMap(value -> value.attack(s))
//                    .map(SuperheroDtoForBattle::isAlive)
//                    .map(value -> )

            while (true) {
                int healthBeforeAttack = fighter2.getHealth();
                fighter1.attack(fighter2);
                saveBattleProgress(fighter1, value.getId(), fighter2, healthBeforeAttack);
                if (fighter2.isAlive()) {
                    healthBeforeAttack = fighter1.getHealth();
                    fighter2.attack(fighter1);
                    saveBattleProgress(fighter2, value.getId(), fighter1, healthBeforeAttack);
                } else {
                    result.setWinnerId(fighter1.getId());
                    result.setAttackNumber(fighter1.getAttackCount());
                    break;
                }
                if (!fighter1.isAlive()) {
                    result.setWinnerId(fighter2.getId());
                    result.setAttackNumber(fighter2.getAttackCount());
                    break;
                }
                try {
                    System.out.println(Thread.currentThread().getName() + " ----------It's round for " + value.getId());
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(Thread.currentThread().getName() + " ----------It's battle");
            result.setEndOfBattle(Instant.now());
            return result;
        });
    }

    private void saveBattleProgress(SuperheroDtoForBattle attacker, Long battleId, SuperheroDtoForBattle defender, int healthBeforeAttack) {
        BattleProgress progress = BattleProgress.builder()
                .battleId(battleId)
                .attackerId(attacker.getId())
                .defenderId(defender.getId())
                .damage(defender.getHealth() - healthBeforeAttack)
                .residualHealth(defender.getHealth())
                .build();

        Mono<BattleProgress> dtoMono = arenaRepository.save(progress);
        dtoMono.subscribe(value -> log.info("Saved in db: {}", value), Throwable::printStackTrace);
        System.out.println(Thread.currentThread().getName() + " save");
    }


}
