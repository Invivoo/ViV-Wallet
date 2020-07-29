package com.invivoo.vivwallet.api.domain.user;

import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private ActionRepository actionRepository;

    public UserService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public long computeBalance(long userId) {
        return actionRepository.findAllByPaymentIsNull().stream()
                .filter(action -> action.getAchiever().getId().equals(userId))
                .mapToLong(action -> action.getViv().longValue())
                .sum();
    }
}
