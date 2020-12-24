package torimia.arena.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import torimia.arena.dto.BattleDto;
import torimia.arena.dto.MessageDto;
import torimia.arena.handlers.BattleHandler;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("battle")
public class BattleController {

    private final BattleHandler handler;

    @PostMapping
    public ResponseEntity<MessageDto> create(@Valid @RequestBody BattleDto battle){
        handler.sendResponseToSuperheroController(battle);
        return ResponseEntity.ok(new MessageDto("Fight started"));
    }
}
