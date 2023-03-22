package com.example.demo.adaptor.outbound.repository;

import com.example.demo.adaptor.outbound.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<BookEntity, String> {

    Optional<BookEntity> findByTitle(String title);
}
