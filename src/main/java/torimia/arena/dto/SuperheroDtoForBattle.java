package torimia.arena.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuperheroDtoForBattle {

    private Long id;

    private String nickname;

    private Integer damage;

    private Integer health;

    private Integer attackCount = 0;

    public boolean isAlive() {
        return health > 0;
    }

    public SuperheroDtoForBattle attack(SuperheroDtoForBattle secondFighter) {
        attackCount++;
        int attack = (int) (Math.random() * (this.getDamage()) + 1);
        secondFighter.setHealth(secondFighter.getHealth() - attack);
        return secondFighter;
    }

}
