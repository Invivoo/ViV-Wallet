package com.invivoo.vivwallet.api.interfaces.admin;

import com.invivoo.vivwallet.api.application.provider.payments.PaymentsProviderService;
import com.invivoo.vivwallet.api.application.provider.users.UsersProviderService;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import com.invivoo.vivwallet.api.interfaces.actions.ActionDto;
import com.invivoo.vivwallet.api.interfaces.users.UserDto;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


// /!\ Must be deleted after UAT /!\
@RestController
@RequestMapping(AdminController.API_V_1_ADMIN)
@PreAuthorize("hasAnyAuthority('API_USER')")
@Deprecated
public class AdminController {

    protected static final String API_V_1_ADMIN = "/api/v1/admin";
    public static final int ACTION_ID_COLUMN_INDEX = 0;
    public static final int ACTION_DATE_COLUMN_INDEX = 1;
    public static final int ACTION_TYPE_COLUMN_INDEX = 2;
    public static final int ACTION_LYNX_ACTIVITY_ID_COLUMN_INDEX = 3;
    public static final int ACTION_VIV_AMOUNT_COLUMN_INDEX = 4;
    public static final int ACTION_CONTEXT_COLUMN_INDEX = 5;
    public static final int ACTION_ACHIEVER_COLUMN_INDEX = 7;

    private UsersProviderService usersProviderService;
    private PaymentsProviderService paymentsProviderService;
    private UserService userService;
    private PaymentService paymentService;
    private ActionService actionService;

    public AdminController(UsersProviderService usersProviderService, PaymentsProviderService paymentsProviderService, UserService userService, PaymentService paymentService, ActionService actionService) {
        this.usersProviderService = usersProviderService;
        this.paymentsProviderService = paymentsProviderService;
        this.userService = userService;
        this.paymentService = paymentService;
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

    @PostMapping("/generateMasterExcel")
    public ResponseEntity<InputStreamResource> generateMasterExcel(HttpServletResponse response){
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet actionsSheet = workbook.createSheet("actions");
            addHeaderRow(actionsSheet);
            List<Action> actions = actionService.findAllByOrderByDateDesc();
            IntStream.rangeClosed(1, actions.size())
                     .forEach(i -> addActionRow(actionsSheet.createRow(i), actions.get(i - 1)));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cleanData")
    public ResponseEntity<Boolean> cleanData(){
        actionService.deleteAll();
        paymentService.deleteAll();
        userService.deleteAll();
        return ResponseEntity.ok(true);
    }

    private void addHeaderRow(XSSFSheet actionsSheet) {
        XSSFRow headerRow = actionsSheet.createRow(0);
        headerRow.createCell(ACTION_ID_COLUMN_INDEX).setCellValue("id");
        headerRow.createCell(ACTION_DATE_COLUMN_INDEX).setCellValue("date");
        headerRow.createCell(ACTION_TYPE_COLUMN_INDEX).setCellValue("type");
        headerRow.createCell(ACTION_LYNX_ACTIVITY_ID_COLUMN_INDEX).setCellValue("lynxActivityId");
        headerRow.createCell(ACTION_VIV_AMOUNT_COLUMN_INDEX).setCellValue("vivAmount");
        headerRow.createCell(ACTION_CONTEXT_COLUMN_INDEX).setCellValue("context");
        headerRow.createCell(ACTION_ACHIEVER_COLUMN_INDEX).setCellValue("fullName");
    }

    private void addActionRow(XSSFRow row, Action action) {
        row.createCell(ACTION_ID_COLUMN_INDEX).setCellValue(action.getId());
        row.createCell(ACTION_DATE_COLUMN_INDEX).setCellValue(action.getDate());
        row.createCell(ACTION_TYPE_COLUMN_INDEX).setCellValue(action.getType().getName());
        row.createCell(ACTION_LYNX_ACTIVITY_ID_COLUMN_INDEX).setCellValue(action.getLynxActivityId());
        row.createCell(ACTION_VIV_AMOUNT_COLUMN_INDEX).setCellValue(action.getVivAmount());
        row.createCell(ACTION_CONTEXT_COLUMN_INDEX).setCellValue(action.getContext());
        row.createCell(ACTION_ACHIEVER_COLUMN_INDEX).setCellValue(action.getAchiever().getFullName());
    }

}
