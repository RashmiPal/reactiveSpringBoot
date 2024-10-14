package com.jts.controller;

import com.jts.model.Userdetails;
import com.jts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Userdetails> createUser(@RequestBody Userdetails userdetails) {
        return userService.createUser(userdetails);
    }

    @GetMapping
    public Flux<Userdetails> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<Userdetails> getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public Mono<Userdetails> updateUser(@PathVariable int id, @RequestBody Userdetails userUpdates) {
        Mono<Userdetails> updatedUser = userService.updateUser(id, userUpdates);
        updatedUser.subscribe(userdetails -> System.out.println("updatedUser userName is:"+ userdetails.getUsername()));
        return updatedUser;
    }

    @GetMapping("/fetch/{id}")
    public Mono<Userdetails> fetchUserByIdFromApi(@PathVariable int id) {
        return userService.fetchUserByIdFromApi(id).log();
    }

}
