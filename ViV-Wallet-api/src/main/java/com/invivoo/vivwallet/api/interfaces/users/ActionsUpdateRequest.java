package com.invivoo.vivwallet.api.interfaces.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionsUpdateRequest {

    List<ActionUpdateRequest> updates;
}
