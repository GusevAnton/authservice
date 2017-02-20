package com.mycompany.kanban.endpoint;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycompany.common.client.UserDatabaseClient;
import com.mycompany.common.entity.User;
import com.mycompany.common.view.UserView;
import com.mycompany.kanban.service.RegisterService;
import io.reactivex.Flowable;
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

    @JsonView(UserView.Register.class)
    @PostMapping(value = "/register")
    public DeferredResult<ResponseEntity> register(@RequestBody @Valid @JsonView(UserView.Register.class) final User userRequest) {
        DeferredResult deferredResult = new DeferredResult();
        Flowable.just(registerService.registerUser(userRequest))
                .subscribe(result -> deferredResult.setResult(result));
        return deferredResult;
    }

    @GetMapping("/confirm/{username}/{activationKey}")
    public DeferredResult<ResponseEntity> confirm(@PathVariable String username, @PathVariable String activationKey) {
        DeferredResult deferredResult = new DeferredResult();
        Flowable.just(userDatabaseClient.findUserByUsername(username).getContent())
                .map((user) -> registerService.confirm((User) user, activationKey))
                .subscribe(result -> deferredResult.setResult(result));
        return deferredResult;
    }

}
