package torimia.arena.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;
import torimia.arena.dto.SuperheroDtoForBattle;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleServiceTest {

    private BattleServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BattleServiceImpl(50);
    }

    @Test
    void battleWithThreeFighters() {
        BattleDto battleDto = new BattleDto();
        battleDto.setId(10L);
        SuperheroDtoForBattle superhero1 = createSuperhero(1L);
        SuperheroDtoForBattle superhero2 = createSuperhero(2L);
        SuperheroDtoForBattle superhero3 = createSuperhero(3L);
        List<SuperheroDtoForBattle> list = new ArrayList<>();
        list.add(superhero1);
        list.add(superhero2);
        list.add(superhero3);
        battleDto.setSuperheroes(list);

        BattleDtoResult battleResult = service.battle(battleDto);

//        assertThat(battleResult)
//                .returns(Date.valueOf(LocalDate.now()), BattleDtoResult::getDate);
        assertTrue(battleResult.getAttackNumber() > 0);
        assertTrue(battleResult.getWinnerId() > 0);
    }

    @Test
    void battleWithTwoFighters() {
        BattleDto battleDto = new BattleDto();
        battleDto.setId(10L);
        SuperheroDtoForBattle superhero1 = createSuperhero(1L);
        SuperheroDtoForBattle superhero2 = createSuperhero(2L);
        List<SuperheroDtoForBattle> list = new ArrayList<>();
        list.add(superhero1);
        list.add(superhero2);
        battleDto.setSuperheroes(list);

        BattleDtoResult battleResult = service.battle(battleDto);

//        assertThat(battleResult)
//                .returns(Date.valueOf(LocalDate.now()), BattleDtoResult::getDate);
        assertTrue(battleResult.getAttackNumber() > 0);
        assertTrue(battleResult.getWinnerId() > 0);
    }

    private SuperheroDtoForBattle createSuperhero(Long id) {
        return SuperheroDtoForBattle.builder()
                .id(id)
                .nickname("Superman")
                .attackCount(0)
                .damage(20)
                .health(100)
                .build();
    }
}