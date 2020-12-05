package com.invivoo.vivwallet.api.interfaces.expertises;

import com.invivoo.vivwallet.api.application.security.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ExpertisesController.API_V1_EXPERTISES)
public class ExpertisesController {

    static final String API_V1_EXPERTISES = "/api/v1/expertises";

    private final SecurityService securityService;

    public ExpertisesController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping
    public ResponseEntity<List<ExpertiseDto>> getAllExpertises() {
        return ResponseEntity.ok(securityService.getAllowedExpertiseForConnectedUser()
                                                .stream()
                                                .map(ExpertiseDto::fromExpertise)
                                                .sorted(Comparator.comparing(ExpertiseDto::getExpertiseName))
                                                .collect(Collectors.toList()));
    }
}

