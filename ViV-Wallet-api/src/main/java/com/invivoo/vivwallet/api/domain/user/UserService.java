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

    public Optional<User> findByX4bId(String x4bId){
        return userRepository.findByX4bId(x4bId);
    }

    public User findByX4bIdOrCreateIfNotExists(String x4bId) {
        return userRepository.findByX4bId(x4bId)
                             .orElseGet(() -> userRepository.findByFullName(getFullNameFromX4bId(x4bId))
                                                            .orElseGet(() -> createUser(x4bId)));
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

    private String getFullNameFromX4bId(String x4bId) {
        return x4bId.replaceAll("[0-9]", "").trim();
    }

    private User createUser(String x4bId) {
        User user = User.builder()
                        .x4bId(x4bId)
                        .fullName(getFullNameFromX4bId(x4bId))
                        .build();
        return userRepository.save(user);
    }
}
