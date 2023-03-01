package com.nisum.restfulapi.controller;

import com.nisum.restfulapi.model.DTO.RequestRegister;
import com.nisum.restfulapi.model.DTO.ResponseRegister;
import com.nisum.restfulapi.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseRegister> createRegister(@Valid @RequestBody RequestRegister requestRegister) throws Exception {
        ResponseRegister responseRegister = registerService.saveRegister(requestRegister);
        HttpStatus status = responseRegister.getUser() != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<ResponseRegister>(responseRegister, status);
    }
}