package de.westlotto.winningnumbers.service;

import java.util.List;

import de.westlotto.winningnumbers.model.WinningNumber;
import de.westlotto.winningnumbers.model.WinningNumbers;

public interface WinningNumbersService {
    public void readyToReceive(Long drawId);

    WinningNumbers findWinningNumbers(Long drawId);

    WinningNumbers saveWinningNumbers(Long drawId, List<WinningNumber> winningNumbers);

    WinningNumbers approveWinningNumbers(Long drawId);
}
