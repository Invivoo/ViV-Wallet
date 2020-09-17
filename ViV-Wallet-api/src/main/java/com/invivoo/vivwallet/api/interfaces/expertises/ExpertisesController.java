package com.invivoo.vivwallet.api.interfaces.expertises;

import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(ExpertisesController.API_V1_EXPERTISES)
public class ExpertisesController {

    static final String API_V1_EXPERTISES = "/api/v1/expertises";

    @GetMapping
    public ResponseEntity<List<Expertise>> getAllExpertises() {
        List<Expertise> expertises = Stream.of(Expertise.values()).collect(Collectors.toList());
        return ResponseEntity.ok(expertises);
    }
}

