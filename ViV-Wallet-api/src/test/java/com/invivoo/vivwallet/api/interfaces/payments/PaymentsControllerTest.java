package com.invivoo.vivwallet.api.interfaces.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentsController.class)
public class PaymentsControllerTest {

    private static final Payment TEST_PAYMENT_1 = Payment.builder().id(1L).build();
    private static final Payment TEST_PAYMENT_2 = Payment.builder().id(2L).build();
    private static final List<Payment> TEST_PAYMENTS = Arrays.asList(TEST_PAYMENT_1, TEST_PAYMENT_2);
    private static final Action ACTION_1 = Action.builder().id(1L).build();
    private static final Action ACTION_2 = Action.builder().id(2L).build();
    private static final List<Action> TEST_ACTIONS = Arrays.asList(ACTION_1, ACTION_2);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentsController paymentsController;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void getAllPayments() throws Exception {
        // given
        when(paymentService.getAll())
                .thenReturn(TEST_PAYMENTS);
        String jsonPayments = mapper.writeValueAsString(TEST_PAYMENTS);

        // when
        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/payments"))
                .andDo(MockMvcResultHandlers.print());
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(jsonPayments));
    }

    @Test
    public void getActionsByPaymentId() throws Exception {
        // given
        when(paymentService.getActionsByPaymentId(anyLong()))
                .thenReturn(TEST_ACTIONS);
        String jsonActions = mapper.writeValueAsString(TEST_ACTIONS);

        // when
        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/v1/payments/%s/actions", TEST_PAYMENT_1.getId())))
                .andDo(MockMvcResultHandlers.print());
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(jsonActions));
    }
}