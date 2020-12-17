package torimia.arena.controllers;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import torimia.arena.dto.Battle;
import torimia.arena.dto.BattleResult;
import torimia.arena.dto.SuperheroDtoForBattle;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("battle")
public class BattleController {

    @PostMapping
    public BattleResult create(@RequestBody Battle battle) throws InterruptedException {
        SuperheroDtoForBattle superhero1 = battle.getSuperhero1();
        SuperheroDtoForBattle superhero2 = battle.getSuperhero2();
        int attackNumber = 0;
        SuperheroDtoForBattle winner;
        SuperheroDtoForBattle loser;
        DateTime beforeBattle = DateTime.now();

        while (superhero1.getHealth() > 0 || superhero2.getHealth() > 0) {
            int superhero1Attack = (int) (Math.random() * (superhero1.getDamage() - 1) + 1);
            superhero2.setHealth(superhero2.getHealth() - superhero1Attack);
            int superhero2Attack = (int) (Math.random() * (superhero2.getDamage() - 1) + 1);
            superhero1.setHealth(superhero1.getHealth() - superhero2Attack);
            attackNumber++;
  //        Thread.sleep(100);
        }
        Thread.sleep(5000);
        DateTime afterBattle = DateTime.now();
        if (superhero1.getHealth() < 0 || superhero1.getHealth() == 0) {
            winner = superhero2;
            loser = superhero1;
        } else {
            winner = superhero1;
            loser = superhero2;
        }

        Period diff = new Period(beforeBattle, afterBattle);

        Time res = new Time( 0, diff.getMinutes(), diff.getSeconds());
        return BattleResult.builder()
                .nameWinner(winner.getNickname())
                .nameLoser(loser.getNickname())
                .attackNumber(attackNumber)
                .battleTime(res)
                .date(Date.valueOf(LocalDate.now()))
                .build();
    }


}
