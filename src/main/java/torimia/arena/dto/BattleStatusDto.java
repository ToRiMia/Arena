package torimia.arena.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleStatusDto {
   private Long id;

   private BattleStatus message;
}
