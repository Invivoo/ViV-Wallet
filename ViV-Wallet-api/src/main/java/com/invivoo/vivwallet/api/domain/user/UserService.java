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
import java.util.Objects;
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

    public Optional<User> findByX4bIdOrByFullName(String x4bId) {
        Optional<User> userByX4bId = userRepository.findByX4bIdIgnoreCase(x4bId);
        if(userByX4bId.isPresent()){
            return userByX4bId;
        }
        Optional<User> userByFullName = userRepository.findByFullNameIgnoreCase(getFullNameFromX4bId(x4bId));
        userByFullName.filter(user -> Objects.isNull(user.getX4bId())).ifPresent(user -> userRepository.save(user.toBuilder().x4bId(x4bId).build()));
        return userByFullName;
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

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public int computeBalance(User user) {
        LocalDateTime vivInitialBalanceDate = Optional.ofNullable(user.getVivInitialBalanceDate())
                                                      .orElse(LocalDateTime.MIN);
        int userBalanceFromActionsInVivAfterInitialBalanceDate = actionRepository.findAllByAchieverAndValueDateAfter(user, vivInitialBalanceDate)
                                                                                 .stream()
                                                                                 .filter(Action::isPayable)
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

    private String getFullNameFromX4bId(String x4bId) {
        return x4bId.replaceAll("[0-9]", "").trim();
    }
}
