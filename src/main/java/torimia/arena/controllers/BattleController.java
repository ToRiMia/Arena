package torimia.arena.controllers;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import torimia.arena.dto.Battle;
import torimia.arena.dto.BattleResult;
import torimia.arena.dto.SuperheroDtoForBattle;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("battle")
public class BattleController {

    private final BattleResult battleResult = new BattleResult();
    private SuperheroDtoForBattle fighter1;
    private SuperheroDtoForBattle fighter2;

    @PostMapping
    public BattleResult create(@RequestBody Battle battle) throws InterruptedException {
        chooseFirstFighter(battle);

  //      DateTime beforeBattle = DateTime.now();
       Instant now = Instant.now();
        battle(fighter1, fighter2);
        Thread.sleep(5000);
       Duration between = Duration.between(now, Instant.now());
 //       DateTime afterBattle = DateTime.now();

        if (battleResult.getLoserId().equals(fighter1.getId())) {
            battleResult.setWinnerId(fighter2.getId());
        } else {
            battleResult.setWinnerId(fighter1.getId());
        }

//        Period diff = new Period(beforeBattle, afterBattle);

//        Seconds seconds =
//        LocalTime time= LocalTime.of(( between.getSeconds()/3600),between.getSeconds()/3600,);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss");
//        formatter.format(time);
//        String formattedDate = time.(formatter); ///////////////////////////////////////////////////доробити час, руками визначити часи, минути, секунди і засетити їх у локалтайм
 //       Time res = new Time(0, diff.getMinutes(), diff.getSeconds());

        return battleResult;
//        return BattleResult.builder()
//                .winnerId(winner.getId())
//                .loserId(loser.getId())
//                .attackNumber(attackNumber)
//                .battleTime(res)
//                .date(Date.valueOf(LocalDate.now()))
//                .build();
    }

    private void chooseFirstFighter(Battle battle) {
        int chooseFirst = (int) (Math.random() * (2 - 1) + 1);
        if (chooseFirst == 1) {
            fighter1 = battle.getSuperhero1();
            fighter2 = battle.getSuperhero2();
        } else {
            fighter2 = battle.getSuperhero1();
            fighter1 = battle.getSuperhero2();
        }
    }

    private void battle(SuperheroDtoForBattle fighter1, SuperheroDtoForBattle fighter2) throws InterruptedException {
        int attackNumber1 = 0;
        int attackNumber2 = 0;
        while (true) {
            if (isAlive(fighter1)) {
                attack(fighter1, fighter2);
                attackNumber1++;
                battleResult.setAttackNumber(attackNumber1);
            } else break;

            if (isAlive(fighter2)) {
                attack(fighter2, fighter1);
                attackNumber2++;
                battleResult.setAttackNumber(attackNumber2);
            } else break;

 //           Thread.sleep(100);
        }
    }

    private boolean isAlive(SuperheroDtoForBattle fighter) {
        if (fighter.getHealth() <= 0) {
            battleResult.setLoserId(fighter.getId());
            return false;
        }
        return true;
    }

    private void attack(SuperheroDtoForBattle superhero1, SuperheroDtoForBattle superhero2) {
        int superhero1Attack = (int) (Math.random() * (superhero1.getDamage() - 1) + 1);
        superhero2.setHealth(superhero2.getHealth() - superhero1Attack);
    }


}
