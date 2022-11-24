package com.invivoo.vivwallet.api.interfaces.actions;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.interfaces.users.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private com.invivoo.vivwallet.api.domain.action.ActionStatus status;
    private LocalDate paymentDate;
    private UserDto achiever;

    public static ActionDto createFromAction(Action action) {
        return ActionDto.builder()
                        .id(action.getId())
                        .userId(action.getAchiever().getId())
                        .type(action.getType().getName())
                        .status(action.getStatus())
                        .comment(action.getContext())
                        .creationDate(action.getDate())
                        .valueDate(action.getValueDate())
                        .achiever(UserDto.createFromUser(action.getAchiever()))
                        .payment(action.getVivAmount()).build();
    }
}
