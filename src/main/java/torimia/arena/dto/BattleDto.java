package torimia.arena.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BattleDto {

    @NotNull
    Long id;

    @NotNull(message = "Fighters must be not null")
    List<SuperheroDtoForBattle> superheroes;
}
