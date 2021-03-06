package com.invivoo.vivwallet.api.interfaces.actions;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.interfaces.users.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionDto {

    private Long id;
    private Long userId;
    private String type;
    private String comment;
    private LocalDateTime creationDate;
    private LocalDateTime valueDate;
    private Integer payment;
    private String status;
    private LocalDate paymentDate;
    private UserDto achiever;

    public static ActionDto createFromAction(Action action) {
        ActionDto actionDto = ActionDto.builder()
                                       .id(action.getId())
                                       .userId(action.getAchiever().getId())
                                       .type(action.getType().getName())
                                       .comment(action.getContext())
                                       .creationDate(action.getDate())
                                       .valueDate(action.getValueDate())
                                       .achiever(UserDto.createFromUser(action.getAchiever()))
                                       .payment(action.getVivAmount()).build();
        Optional.ofNullable(action.getPayment()).ifPresentOrElse(
                payment -> {
                    actionDto.setStatus(ActionStatus.PAID.getLabel());
                    actionDto.setPaymentDate(payment.getDate());
                },
                () -> actionDto.setStatus(ActionStatus.UNPAID.getLabel())
        );

        return actionDto;
    }
}
