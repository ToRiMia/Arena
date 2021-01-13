package torimia.arena.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BattleRound {

    private SuperheroDtoForBattle attacker;

    private SuperheroDtoForBattle defender;
}
