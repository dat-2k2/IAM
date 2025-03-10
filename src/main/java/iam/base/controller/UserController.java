package iam.base.controller;

import iam.base.dto.auth.UserPrincipal;
import iam.base.dto.user.RegisterForm;
import iam.base.entities.user.User;
import iam.base.service.UserService;
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
