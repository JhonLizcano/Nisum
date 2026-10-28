package com.nisum.restfulapi.service.impl;

import com.nisum.restfulapi.exceptions.EmailAlreadyExistsException;
import com.nisum.restfulapi.exceptions.InvalidPasswordException;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;
import com.nisum.restfulapi.model.User;
import com.nisum.restfulapi.repository.UserRepository;
import com.nisum.restfulapi.security.TokenProvider;
import com.nisum.restfulapi.service.RegisterService;
import com.nisum.restfulapi.util.RegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${registerapi.constants.passregexp}")
    private String passwordRegex;

    @Override
    @Transactional
    public ResponseRegister saveRegister(RequestRegister requestRegister) {
        passwordRegex = passwordRegex != null ? passwordRegex : "[A-Za-z0-9]*";
        if (!requestRegister.getPassword().matches(passwordRegex)) {
            throw new InvalidPasswordException();
        }

        if (userRepository.findByEmail(requestRegister.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User user = RegisterMapper.getUserFromRequestRegister(requestRegister);
        user.setToken(tokenProvider.generateToken(user));
        user.setPassword(passwordEncoder.encode(requestRegister.getPassword()));
        User savedUser = userRepository.save(user);

        return ResponseRegister.builder().user(RegisterMapper.getUserDtoFromUser(savedUser)).build();
    }

}
