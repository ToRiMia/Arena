package torimia.arena.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("battle_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleProgress {

    @Id
    private Long id;
    private Long battleId;
    private Long attackerId;
    private Long defenderId;
    private int damage;
    private int residualHealth;
}
