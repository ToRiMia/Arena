package torimia.arena.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleResult;
import torimia.arena.dto.SuperheroDtoForBattle;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

@Slf4j
@AllArgsConstructor
@Service
public class BattleServiceImpl implements BattleService {

    @Value("${battle-service.int.time-to-sleep}")
    private final int timeToSleep;

    @Override
    public BattleResult battle(BattleDto dto) {
        SuperheroDtoForBattle fighter1;
        SuperheroDtoForBattle fighter2;

        fighter1 = chooseFirstFighter(dto);
        fighter2 = fighter1.equals(dto.getSuperhero1()) ? dto.getSuperhero2() : dto.getSuperhero1();

        Instant beginFight = Instant.now();

        BattleResult battleResult = battle(fighter1, fighter2, new BattleResult());
        Instant endFight = Instant.now();

        Duration fightDuration = Duration.between(beginFight, endFight);
        battleResult.setBattleTime(fightDuration.getSeconds());

        Date dateOfBattle = Date.valueOf(LocalDate.now());
        battleResult.setDate(dateOfBattle);

        battleResult.setId(dto.getId());

        //      throw new IndexOutOfBoundsException();
        return battleResult;
    }

    private SuperheroDtoForBattle chooseFirstFighter(BattleDto battle) {
        int chooseFirst = (int) (Math.random() * (2) + 1);
        return chooseFirst == 1 ? battle.getSuperhero1() : battle.getSuperhero2();
    }

    private BattleResult battle(SuperheroDtoForBattle fighter1, SuperheroDtoForBattle fighter2, BattleResult battleResult) {
        try {
            while (battle(fighter1, fighter2) && battle(fighter2, fighter1)) {
                Thread.sleep(timeToSleep);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Battle not finished");
        }

        if (fighter1.isAlive()) {
            getBattleResult(fighter1, fighter2, battleResult);
        } else
            getBattleResult(fighter2, fighter1, battleResult);

        return battleResult;
    }

    private boolean battle(SuperheroDtoForBattle attacker, SuperheroDtoForBattle defender) {
        attacker.attack(defender);
        return defender.isAlive();
    }

    private void getBattleResult(SuperheroDtoForBattle winner, SuperheroDtoForBattle loser, BattleResult battleResult) {
        battleResult.setWinnerId(winner.getId());
        battleResult.setLoserId(loser.getId());
        battleResult.setAttackNumber(winner.getAttackCount());
    }
}
