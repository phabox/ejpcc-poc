package de.westlotto.winningnumbers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.westlotto.winningnumbers.model.WinningNumber;
import de.westlotto.winningnumbers.model.WinningNumbers;
import de.westlotto.winningnumbers.service.WinningNumbersService;

@RestController
@RequestMapping("/draws")
public class WinningNumberController {

    @Autowired
    private WinningNumbersService winningNumbersService;

    @GetMapping("/{drawId}/winningnumbers")
    public WinningNumbers findWinningNumbers(@PathVariable("drawId") Long drawId) {
        try {
            return winningNumbersService.findWinningNumbers(drawId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Request could not be processed: %s", e.getMessage()));
        }
    }

    @PostMapping("/{drawId}/winningnumbers")
    public WinningNumbers saveWinningNumbers(@PathVariable("drawId") Long drawId,
            @RequestBody List<WinningNumber> winningNumbers) {
        try {
            return winningNumbersService.saveWinningNumbers(drawId, winningNumbers);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("WinningNumbers could not be processed: %s", e.getMessage()));
        }
    }

    @PostMapping("/{drawId}/winningnumbers/actions")
    public WinningNumbers action(@PathVariable Long drawId, @RequestBody WinningNumbersAction action) {
        switch (action.getType()) {
            case READY_TO_RECEIVE:
                winningNumbersService.readyToReceive(drawId);
                return new WinningNumbers();
            case APPROVE_WINNINGNUMBERS:
                return winningNumbersService.approveWinningNumbers(drawId);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Unknown action transmitted"));
        }
    }

}
