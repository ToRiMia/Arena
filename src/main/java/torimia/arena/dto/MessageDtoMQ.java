package torimia.arena.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDtoMQ {
   private Long id;

   private FightStatus message;
}
