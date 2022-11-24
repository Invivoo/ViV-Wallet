package com.invivoo.vivwallet.api.interfaces.users;


import com.invivoo.vivwallet.api.domain.action.ActionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionUpdateRequest {

    private String id;
    private ActionStatus status;
}
