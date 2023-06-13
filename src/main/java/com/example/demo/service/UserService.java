package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.exception.UserException;
import com.example.demo.mapper.CustomUserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomPrincipal;
import com.example.demo.security.JwtService;
import com.example.demo.web.request.AuthRequest;
import com.example.demo.web.response.LoginResponse;
import com.example.demo.web.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CustomUserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @SneakyThrows
    @Transactional
    public UserResponse getUserList() {
        return new UserResponse(userRepository.findAll().stream().map(userMapper::map).collect(Collectors.toList()), userRepository.count());
    }

    @SneakyThrows
    @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @SneakyThrows
    @Transactional
    public User updateUser(Long id, UserDto dto) {
        if (!userRepository.existsById(id)) {
            throw new UserException("User not exist");
        }

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        String oldUsername = user.getUsername();

        if (!user.getUsername().equals(dto.getUsername())) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty() && dto.getPassword().length() > 4) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRoles() != null) {
            user.setRoles(dto.getRoles().stream().map(roleService::findByName).collect(Collectors.toSet()));
        }

        userRepository.save(user);
        log.debug("User with username - {} has been updated. New username - {}, new roles - {}", oldUsername, dto.getUsername(), dto.getRoles());
        return user;
    }

    @SneakyThrows
    @Transactional
    public UserDto updatePassword(Long id, String password) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new UserException("User not exist");
        }
        if (!passwordEncoder.encode(user.getPassword()).equals(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
        log.info("In updatePassword - users password with: {} updated", id);

        return userMapper.map(user);
    }

    @SneakyThrows
    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            log.debug("User with id - {} has been deleted", id);
            return;
        }
        throw new UserException("User not exist");
    }

    @SneakyThrows
    @Transactional
    public LoginResponse loginUser(AuthRequest request) {
        String username = request.getUsername();
        if (!userRepository.existsByUsername(username)) {
            throw new UserException("User does not exist");
        }
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        String token = jwtService.createToken(user);

        return new LoginResponse(token);
    }

    @SneakyThrows
    @Transactional
    public User registerUser(UserDto userDto) {
        return userRepository.save(userMapper.map(userDto));
    }

    @SneakyThrows
    public UserDto getUserInfo(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userMapper.map(Objects.requireNonNull(userRepository.findById(principal.getId()).orElse(null)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }
}
