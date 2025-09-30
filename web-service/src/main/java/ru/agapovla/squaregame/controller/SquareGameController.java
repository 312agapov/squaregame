package ru.agapovla.squaregame.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.agapovla.squaregame.dto.BoardDto;
import ru.agapovla.squaregame.dto.MoveResultDto;
import ru.agapovla.squaregame.dto.SimpleMoveDto;
import ru.agapovla.squaregame.service.SquareGameService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SquareGameController {

    private final SquareGameService gameService;

    @PostMapping("/{rules}/nextMove")
    public SimpleMoveDto nextMove(@PathVariable String rules, @RequestBody BoardDto boardDto) {
        return gameService.nextMove(boardDto);
    }

    @PostMapping("/game")
    public String startGame(@RequestParam int size,
                            @RequestParam String player1,
                            @RequestParam String player2) {
        return gameService.startGame(size, player1, player2);
    }

    @PostMapping("/move")
    public MoveResultDto makeMove(@RequestParam int x, @RequestParam int y) {
        return gameService.move(x, y);
    }

    @GetMapping("/help")
    public String help() {
        return gameService.help();
    }

    @PostMapping("/exit")
    public void exit() {
        gameService.exit();
    }
}
