package com.invivoo.vivwallet.api.domain.user;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentRepository;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@PersistenceContext
public class UserService {

    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final PaymentRepository paymentRepository;

    public UserService(UserRepository userRepository, ActionRepository actionRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.actionRepository = actionRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByExpertise(Expertise expertise) {
        return userRepository.findDistinctByExpertisesExpertise(expertise);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByFullName(String fullName) {
        return Optional.ofNullable(fullName)
                       .map(String::trim)
                       .flatMap(userRepository::findByFullNameIgnoreCase);
    }

    public User findByX4bIdOrCreateIfNotExists(String x4bId) {
        return userRepository.findByX4bId(x4bId)
                             .orElseGet(() -> userRepository.findByFullNameIgnoreCase(getFullNameFromX4bId(x4bId))
                                                            .orElseGet(() -> createUser(x4bId)));
    }

    public Optional<User> findByRoleType(RoleType type) {
        return userRepository.findFirstByRolesType(type);
    }

    public User save(User user) {
        updateRelatedEntitiesWithUser(user);
        return userRepository.save(user);
    }

    public List<User> saveAll(List<User> users) {
        users.forEach(this::updateRelatedEntitiesWithUser);
        return userRepository.saveAll(users);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public int computeBalance(User user) {
        int userBalanceFromActionsInVivAfterInitialBalanceDate = actionRepository.findAllByAchieverAndDateAfter(user, Optional.ofNullable(user.getVivInitialBalanceDate())
                                                                                                                              .orElse(LocalDateTime.of(2000, 1, 1, 0, 0)))
                                                                                 .stream()
                                                                                 .mapToInt(Action::getVivAmount)
                                                                                 .sum();
        int userPaymentsAmountInViv = paymentRepository.findAllByReceiverOrderByDateDesc(user)
                                                       .stream()
                                                       .mapToInt(Payment::getVivAmount)
                                                       .sum();
        return user.getVivInitialBalance() + userBalanceFromActionsInVivAfterInitialBalanceDate - userPaymentsAmountInViv;
    }

    private void updateRelatedEntitiesWithUser(User user) {
        Optional.ofNullable(user.getRoles())
                .ifPresent(roles -> roles.forEach(r -> r.setUser(user)));
        Optional.ofNullable(user.getExpertises())
                .ifPresent(expertises -> expertises.forEach(e -> e.setUser(user)));
    }

    private User createUser(String x4bId) {
        User user = User.builder()
                        .x4bId(x4bId)
                        .fullName(getFullNameFromX4bId(x4bId))
                        .build();
        return userRepository.save(user);
    }

    private String getFullNameFromX4bId(String x4bId) {
        return x4bId.replaceAll("[0-9]", "").trim();
    }
}
