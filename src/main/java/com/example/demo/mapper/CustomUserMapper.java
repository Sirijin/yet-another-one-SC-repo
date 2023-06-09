//package com.example.demo.mapper;
//
//import com.example.demo.dto.UserDto;
//import com.example.demo.entity.Role;
//import com.example.demo.entity.User;
//import com.example.demo.repository.RoleRepository;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.type.RoleSet;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class CustomUserMapper {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    User fromUserDto(UserDto userDto) {
//        User user = new User();
//
//        user.setUsername(userDto.getUsername());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        Set<Role> roles = new HashSet<>();
//
//        for (RoleSet roleSet : userDto.getRoles()) {
//            roles.add(new Role())
//        }
//        user.setRoles();
//        return user;
//    }
//}
