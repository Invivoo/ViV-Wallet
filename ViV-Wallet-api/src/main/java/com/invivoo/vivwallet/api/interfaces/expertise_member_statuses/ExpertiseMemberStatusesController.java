package com.invivoo.vivwallet.api.interfaces.expertise_member_statuses;

import com.invivoo.vivwallet.api.domain.expertise.UserExpertiseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(ExpertiseMemberStatusesController.API_V1_EXPERTISES)
public class ExpertiseMemberStatusesController {

    static final String API_V1_EXPERTISES = "/api/v1/expertiseMemberStatuses";

    @GetMapping
    public ResponseEntity<List<UserExpertiseStatus>> getAllExpertiseMemberStatuses() {
        List<UserExpertiseStatus> userExpertiseStatuses = Stream.of(UserExpertiseStatus.values()).collect(Collectors.toList());
        return ResponseEntity.ok(userExpertiseStatuses);
    }
}

