package iam.base.service;

import iam.base.dto.user.RegisterForm;
import iam.base.entities.user.User;
import iam.base.exception.UserExistedException;
import iam.base.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow();
    }

    static void activate(User user){
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
    }

    public User createUser(RegisterForm form){
        if (userRepository.findByUsername(form.getUsername()).isPresent()){
            throw new UserExistedException();
        }

        User newUser = User.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .build();
        activate(newUser);

        return userRepository.save(newUser);
    }
}
