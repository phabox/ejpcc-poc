package de.westlotto.logging.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.westlotto.logging.model.LogMessage;
import de.westlotto.logging.repository.LogRepository;

@RestController
@RequestMapping("/logs")
public class LogController  {
    
    @Autowired
    private LogRepository logRepository;

    @GetMapping
    public Iterable<LogMessage> getLogs(){
        return logRepository.findAll();
    }

}
