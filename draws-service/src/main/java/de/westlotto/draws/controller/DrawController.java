package de.westlotto.draws.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.westlotto.draws.model.Draw;

import de.westlotto.draws.service.DrawService;

@RestController
@RequestMapping("/draws")
public class DrawController {
    @Autowired
    private DrawService drawService;

    @GetMapping("/{id}")
    public Optional<Draw> one(@PathVariable("id") Long id) {
        return drawService.findById(id);
    }

    @GetMapping
    public Iterable<Draw> all() {
        return drawService.findAll();
    }

    @PostMapping
    public Draw create(@RequestBody Draw draw) {
        return drawService.createDraw(draw);
    }

    @PostMapping("/{drawId}/actions")
    public Draw companyReady(@PathVariable Long drawId, @RequestBody DrawAction action) {
        switch (action.getType()) {
            case CHANGE_STATE:
                return this.drawService.changeDrawState(drawId, action.getState());
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Unknown action transmitted"));
        }
    }

    @PostMapping("/{drawId}/companies/{companyId}/actions")
    public Draw action(@PathVariable Long drawId, @PathVariable Long companyId,
            @RequestBody DrawCompanyAction action) {
        switch (action.getType()) {
            case SUBMIT_READINESS:
                if (action.isReadiness()) {
                    return drawService.companyReady(drawId, companyId);
                }
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Unknown action transmitted"));
        }
        return null;
    }

}
