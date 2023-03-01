package com.nisum.restfulapi.service;

import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;

public interface RegisterService {

    ResponseRegister saveRegister(RequestRegister requestRegister);
}
