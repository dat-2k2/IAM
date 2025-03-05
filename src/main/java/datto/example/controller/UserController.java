package datto.example.controller;

import datto.example.dto.auth.UserPrincipal;
import datto.example.dto.user.RegisterForm;
import datto.example.entities.user.User;
import datto.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserPrincipal> registerUser(@RequestBody RegisterForm form){
        log.debug("Try to register {}", form.getUsername());
        User newUser =  userService.register(form);
        log.debug("OK register {}", form.getUsername());
        return ResponseEntity.ok(UserPrincipal.create(newUser));
    }

    @GetMapping("/user")
    public ResponseEntity<UserPrincipal> base(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(userPrincipal);
    }
}
