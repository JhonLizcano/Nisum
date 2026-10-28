package com.nisum.restfulapi.service.impl;

import com.nisum.restfulapi.exceptions.EmailAlreadyExistsException;
import com.nisum.restfulapi.exceptions.InvalidPasswordException;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.UserDto;
import com.nisum.restfulapi.model.User;
import com.nisum.restfulapi.repository.UserRepository;
import com.nisum.restfulapi.security.TokenProvider;
import com.nisum.restfulapi.service.RegisterService;
import com.nisum.restfulapi.util.RegisterMapper;
import com.nisum.restfulapi.validation.PasswordPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final PasswordPolicy passwordPolicy;

    @Override
    @Transactional
    public UserDto saveRegister(RequestRegister requestRegister) {
        if (!passwordPolicy.isValid(requestRegister.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (userRepository.findByEmailIgnoreCase(requestRegister.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User user = RegisterMapper.getUserFromRequestRegister(requestRegister);
        user.setToken(tokenProvider.generateToken(user));
        user.setPassword(passwordEncoder.encode(requestRegister.getPassword()));
        User savedUser = userRepository.save(user);

        return RegisterMapper.getUserDtoFromUser(savedUser);
    }

}
