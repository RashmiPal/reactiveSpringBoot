package com.jts.service;

import com.jts.model.Userdetails;
import com.jts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;


    public Mono<Userdetails> createUser(Userdetails userdetails) {
       return userRepository.save(userdetails)
                .doOnSuccess(savedUser -> System.out.println("User created successfully : "+ savedUser))
                .doOnError(error -> System.err.println("Error in creating User : "+ error.getMessage()));
    }

    public Flux<Userdetails> getAllUsers() {
        return userRepository.findAll()
                .map(this::excludePassword)
                .doOnComplete(() -> System.out.println("Fetched all users successfully"))
                .doOnError(error -> System.err.println("Error fetching users: " + error.getMessage()));
    }

    public Mono<Userdetails> getUserById(long id) {
        return userRepository.findById(id)
                .map(this::excludePassword)
                .doOnSuccess(userdetails -> System.out.println("Fetched user: " + userdetails))
                .doOnError(error -> System.err.println("Error fetching user: " + error.getMessage()));
    }

    // Call another endpoint in the same microservice
    public Mono<Userdetails> fetchUserByIdFromApi(long id) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/users/{id}", id) // Replace with your service's URL and port
                .retrieve()
                .bodyToMono(Userdetails.class)
                .map(this::excludePassword)
                .doOnSuccess(userdetails -> System.out.println("Fetched user from API: " + userdetails))
                .doOnError(error -> System.err.println("Error fetching user from API: " + error.getMessage()));
    }

    public Mono<Void> deleteUser(long id) {
        return userRepository.deleteById(id)
                .doOnSuccess(aVoid -> System.out.println("User deleted successfully with ID: " + id))
                .doOnError(error -> System.err.println("Error deleting user: " + error.getMessage()));
    }

    public Mono<Userdetails> updateUser(long id, Userdetails userUpdates) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(userUpdates.getUsername());
                    existingUser.setEmail(userUpdates.getEmail());
                    // Note: Do NOT set the password directly unless you're hashing it!
                    return userRepository.save(existingUser);
                })
                .doOnSuccess(updatedUser -> System.out.println("User updated successfully: " + updatedUser))
                .doOnError(error -> System.err.println("Error updating user: " + error.getMessage()));
    }

    private Userdetails excludePassword(Userdetails userdetails) {
        if (userdetails != null) {
            userdetails.setPassword(null);
        }
        return userdetails;
    }
}
