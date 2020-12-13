package com.invivoo.vivwallet.api.interfaces.admin;

import com.invivoo.vivwallet.api.application.provider.payments.PaymentsProviderService;
import com.invivoo.vivwallet.api.application.provider.users.UsersProviderService;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import com.invivoo.vivwallet.api.interfaces.actions.ActionDto;
import com.invivoo.vivwallet.api.interfaces.users.UserDto;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


// /!\ Must be deleted after UAT /!\
@RestController
@RequestMapping(AdminController.API_V_1_ADMIN)
@Deprecated
public class AdminController {

    protected static final String API_V_1_ADMIN = "/api/v1/admin";

    private UsersProviderService usersProviderService;
    private PaymentsProviderService paymentsProviderService;
    private ActionService actionService;

    public AdminController(UsersProviderService usersProviderService, PaymentsProviderService paymentsProviderService, ActionService actionService) {
        this.usersProviderService = usersProviderService;
        this.paymentsProviderService = paymentsProviderService;
        this.actionService = actionService;
    }

    @PostMapping("/updateUsersFromExcel")
    public ResponseEntity<List<UserDto>> updateUsersFromExcel(@RequestParam MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        List<User> importedUsers = usersProviderService.provide(workbook);
        paymentsProviderService.provide(workbook);
        return ResponseEntity.ok(importedUsers.stream()
                                              .map(UserDto::createFromUser)
                                              .collect(Collectors.toList()));
    }

    @PostMapping("/updateActionsFromLynxActivities")
    public ResponseEntity<List<ActionDto>> updateActionsFromLynxActivities(@RequestBody Activities activities) throws IOException {
        List<ActionDto> actions = actionService.updateFromLynxActivities(activities)
                                               .stream()
                                               .map(ActionDto::createFromAction)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(actions);
    }

}
