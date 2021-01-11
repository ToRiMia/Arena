package torimia.arena.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.BattleDtoResult;
import torimia.arena.dto.SuperheroDtoForBattle;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleServiceTest {

    private BattleServiceImpl service;

    private static final Long SUPERHERO_ID = 1L;

    @BeforeEach
    void setUp() {
        service = new BattleServiceImpl(90);

    }

    @Test
    void battle() {
        SuperheroDtoForBattle superhero1 = createSuperhero();
        SuperheroDtoForBattle superhero2 = createSuperhero();

        BattleDto battleDto = new BattleDto();
        battleDto.setSuperhero1(superhero1);
        battleDto.setSuperhero2(superhero2);

        BattleDtoResult battleResult = service.battle(battleDto);

        assertThat(battleResult)
                .returns(Date.valueOf(LocalDate.now()), BattleDtoResult::getDate)
                .returns(SUPERHERO_ID, BattleDtoResult::getLoserId)
                .returns(SUPERHERO_ID, BattleDtoResult::getWinnerId);
        assertTrue(battleResult.getBattleTime() > 0);
        assertTrue(battleResult.getAttackNumber() > 0);
    }

    private SuperheroDtoForBattle createSuperhero() {
        return SuperheroDtoForBattle.builder()
                .id(SUPERHERO_ID)
                .nickname("Superman")
                .attackCount(0)
                .damage(9)
                .health(100)
                .build();
    }
}