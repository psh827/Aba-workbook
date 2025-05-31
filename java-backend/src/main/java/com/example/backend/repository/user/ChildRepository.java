package com.example.backend.repository.user;

import com.example.backend.domain.basic.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, String> {
    Optional<Child> findByName(String name);
}
