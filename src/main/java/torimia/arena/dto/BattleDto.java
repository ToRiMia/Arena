package torimia.arena.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class BattleDto {

    @NotNull(message = "First fighter must be not null")
    SuperheroDtoForBattle superhero1;

    @NotNull(message = "First fighter must be not null")
    SuperheroDtoForBattle superhero2;
}
