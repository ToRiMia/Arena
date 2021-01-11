package torimia.arena.dto;

import lombok.*;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BattleDtoResult {

    private Long id;

    private Long winnerId;

    private Long loserId;

    private Long battleTime;

    private Integer attackNumber;

    private Date date;

}
