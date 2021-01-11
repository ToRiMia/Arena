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

        BattleDtoResult battleResult = battle(listOfFighters, new BattleDtoResult());
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

    private BattleDtoResult battle(List<SuperheroDtoForBattle> fighters, BattleDtoResult battleResult) {
        try {
            while (fighters.size() > 1) {
                for (int i = 0; i < fighters.size(); i++) {
                    for (int j = 0; j < fighters.size(); j++) {
                        if (i == j) {
                            continue;
                        }
                        fighters.get(i).attack(fighters.get(j));
                        Thread.sleep(timeToSleep);
                    }
                }

                fighters.removeIf(fighter -> !fighter.isAlive());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Battle not finished");
        }

        getBattleResult(fighters.get(0),battleResult);

        return battleResult;
    }

    private void getBattleResult(SuperheroDtoForBattle winner, BattleDtoResult battleResult) {
        battleResult.setWinnerId(winner.getId());
        battleResult.setAttackNumber(winner.getAttackCount());
    }
}
