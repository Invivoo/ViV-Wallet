package com.invivoo.vivwallet.api.interfaces.users;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMember;
import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMemberRepository;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.interfaces.actions.ActionDto;
import com.invivoo.vivwallet.api.interfaces.actions.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UsersController.API_V_1_USERS)
public class UsersController {

    static final String API_V_1_USERS = "/api/v1/users";
    private final UserRepository userRepository;
    private final UserService userService;
    private final ActionService actionService;
    private final ExpertiseMemberRepository expertiseMemberRepository;

    @Autowired
    public UsersController(UserRepository userRepository, UserService userService,
                           ActionService actionService, ExpertiseMemberRepository expertiseMemberRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.actionService = actionService;
        this.expertiseMemberRepository = expertiseMemberRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.created(getLocation(savedUser))
                .body(user);
    }

    private URI getLocation(User savedUser) {
        return URI.create(String.format("%s/%s", API_V_1_USERS, savedUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        return userRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> putUser(@PathVariable("id") Long userId, @RequestBody UserUpdateDto userUpdate) {
        return userRepository.findById(userId)
                .map(user -> updateUser(user, userUpdate))
                .map(userRepository::save)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    private User updateUser(User user, UserUpdateDto userUpdate) {
        user.setFullName(userUpdate.getFullName());
        return user;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {
        return userRepository.findById(userId)
                .map(this::deleteUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    private User deleteUser(User user) {
        userRepository.delete(user);
        return user;
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Long> getBalance(@PathVariable("id") Long userId) {
        long balance = userService.computeBalance(userId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{id}/actions")
    public ResponseEntity<List<ActionDto>> getActions(@PathVariable("id") Long userId) {
        Expertise expertise = userRepository.findById(userId)
                .flatMap(
                    user -> expertiseMemberRepository.findByUser(user.getFullName()).map(ExpertiseMember::getExpertise)
                )
                .orElse(null);

        List<Action> userActions = actionService.findAllByAchiever(userId);
        List<ActionDto> userActionDtos = userActions.stream()
                .map(action -> convertToGetDto(action, expertise))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userActionDtos);
    }

    private ActionDto convertToGetDto(Action action, Expertise userExpertise) {
        ActionDto actionDto = ActionDto.builder()
                .id(action.getId())
                .userId(action.getAchiever().getId())
                .type(action.getType().getName())
                .comment(action.getContext())
                .creationDate(action.getDate())
                .payment(action.getViv()).build();
        Optional.ofNullable(userExpertise).ifPresent(expertise -> actionDto.setExpertise(expertise.getExpertiseName()));
        Optional.ofNullable(action.getPayment()).ifPresentOrElse(
                payment -> {
                    actionDto.setStatus(ActionStatus.PAID.getLabel());
                    actionDto.setPaymentDate(payment.getDate());
                },
                () -> actionDto.setStatus(ActionStatus.UNPAID.getLabel())
        );

        return actionDto;
    }
}
