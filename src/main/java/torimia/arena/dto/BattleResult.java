package torimia.arena.dto;

import lombok.*;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BattleResult {

    Long winnerId;

    Long loserId;

    Long battleTime;

    Integer attackNumber;

    Date date;

}
