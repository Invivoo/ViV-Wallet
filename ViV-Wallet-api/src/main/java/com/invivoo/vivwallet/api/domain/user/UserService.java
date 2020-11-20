package com.invivoo.vivwallet.api.domain.user;

import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActionRepository actionRepository;

    public UserService(UserRepository userRepository, ActionRepository actionRepository) {
        this.userRepository = userRepository;
        this.actionRepository = actionRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByExpertise(Expertise expertise) {
        return userRepository.findByExpertisesExpertise(expertise);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public long computeBalance(long userId) {
        return actionRepository.findAllByPaymentIsNull().stream()
                               .filter(action -> action.getAchiever().getId().equals(userId))
                               .mapToLong(action -> action.getViv().longValue())
                               .sum();
    }
}
