package com.nisum.restfulapi.security;

import com.nisum.restfulapi.model.User;

public interface TokenProvider {

    String generateToken(User user);

}
