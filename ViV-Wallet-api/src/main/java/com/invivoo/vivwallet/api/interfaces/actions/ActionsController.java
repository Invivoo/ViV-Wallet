package com.invivoo.vivwallet.api.interfaces.actions;

import com.invivoo.vivwallet.api.domain.action.ActionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ActionsController.API_V_1_ACTIONS)
public class ActionsController {

    static final String API_V_1_ACTIONS = "/api/v1/actions";

    private final ActionService actionService;

    public ActionsController(ActionService actionService) {
        this.actionService = actionService;
    }

    @PreAuthorize("hasAnyAuthority('EXPERTISE_MANAGER','SENIOR_MANAGER', 'COMPANY_ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<ActionDto>> getActionsOrderedByDateDesc() {
        List<ActionDto> actions = actionService.getActionsOrderedByDateDesc()
                                               .stream()
                                               .map(ActionDto::createFromAction)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(actions);
    }

    @PostMapping("/updateFromLynx")
    public ResponseEntity<List<ActionDto>> updateFromLynx() {
        List<ActionDto> actions = actionService.updateFromLynx()
                                               .stream()
                                               .map(ActionDto::createFromAction)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(actions);
    }

}
