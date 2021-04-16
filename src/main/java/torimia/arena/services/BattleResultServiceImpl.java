package torimia.arena.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import torimia.arena.ArenaRepository;
import torimia.arena.BattleMapper;
import torimia.arena.dto.BattleProgressDto;

@Service
@RequiredArgsConstructor
public class BattleResultServiceImpl implements BattleResultService {

    private final ArenaRepository repository;
    private final BattleMapper mapper;

    @Override
    public Flux<BattleProgressDto> getBattleResult(Long id) {
        return repository.findByBattleId(id)
                .map(mapper::toDto);
    }
}
