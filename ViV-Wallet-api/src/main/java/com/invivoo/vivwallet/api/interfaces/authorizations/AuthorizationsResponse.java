package com.invivoo.vivwallet.api.interfaces.authorizations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationsResponse {

    private Long userId;
    private List<String> roles;
}
