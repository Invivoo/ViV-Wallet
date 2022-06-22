package com.invivoo.vivwallet.api.interfaces.authorizations;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizationsController.class)
public class AuthorizationsControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JWTTokenProvider jwtTokenProvider;
    @MockBean
    private UserService userService;

    @Test
    public void shouldGetAuthorizations_whenAuthorizeWithX4BId() throws Exception {
        //given
        Mockito.when(jwtTokenProvider.resolveToken(Mockito.any(HttpServletRequest.class))).thenCallRealMethod();

        DecodedJWT decodedJWT = Mockito.mock(DecodedJWT.class);
        Claim vivWalletClaims = Mockito.mock(Claim.class);
        Mockito.when(vivWalletClaims.asString()).thenReturn("{\"userId\":314,\"roles\":[\"EXPERTISE_MANAGER\"]}");
        Mockito.when(decodedJWT.getClaim("viv-wallet")).thenReturn(vivWalletClaims);
        Claim userClaim = Mockito.mock(Claim.class);
        Mockito.when(userClaim.asString()).thenReturn("John DOE");
        Mockito.when(decodedJWT.getClaim("user")).thenReturn(userClaim);

        Mockito.when(jwtTokenProvider.verify("jwt")).thenReturn(decodedJWT);
        Mockito.when(userService.findByX4bIdOrByFullName("John DOE"))
               .thenReturn(Optional.of(User.builder().build()));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/Authorizations")
                                                                            .header("Authorization",
                                                                                    "Bearer jwt"))
                                             .andDo(MockMvcResultHandlers.print());

        //then
        String expectedAuthorizations = mapper.writeValueAsString(AuthorizationsResponse.builder().roles(List.of()).build());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(expectedAuthorizations));
    }

    @Test
    public void shouldGetAuthorizations_whenAuthorizeWithEmail() throws Exception {
        //given
        Mockito.when(jwtTokenProvider.resolveToken(Mockito.any(HttpServletRequest.class))).thenCallRealMethod();

        DecodedJWT decodedJWT = Mockito.mock(DecodedJWT.class);
        Claim vivWalletClaims = Mockito.mock(Claim.class);
        Mockito.when(vivWalletClaims.asString()).thenReturn("{\"userId\":314,\"roles\":[\"EXPERTISE_MANAGER\"]}");
        Mockito.when(decodedJWT.getClaim("viv-wallet")).thenReturn(vivWalletClaims);
        Claim emailClaim = Mockito.mock(Claim.class);
        Mockito.when(emailClaim.asString()).thenReturn("john.doe@invivoo.com");
        Mockito.when(decodedJWT.getClaim(AuthorizationsController.HTTP_SCHEMAS_XMLSOAP_ORG_WS_2005_05_IDENTITY_CLAIMS_EMAILADDRESS))
               .thenReturn(emailClaim);

        Mockito.when(jwtTokenProvider.verify("jwt")).thenReturn(decodedJWT);
        Mockito.when(userService.findByEmail("john.doe@invivoo.com"))
               .thenReturn(Optional.of(User.builder().build()));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/Authorizations")
                                                                            .header("Authorization",
                                                                                    "Bearer jwt"))
                                             .andDo(MockMvcResultHandlers.print());

        //then
        String expectedAuthorizations = mapper.writeValueAsString(AuthorizationsResponse.builder().roles(List.of()).build());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(expectedAuthorizations));
    }
}
