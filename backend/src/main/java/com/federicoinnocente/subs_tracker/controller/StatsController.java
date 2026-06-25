package com.federicoinnocente.subs_tracker.controller;

import com.federicoinnocente.subs_tracker.dto.SummaryDTO;
import com.federicoinnocente.subs_tracker.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final StatService statsService;

    @GetMapping("/summary")
    public SummaryDTO getSummary() {
        return statsService.getSummary();
    }
}
