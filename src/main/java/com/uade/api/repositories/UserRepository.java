package com.uade.api.repositories;

import com.uade.api.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(@Param("email") String email);
    User findById(@Param("identificador") int id);
    User findUserByEmail(String email);
}
