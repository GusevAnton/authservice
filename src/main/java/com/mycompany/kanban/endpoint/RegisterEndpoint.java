package com.mycompany.kanban.endpoint;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycompany.common.client.UserDatabaseClient;
import com.mycompany.common.entity.User;
import com.mycompany.common.function.AsyncProcessor;
import com.mycompany.common.view.UserView;
import com.mycompany.kanban.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;

@RestController
public class RegisterEndpoint {

    @Autowired
    private UserDatabaseClient userDatabaseClient;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AsyncProcessor asyncProcessor;

    @JsonView(UserView.Register.class)
    @PostMapping(value = "/register")
    public DeferredResult<ResponseEntity> register(@RequestBody @Valid @JsonView(UserView.Register.class) final User userRequest) {
        return asyncProcessor.runAsync(() -> registerService.registerUser(userRequest));
    }

    @GetMapping("/confirm/{username}/{activationKey}")
    public DeferredResult<ResponseEntity> confirm(@PathVariable String username, @PathVariable String activationKey) {
        return asyncProcessor.supplyThenAccept(() -> userDatabaseClient.findUserByUsername(username).getContent(),
                (user) -> registerService.confirm((User) user, activationKey));
    }

}
