package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

// Estende JpaRepository per fornire le operazioni CRUD di base
public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {

}
