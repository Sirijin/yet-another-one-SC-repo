package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.exception.UserException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomPrincipal;
import com.example.demo.security.JwtUtil;
import com.example.demo.web.request.AuthRequest;
import com.example.demo.web.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


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

        if (!dto.getRoles().isEmpty()) {
            user.setRoles(dto.getRoles().stream().map(roleService::getRole).collect(Collectors.toSet()));
        }

        userRepository.save(user);
        log.debug("User with username - {} has been updated. New username - {}, new roles - {}", oldUsername, dto.getUsername(), dto.getRoles());
        return user;
    }

    @SneakyThrows
    @Transactional
    public UserDto updatePassword(Long id, String password) {
        UserDto userDto = userMapper.map(userRepository.findById(id).orElse(null));

        if (userDto == null) {
            throw new UserException("User not exist");
        }
        if (!passwordEncoder.encode(userDto.getPassword()).equals(password)) {
            userDto.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(userMapper.map(userDto));
        log.info("In updatePassword - users password with: {} updated", id);

        return userDto;
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
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @SneakyThrows
    @Transactional
    public Map<Object, Object> loginUser(AuthRequest request) {
        String username = request.getUsername();
        if (!userRepository.existsByUsername(username)) {
            throw new UserException("User does not exist");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        String token = jwtUtil.createToken(user);

        Map<Object, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("token", token);

        return response;
    }

    @SneakyThrows
    @Transactional
    public User registerUser(UserDto userDto) {
        User newUser = userMapper.map(userDto).toBuilder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(userDto.getRoles().stream().map(roleService::getRole).collect(Collectors.toSet()))
                .build();

        return userRepository.save(newUser);
    }

    @SneakyThrows
    @Transactional
    public Object getUserInfo(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userRepository.findById(principal.getId()).orElse(null);
    }

}

