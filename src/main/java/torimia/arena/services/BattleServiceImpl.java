package torimia.arena.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import torimia.arena.ArenaRepository;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;
import torimia.arena.dto.BattleProgressRoundDto;
import torimia.arena.dto.SuperheroDtoForBattle;
import torimia.arena.entity.BattleProgress;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Data
@RequiredArgsConstructor
//@Service
public class BattleServiceImpl implements BattleService {

    @Value("${battle-service.int.time-to-sleep}")
    private final int timeToSleep;

    private final SocketBattleService socketBattleService;
    private final ArenaRepository arenaRepository;

    private static int counter = 20;

    @Override
    public Mono<BattleDtoResult> battle(Mono<BattleDto> dto) {


//        List<SuperheroDtoForBattle> listOfFighters = chooseOrderOfFightersAttask(dto.getSuperheroes());

        Instant beginBattle = Instant.now();
//        Mono<BattleDto> listOfFighters = dto.map(value -> chooseOrderOfFightersAttask(value));

        dto.map(dto1 -> Mono.just(dto1.getSuperheroes()));


        Stream<Stream<Integer>> streamStream = Stream.of(1, 2, 3).map(i -> Stream.of(i));
        Stream<Integer> streamStream2 = Stream.of(1, 2, 3).flatMap(i -> Stream.of(i));
        Stream.of(1,2,3).map(i -> i);

        Mono<BattleDtoResult> result = dto.map(value -> battle(chooseOrderOfFightersAttask(value), value.getId()));

//        result.subscribe();
//        BattleDtoResult battleResult = battle(listOfFighters, dto.getId());
        Instant endBattle = Instant.now();

//        battleResult.setStartOfBattle(beginBattle);
//        battleResult.setEndOfBattle(endBattle);

//        battleResult.setId(dto.getId());

//        socketBattleService.sendResult(battleResult);
        return result;
    }

    private BattleDto chooseOrderOfFightersAttask(BattleDto dto) {
        List<SuperheroDtoForBattle> orderOfFightersAttask = new ArrayList<>();
        List<SuperheroDtoForBattle> superheroes = dto.getSuperheroes();

        for (int i = 0; i < superheroes.size() + orderOfFightersAttask.size(); i++) {
            SuperheroDtoForBattle chosenSuperhero = superheroes.get((int) (Math.random() * (superheroes.size())));
            orderOfFightersAttask.add(chosenSuperhero);
            superheroes.remove(chosenSuperhero);
        }

        dto.setSuperheroes(orderOfFightersAttask);
        return dto;
    }

    private BattleDtoResult battle(BattleDto battleDto, Long battleId) {
            List<SuperheroDtoForBattle> fighters = battleDto.getSuperheroes();

            List<SuperheroDtoForBattle> defenders = fighters;
            SuperheroDtoForBattle winner = new SuperheroDtoForBattle();
            while (!defenders.isEmpty()) {
                for (int i = 0; i < fighters.size(); i++) {
                    SuperheroDtoForBattle attacker = fighters.get(i);
                    if (!defenders.contains(attacker))
                        continue;

                    defenders = fighters.stream().filter(fighter -> (!fighter.equals(attacker) && (fighter.isAlive()))).collect(Collectors.toList());
                    defenders = roundTryCatch(attacker, defenders, battleId);
                    winner = attacker;
                }
            }
            return getBattleResult(winner);
    }

    private List<SuperheroDtoForBattle> roundTryCatch(SuperheroDtoForBattle attacker, List<SuperheroDtoForBattle> defenders, Long battleId) {
        try {
            socketBattleService.sendProgress(new BattleProgressRoundDto(attacker, defenders));
            return round(attacker, defenders, battleId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Battle not finished");
            return new ArrayList<>();
        }
    }

    private List<SuperheroDtoForBattle> round(SuperheroDtoForBattle attacker, List<SuperheroDtoForBattle> defenders, Long battleId) throws InterruptedException {
        List<SuperheroDtoForBattle> aliveDefenders;

        for (SuperheroDtoForBattle defender : defenders) {
            int healthBeforeAttack = defender.getHealth();
            attacker.attack(defender);

            saveBattleProgress(attacker, battleId, defender, healthBeforeAttack);

            Thread.sleep(timeToSleep);

            System.out.println(Thread.currentThread().getName() + " " + counter);
//            counter = counter + 10;
//            Thread.sleep(counter);

        }
        aliveDefenders = defenders.stream().filter(SuperheroDtoForBattle::isAlive).collect(Collectors.toList());
        return aliveDefenders;
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

    private BattleDtoResult getBattleResult(SuperheroDtoForBattle winner) {
        BattleDtoResult battleResult = new BattleDtoResult();
        battleResult.setWinnerId(winner.getId());
        battleResult.setAttackNumber(winner.getAttackCount());
        return battleResult;
    }
}
