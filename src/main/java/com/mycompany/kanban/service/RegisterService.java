package com.mycompany.kanban.service;

import com.mycompany.common.client.MailClient;
import com.mycompany.common.client.UserDatabaseClient;
import com.mycompany.common.entity.User;
import com.mycompany.kanban.config.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by antongusev on 19.11.16.
 */
@Service
public class RegisterService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDatabaseClient userDatabaseClient;

    @Autowired
    private ConfigurationProperties configurationProperties;

    @Autowired
    private MailClient mailClient;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void registerUser(User userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
//        user.setAuthorityList(Collections.singletonList(Authority.ROLE_USER.name()));
        String activationKey = Sha512DigestUtils.shaHex(Objects.toString(user));
        user.setActivationKey(activationKey);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setActivated(true);

        userDatabaseClient.saveUser(user);
        sendEmail(user);
    }

    @Async
    public void sendEmail(User user) {
        mailClient.send(user.getEmail(),
                String.format(configurationProperties.getAddress(), user.getUsername(),
                        user.getActivationKey()));
    }

    public void confirm(User user, String activationKey) {
        if (activationKey.equals(user.getActivationKey())) {
            user.setActivated(true);
            user.setActivationKey(null);
            userDatabaseClient.saveUser(user);
        }
    }

}
