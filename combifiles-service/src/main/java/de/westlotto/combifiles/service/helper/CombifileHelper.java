package de.westlotto.combifiles.service.helper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CombifileHelper {

    private static final int STAKE_PER_TICKET = 1000;

    public Long calculateStake(MultipartFile file) throws IOException {
        AtomicLong result = new AtomicLong(0);
        new String(file.getBytes()).lines().forEach(line -> {
            String[] splitted = line.split("=");
            if (splitted.length == 2 && NumberUtils.isDigits(splitted[1])) {
                result.addAndGet(NumberUtils.toLong(splitted[1]));
            } else {
                throw new RuntimeException(String.format("Invalid combination file content: %s", line));
            }
        });

        return result.get() * STAKE_PER_TICKET;
    }

}
