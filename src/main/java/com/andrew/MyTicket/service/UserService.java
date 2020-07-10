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
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    public boolean addUser(User user){
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        if(!StringUtils.isEmpty(user.getEmail())){
            sendEmail(user);
        }
        return true;
    }
    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
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

    public boolean editUser(User user) {
        userRepo.save(user);
        return true;
    }

    public String sendEmail(User user) {
        String message = String.format("Hello, %s\n Welcome to MyTicket. Please, " +
                "click to this link for activate account: http://localhost:8080/registration/activate/%s",user.getUsername(),user.getActivationCode());
        mailSender.send(user.getEmail(),"Activation code", message);
        return message;
    }
}
