package com.invivoo.vivwallet.api.interfaces.actions;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(ActionsController.API_V_1_ACTIONS)
public class ActionsController {

    static final String API_V_1_ACTIONS = "/api/v1/actions";

    private final ActionService actionService;

    public ActionsController(ActionService actionService) {
        this.actionService = actionService;
    }

    @RolesAllowed({"EXPERTISE_MANAGER", "SENIOR_MANAGER", "COMPANY_ADMINISTRATOR"})
    @GetMapping
    public ResponseEntity<List<Action>> getActionsOrderedByDateDesc() {
        List<Action> actions = actionService.getActionsOrderedByDateDesc();
        return ResponseEntity.ok(actions);
    }

    @PostMapping("/updateFromLynx")
    public ResponseEntity<List<Action>> updateFromLynx() {
        List<Action> actions = actionService.updateFromLynx();
        return ResponseEntity.ok(actions);
    }

}
