package com.invivoo.vivwallet.api.interfaces.actions;

import com.invivoo.vivwallet.api.application.security.SecurityService;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.interfaces.expertises.ExpertiseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ActionsController.API_V_1_ACTIONS)
public class ActionsController {

    static final String API_V_1_ACTIONS = "/api/v1/actions";

    private final ActionService actionService;
    private final SecurityService securityService;

    public ActionsController(ActionService actionService, SecurityService securityService) {
        this.actionService = actionService;
        this.securityService = securityService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('EXPERTISE_MANAGER','SENIOR_MANAGER', 'COMPANY_ADMINISTRATOR')")
    public ResponseEntity<List<ActionDto>> getActionsOrderedByDateDesc() {
        boolean isAdmin = securityService.connectedUserHasAnyRole(RoleType.SENIOR_MANAGER, RoleType.COMPANY_ADMINISTRATOR);
        List<String> allowedExpertiseIdsForConnectedUser = securityService.getAllowedExpertiseForConnectedUser()
                                                                          .stream()
                                                                          .map(Expertise::name)
                                                                          .collect(Collectors.toList());
        List<ActionDto> actions = actionService.findAllByOrderByDateDesc()
                                               .stream()
                                               .map(ActionDto::createFromAction)
                                               .filter(actionDto -> isAdmin || Optional.ofNullable(actionDto.getAchiever().getExpertise())
                                                                                       .map(ExpertiseDto::getId)
                                                                                       .map(allowedExpertiseIdsForConnectedUser::contains)
                                                                                       .orElse(false))
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(actions);
    }

    @PostMapping("/updateFromLynx")
    @PreAuthorize("hasAnyAuthority('API_USER')")
    public ResponseEntity<List<ActionDto>> updateFromLynx() {
        List<ActionDto> actions = actionService.updateFromLynx()
                                               .stream()
                                               .map(ActionDto::createFromAction)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(actions);
    }

}
