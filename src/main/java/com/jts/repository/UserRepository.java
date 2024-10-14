package com.jts.repository;

import com.jts.model.Userdetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<Userdetails, Long> {
    Mono<Userdetails> findByUsername(String username);
}
