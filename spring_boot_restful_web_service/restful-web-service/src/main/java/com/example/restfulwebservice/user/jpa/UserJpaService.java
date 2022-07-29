package com.example.restfulwebservice.user.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserJpaService {

    private final UserRepository repository;

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public Optional<User> findUser(int id) {
        return repository.findById(id);
    }

    @Transactional
    public void deleteUser(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public User addUser(User user) {
        return repository.save(user);
    }

    @Transactional
    public void updateUser(User user, String name) {
        user.setName(name);
    }

}
