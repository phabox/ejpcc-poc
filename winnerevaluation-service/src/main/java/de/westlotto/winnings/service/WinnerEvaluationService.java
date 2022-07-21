package de.westlotto.winnings.service;

import java.util.List;

import de.westlotto.winnings.model.WinnerEvaluationResult;

public interface WinnerEvaluationService {

    public List<WinnerEvaluationResult> findAll(Long drawId);

    public WinnerEvaluationResult findWinnerEvaluation(Long drawId, Long companyId);

    public List<WinnerEvaluationResult> runWinnerEvaluation(Long drawId);

    public WinnerEvaluationResult confirmWinnerEvaluation(Long drawId, Long companyId);

    public List<WinnerEvaluationResult> approveWinnerEvaluation(Long drawId);

}
