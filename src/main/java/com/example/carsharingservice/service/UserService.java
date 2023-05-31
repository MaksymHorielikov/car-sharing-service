package com.example.carsharingservice.service;

import com.example.carsharingservice.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);

    User findById(Long id);

    Optional<User> findByEmail(String email);

    void delete(Long id);

    List<User> findAll();

    User update(User user);
}
