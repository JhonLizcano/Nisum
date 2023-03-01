package com.nisum.restfulapi.repository;

import com.nisum.restfulapi.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterRepository extends CrudRepository<User, Long> {

    User save(User User);

    Optional<User> findByEmail(String email);
}
