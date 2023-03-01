package com.nisum.restfulapi.service.impl;

import com.nisum.restfulapi.model.DTO.PhoneDTO;
import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;
import com.nisum.restfulapi.model.Phone;
import com.nisum.restfulapi.model.User;
import com.nisum.restfulapi.repository.PhoneRepository;
import com.nisum.restfulapi.repository.RegisterRepository;
import com.nisum.restfulapi.service.RegisterService;
import com.nisum.restfulapi.util.Constants;
import com.nisum.restfulapi.util.RegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Value("${registerapi.constants.passregexp}")
    private String passwordRegex;

    @Override
    public ResponseRegister saveRegister(RequestRegister requestRegister) {
        passwordRegex = passwordRegex != null ? passwordRegex : "[A-Za-z0-9]*";
        if (!requestRegister.getPassword().matches(passwordRegex)){
            return ResponseRegister.builder()
                    .mensaje(Constants.REGEX_PASSWORD)
                    .build();
        }

        Optional<User> userValidate = registerRepository.findByEmail(requestRegister.getEmail());
        if (!userValidate.isPresent()) {
            User user = registerRepository.save(RegisterMapper.getUserFromRequestRegister(requestRegister));
            List<PhoneDTO> phoneDTOS = requestRegister.getPhones();
            for (PhoneDTO phoneDTO : phoneDTOS) {
                Phone phone = Phone.builder()
                        .number(phoneDTO.getNumber())
                        .cityCode(phoneDTO.getCitycode())
                        .countryCode(phoneDTO.getCountrycode())
                        .user(user)
                        .build();
                phoneRepository.save(phone);
            }
            return ResponseRegister.builder()
                    .user(RegisterMapper.getUserDtoFromUser(user))
                    .build();
        } else {
            return ResponseRegister.builder()
                    .mensaje(Constants.EXIST_MAIL)
                    .build();
        }
    }
}
