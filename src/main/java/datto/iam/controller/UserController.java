package datto.iam.controller;

import datto.iam.dto.auth.UserPrincipal;
import datto.iam.service.UserService;
import datto.iam.dto.user.RegisterForm;
import datto.iam.entities.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserPrincipal> registerUser(@RequestBody RegisterForm form){
        log.debug("Try to createUser {}", form.getUsername());
        User newUser =  userService.createUser(form);
        log.debug("OK createUser {}", form.getUsername());
        return ResponseEntity.ok(UserPrincipal.create(newUser));
    }

    @GetMapping("/user")
    public ResponseEntity<UserPrincipal> base(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(userPrincipal);
    }
}
