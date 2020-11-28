package com.invivoo.vivwallet.api.interfaces.users.expertises;

import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UserExpertisesController.API_V_1_USERS_ID_EXPERTISES)
public class UserExpertisesController {

    public static final String USER_ID = "userId";
    public static final String API_V_1_USERS_ID_EXPERTISES = "/api/v1/users/{" + USER_ID + "}/expertises";
    private final UserService userService;

    public UserExpertisesController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserExpertiseDto>> getUserExpertises(@PathVariable(USER_ID) Long userId) {
        LocalDate now = LocalDate.now();
        return userService.findById(userId)
                          .map(User::getExpertises)
                          .map(userExpertises -> userExpertises.stream()
                                                               .filter(e -> e.isValid(now))
                                                               .map(UserExpertiseDto::createFromUserExpertise)
                                                               .collect(Collectors.toList()))
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound()
                                                .build());
    }

    @PostMapping
    public ResponseEntity<UserExpertiseDto> postUserExpertise(@PathVariable(USER_ID) Long userId, @RequestBody UserExpertiseDto userExpertiseDto) {
        Optional<User> userOpt = userService.findById(userId);
        Optional<Expertise> expertiseOpt = Expertise.forName(userExpertiseDto.getExpertise().getId());
        if (expertiseOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.notFound()
                                 .build();
        }
        User user = userOpt.get();
        HashSet<UserExpertise> expertises = new HashSet<>(user.getExpertises());
        UserExpertise userExpertise = UserExpertise.builder()
                                                   .expertise(expertiseOpt.get())
                                                   .startDate(userExpertiseDto.getStartDate())
                                                   .endDate(userExpertiseDto.getEndDate())
                                                   .build();
        expertises.add(userExpertise);
        user.setExpertises(expertises);
        User savedUser = userService.save(user);
        return ResponseEntity.created(getExpertiseLocation(user, userExpertise))
                             .body(UserExpertiseDto.createFromUserExpertise(userExpertise));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserExpertise> putExpertiseMember(@PathVariable(USER_ID) Long userId, @PathVariable("id") Long userExpertiseId, @RequestBody UserExpertise update) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound()
                                 .build();
        }
        User user = userOpt.get();
        Optional<UserExpertise> userExpertiseOpt = user.getExpertises()
                                                       .stream()
                                                       .filter(expertise -> expertise.getId().equals(userExpertiseId))
                                                       .findFirst();

        if (userExpertiseOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                                 .build();
        }
        UserExpertise userExpertise = userExpertiseOpt.get();
        updateUserExpertise(userExpertise, update);
        userService.save(user);
        return ResponseEntity.created(getExpertiseLocation(user, userExpertise))
                             .body(userExpertise);
    }

    private void updateUserExpertise(UserExpertise userExpertise, UserExpertise update) {
        userExpertise.setExpertise(update.getExpertise());
        userExpertise.setStatus(update.getStatus());
        userExpertise.setStartDate(update.getEndDate());
        userExpertise.setEndDate(update.getEndDate());
    }

    private URI getExpertiseLocation(User user, UserExpertise userExpertise) {
        return URI.create(API_V_1_USERS_ID_EXPERTISES.replace("{" + USER_ID + "}", user.getId().toString())
                          + "/"
                          + Optional.ofNullable(userExpertise.getId())
                                    .map(Object::toString)
                                    .orElse(null));
    }
}
