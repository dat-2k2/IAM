package datto.example.service;

import datto.example.dto.user.RegisterForm;
import datto.example.entities.user.User;
import datto.example.exception.UserExistedException;
import datto.example.repos.UserRepository;
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

    public User register(RegisterForm form){
        if (userRepository.findByUsername(form.getUsername()).isPresent()){
            throw new UserExistedException();
        }
        return userRepository.save(User.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .build());
    }
}
