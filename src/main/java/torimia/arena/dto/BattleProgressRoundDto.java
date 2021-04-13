package torimia.arena.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleProgressRoundDto {
    private SuperheroDtoForBattle attacker;
    private List<SuperheroDtoForBattle> defenders;
}
