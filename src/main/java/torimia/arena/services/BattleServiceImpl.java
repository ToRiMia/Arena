package torimia.arena.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;
import torimia.arena.dto.SuperheroDtoForBattle;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BattleServiceImpl implements BattleService {

    @Value("${battle-service.int.time-to-sleep}")
    private final int timeToSleep;

    @Override
    public BattleDtoResult battle(BattleDto dto) {

        List<SuperheroDtoForBattle> listOfFighters = chooseOrderOfFightersAttask(dto.getSuperheroes());

        Instant beginBattle = Instant.now();
        BattleDtoResult battleResult = battle(listOfFighters);
        Instant endBattle = Instant.now();

        Duration battleDuration = Duration.between(beginBattle, endBattle);
        battleResult.setBattleTime(battleDuration.getSeconds());

        Date dateOfBattle = Date.valueOf(LocalDate.now());
        battleResult.setDate(dateOfBattle);

        battleResult.setId(dto.getId());

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

    private BattleDtoResult battle(List<SuperheroDtoForBattle> fighters) {
        while (fighters.size() > 1) {
            fighters.forEach(attacker -> roundTryCatch(attacker, fighters));//ConcurrentModificationException
        }
        return getBattleResult(fighters.get(0));
    }

    private void roundTryCatch(SuperheroDtoForBattle attacker, List<SuperheroDtoForBattle> defenders) {
        try {
            round(attacker, defenders);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Battle not finished");
        }
    }

    private void round(SuperheroDtoForBattle attacker, List<SuperheroDtoForBattle> defenders) throws InterruptedException {
        for (SuperheroDtoForBattle defender : defenders) {
            if (attacker == defender) {
                continue;
            }
            attacker.attack(defender);
            Thread.sleep(timeToSleep);
            defenders.removeIf(fighter -> !fighter.isAlive());
        }
    }

    private BattleDtoResult getBattleResult(SuperheroDtoForBattle winner) {
        BattleDtoResult battleResult = new BattleDtoResult();
        battleResult.setWinnerId(winner.getId());
        battleResult.setAttackNumber(winner.getAttackCount());
        return battleResult;
    }
}
