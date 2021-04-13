package torimia.arena.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import torimia.arena.ArenaRepository;
import torimia.arena.dto.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
@Transactional
public class BattleServiceImpl implements BattleService {

    @Value("${battle-service.int.time-to-sleep}")
    private final int timeToSleep;

    private final SocketBattleService socketBattleService;
    private final ArenaRepository arenaRepository;

    @Override
    public BattleDtoResult battle(BattleDto dto) {

        List<SuperheroDtoForBattle> listOfFighters = chooseOrderOfFightersAttask(dto.getSuperheroes());

        Instant beginBattle = Instant.now();
        BattleDtoResult battleResult = battle(listOfFighters, dto.getId());
        Instant endBattle = Instant.now();

        battleResult.setStartOfBattle(beginBattle);
        battleResult.setEndOfBattle(endBattle);

        battleResult.setId(dto.getId());

        socketBattleService.sendResult(battleResult);
        return battleResult;
    }

    private List<SuperheroDtoForBattle> chooseOrderOfFightersAttask(List<SuperheroDtoForBattle> superheroes) {
        List<SuperheroDtoForBattle> orderOfFightersAttask = new ArrayList<>();
        for (int i = 0; i < superheroes.size() + orderOfFightersAttask.size(); i++) {
            SuperheroDtoForBattle chosenSuperhero = superheroes.get((int) (Math.random() * (superheroes.size())));
            orderOfFightersAttask.add(chosenSuperhero);
            superheroes.remove(chosenSuperhero);
        }
        return orderOfFightersAttask;
    }

    private BattleDtoResult battle(List<SuperheroDtoForBattle> fighters, Long battleId) {
        List<SuperheroDtoForBattle> defenders = fighters;
        SuperheroDtoForBattle winner = new SuperheroDtoForBattle();
        while (!defenders.isEmpty()) {
            for (int i = 0; i < fighters.size(); i++) {
                if (!defenders.contains(fighters.get(i)))
                    continue;
                SuperheroDtoForBattle attacker = fighters.get(i);
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

            BattleProgressDto progress = BattleProgressDto.builder()
                    .battleId(battleId)
                    .attackerId(attacker.getId())
                    .defenderId(defender.getId())
                    .damage(defender.getHealth() - healthBeforeAttack)
                    .residualHealth(defender.getHealth())
                    .build();

            log.info(String.valueOf(progress));
            Mono<BattleProgressDto> dto = arenaRepository.save(progress);
            System.out.println(dto.map(o -> o));

            Thread.sleep(timeToSleep);
        }
        aliveDefenders = defenders.stream().filter(SuperheroDtoForBattle::isAlive).collect(Collectors.toList());
        return aliveDefenders;
    }

    private BattleDtoResult getBattleResult(SuperheroDtoForBattle winner) {
        BattleDtoResult battleResult = new BattleDtoResult();
        battleResult.setWinnerId(winner.getId());
        battleResult.setAttackNumber(winner.getAttackCount());
        return battleResult;
    }
}
