package torimia.arena.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusDto {
   private Long id;

   private FightStatus message;
}
