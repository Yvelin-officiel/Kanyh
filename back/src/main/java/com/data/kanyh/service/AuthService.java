package com.data.kanyh.service;

import com.data.kanyh.dto.AuthRequest;
import com.data.kanyh.dto.AuthResponse;
import com.data.kanyh.dto.UserDTO;
import com.data.kanyh.exception.AlreadyExists;
import com.data.kanyh.exception.InvalidCredentials;
import com.data.kanyh.model.Role;
import com.data.kanyh.model.User;
import com.data.kanyh.repository.UserRepository;
import com.data.kanyh.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    private static final String INVALID_CREDENTIALS = "Identifiants invalides";

    public void register(AuthRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new AlreadyExists("Le nom d'utilisateur existe déjà");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Role.COMMANDITAIRE);

        userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new InvalidCredentials(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentials(INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(user.getUserName(), user.getRoles());
        UserDTO userDto = new UserDTO(user.getId(), user.getUserName(), user.getRoles());
        return new AuthResponse(token, userDto);
    }
}
