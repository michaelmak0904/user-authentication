package com.voting.userauth.service;

import com.voting.userauth.model.User;
import com.voting.userauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author michaelmak
 */
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> login(String username, String password) {
        // Find the user by username and password
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
