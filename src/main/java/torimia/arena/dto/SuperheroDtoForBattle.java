package torimia.arena.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public boolean isAlive() {
        return health > 0;
    }

    public SuperheroDtoForBattle attack(SuperheroDtoForBattle secondFighter) {
        if (secondFighter.getId().equals(id))// хз чи правильно, не можу ударити себе
            return secondFighter;
        attackCount++;
        int attack = (int) (Math.random() * (this.getDamage()) + 1);
        secondFighter.setHealth(secondFighter.getHealth() - attack);
        return secondFighter;
    }

}
