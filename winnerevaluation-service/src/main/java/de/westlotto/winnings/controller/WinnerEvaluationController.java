package de.westlotto.winnings.controller;

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

import de.westlotto.winnings.model.WinnerEvaluationResult;
import de.westlotto.winnings.service.WinnerEvaluationService;

@RestController
@RequestMapping("/draws")
public class WinnerEvaluationController {

    @Autowired
    private WinnerEvaluationService winnerEvaluationService;

    @GetMapping("/{drawId}/winners")
    public List<WinnerEvaluationResult> findAll(@PathVariable("drawId") Long drawId) {
        try {
            return winnerEvaluationService.findAll(drawId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Request could not be processed: %s", e.getMessage()));
        }
    }

    @GetMapping("/{drawId}/companies/{companyId}/winners")
    public WinnerEvaluationResult findCompanyWinnerEvaluation(@PathVariable("drawId") Long drawId,
            @PathVariable("companyId") Long companyId) {
        try {
            return winnerEvaluationService.findWinnerEvaluation(drawId, companyId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Request could not be processed: %s", e.getMessage()));
        }
    }

    @PostMapping("/{drawId}/companies/{companyId}/winners/actions")
    public WinnerEvaluationResult companyAction(@PathVariable Long drawId,
            @PathVariable("companyId") Long companyId,
            @RequestBody WinnerEvaluationAction action) {
        switch (action.getType()) {
            case CONFIRM_WINNER_EVALUATION:
                return winnerEvaluationService.confirmWinnerEvaluation(drawId, companyId);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Unknown action transmitted"));
        }
    }

    @PostMapping("/{drawId}/winners/actions")
    public List<WinnerEvaluationResult> action(@PathVariable Long drawId, @RequestBody WinnerEvaluationAction action) {
        switch (action.getType()) {
            case RUN_WINNER_EVALUATION:
                return winnerEvaluationService.runWinnerEvaluation(drawId);
            case CONFIRM_WINNER_EVALUATION:
                return winnerEvaluationService.approveWinnerEvaluation(drawId);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Unknown action transmitted"));
        }
    }
}
