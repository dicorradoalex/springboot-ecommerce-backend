package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    // Grazie a JpaRepository: save(), findById(), findAll(), deleteById()


}
