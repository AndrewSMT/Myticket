package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.Role;
import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean addUser(User user){
            User userFromDb = userRepo.findByUsername(user.getUsername());
            if(userFromDb != null){
                return false;
            }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userByUsername = userRepo.findByUsername(s);
        if(userByUsername == null){
            throw new UsernameNotFoundException("User not found");
        }
        return userByUsername;
    }
}
