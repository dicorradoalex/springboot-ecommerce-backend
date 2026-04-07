package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.OrderEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderEntryRepository extends JpaRepository<OrderEntryEntity, UUID> {

    // Grazie a JpaRepository: save(), findById(), findAll(), deleteById()


}
