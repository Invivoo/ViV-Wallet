package com.invivoo.vivwallet.api.application.provider.payments;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PaymentsProviderService {

    public static final int PAYMENT_SHEET_INDEX = 1;
    public static final int START_ROW = 1;

    private final UserService userService;
    private final ActionService actionService;
    private final PaymentService paymentService;

    @Autowired
    public PaymentsProviderService(UserService userService, ActionService actionService, PaymentService paymentService) {
        this.userService = userService;
        this.actionService = actionService;
        this.paymentService = paymentService;
    }

    public List<Payment> provide(XSSFWorkbook workbook) {
        List<Payment> payments = new ArrayList<>();
        for (Row row : workbook.getSheetAt(PAYMENT_SHEET_INDEX)) {
            if (row.getRowNum() < START_ROW) {
                continue;
            }
            Optional<User> userOpt = Optional.ofNullable(PaymentsProviderExcelCellResolver.FULL_NAME.resolve(row))
                                             .map(String.class::cast)
                                             .filter(StringUtils::isNotBlank)
                                             .flatMap(userService::findByFullName);
            Optional<User> companyAdministratorOpt = userService.findByRoleType(RoleType.COMPANY_ADMINISTRATOR);
            if (userOpt.isEmpty() || companyAdministratorOpt.isEmpty()) {
                continue;
            }
            User user = userOpt.get();
            User companyAdministrator = companyAdministratorOpt.get();
            int vivAmount = Optional.ofNullable(PaymentsProviderExcelCellResolver.VIV_AMOUNT.resolve(row))
                                    .map(Double.class::cast)
                                    .map(Double::intValue)
                                    .map(v -> -v)
                                    .orElse(0);
            LocalDate paymentDate = convertToLocalDate(PaymentsProviderExcelCellResolver.DATE.resolve(row));
            Payment payment = paymentService.findByDateAndReceiver(paymentDate, user)
                                            .map(Payment::toBuilder)
                                            .orElseGet(Payment::builder)
                                            .creator(companyAdministrator)
                                            .date(paymentDate)
                                            .receiver(user)
                                            .vivAmount(vivAmount)
                                            .build();
            payments.add(payment);
        }
        paymentService.saveAll(payments);
        payments.stream()
                .map(Payment::getReceiver)
                .distinct()
                .forEach(this::addActionsForPayment);
        return payments;
    }

    private void addActionsForPayment(User user) {
        int balance = user.getVivInitialBalance();
        List<Payment> payments = paymentService.findAllByReceiverOrderByDateAsc(user);
        List<Action> actions = actionService.findAllByAchieverAndPaymentIsNullOrderByDateAsc(user);
        for (Payment payment : payments) {
            balance = balance - payment.getVivAmount();
            if (balance >= 0) {
                continue;
            }
            List<Action> unpaidActions = actions.stream()
                                                .filter(a -> ActionType.NO_MAPPING_FOUND != a.getType())
                                                .filter(a -> a.getPayment() == null)
                                                .filter(a -> a.getDate().toLocalDate().isBefore(payment.getDate()))
                                                .filter(a -> a.getVivAmount() > 0)
                                                .collect(Collectors.toList());
            for (Action unpaidAction : unpaidActions) {
                balance += unpaidAction.getVivAmount();
                unpaidAction.setPayment(payment);
                if (balance >= 0) {
                    break;
                }
            }
        }
        actionService.saveAll(actions);
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate();
    }


}
