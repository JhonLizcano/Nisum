package com.nisum.restfulapi.service;

import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.UserDto;

public interface RegisterService {

    UserDto saveRegister(RequestRegister requestRegister);

}
