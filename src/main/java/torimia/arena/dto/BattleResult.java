package torimia.arena.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BattleResult {

    Long winnerId;

    Long loserId;

    Time battleTime;

    Integer attackNumber;

    Date date;

}
