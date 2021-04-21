package torimia.arena.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import torimia.arena.ArenaRepository;
import torimia.arena.BattleMapper;
import torimia.arena.dto.BattleProgressDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleResultServiceImpl implements BattleResultService {

    private final ArenaRepository repository;
    private final BattleMapper mapper;

    @Override
    public Flux<BattleProgressDto> getBattleResult(Long id) {
        log.info("some");
        return repository.findByBattleId(id)
                .map(mapper::toDto);
    }
}
