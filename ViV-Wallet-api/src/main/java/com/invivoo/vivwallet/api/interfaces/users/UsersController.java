package com.invivoo.vivwallet.api.interfaces.users;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.interfaces.actions.ActionDto;
import com.invivoo.vivwallet.api.interfaces.payments.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UsersController.API_V_1_USERS)
public class UsersController {

    static final String API_V_1_USERS = "/api/v1/users";
    private final UserService userService;
    private final ActionService actionService;
    private final PaymentService paymentService;

    @Autowired
    public UsersController(UserService userService,
                           ActionService actionService,
                           PaymentService paymentService) {
        this.userService = userService;
        this.actionService = actionService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) Expertise expertise) {
        List<User> users = expertise == null ? userService.findAll() : userService.findByExpertise(expertise);
        return ResponseEntity.ok(users.stream()
                                      .map(UserDto::createFromUser)
                                      .sorted(Comparator.comparing(UserDto::getFullName))
                                      .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<UserDto> postUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.created(getLocation(savedUser))
                             .body(UserDto.createFromUser(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId) {
        return userService.findById(userId)
                          .map(UserDto::createFromUser)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound()
                                                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> putUser(@PathVariable("id") Long userId, @RequestBody UserUpdateDto userUpdate) {
        return userService.findById(userId)
                          .map(user -> updateUser(user, userUpdate))
                          .map(userService::save)
                          .map(UserDto::createFromUser)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound()
                                                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") Long userId) {
        return userService.findById(userId)
                          .map(this::deleteUser)
                          .map(UserDto::createFromUser)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound()
                                                .build());
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable("id") Long userId) {
        return userService.findById(userId)
                          .map(userService::computeBalance)
                          .map(BalanceDto::new)
                          .map(ResponseEntity::ok)
                          .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/{id}/actions")
    public ResponseEntity<List<ActionDto>> getActions(@PathVariable("id") Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        List<Action> userActions = actionService.findAllByAchieverOrderByDateDesc(user);
        List<ActionDto> userActionDtos = userActions.stream()
                                                    .map(ActionDto::createFromAction)
                                                    .collect(Collectors.toList());

        return ResponseEntity.ok(userActionDtos);
    }

    @PostMapping("/{id}/actions")
    public ResponseEntity<List<ActionDto>> updateActions(@PathVariable("id") Long userId, @RequestBody ActionsUpdateRequest updateRequest) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Map<String, ActionUpdateRequest> updateById = updateRequest.getUpdates()
                                                                   .stream()
                                                                   .filter(actionUpdateRequest -> !Action.ACTION_FOR_INITIAL_BALANCE_ID.toString()
                                                                                                                                       .equals(actionUpdateRequest.getId()))
                                                                   .collect(Collectors.toMap(ActionUpdateRequest::getId,
                                                                                             Function.identity()));
        List<Action> actions = actionService.findAllByAchieverAndIdIn(userOpt.get(),
                                                                      updateById.keySet()
                                                                                .stream()
                                                                                .map(Long::valueOf)
                                                                                .collect(Collectors.toList()));
        actions.forEach(action -> action.setStatus(updateById.get(String.valueOf(action.getId())).getStatus()));
        actionService.saveAll(actions);
        return ResponseEntity.ok(actions.stream().map(ActionDto::createFromAction).collect(Collectors.toList()));
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<List<PaymentDto>> getPayments(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(paymentService.findAllByReceiver(userId));
    }

    @PostMapping("/updateFromLynx")
    @PreAuthorize("hasAnyAuthority('API_USER')")
    public ResponseEntity<List<UserDto>> updateFromLynx() {
        List<UserDto> users = userService.updateFromLynx()
                                         .stream()
                                         .map(UserDto::createFromUser)
                                         .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    private URI getLocation(User savedUser) {
        return URI.create(String.format("%s/%s", API_V_1_USERS, savedUser.getId()));
    }

    private User updateUser(User user, UserUpdateDto userUpdate) {
        user.setFullName(userUpdate.getFullName().trim());
        return user;
    }

    private User deleteUser(User user) {
        userService.delete(user);
        return user;
    }
}
