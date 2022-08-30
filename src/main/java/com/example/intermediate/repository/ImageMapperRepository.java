package com.example.intermediate.repository;

import com.example.intermediate.domain.ImageMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageMapperRepository extends JpaRepository<ImageMapper, Long> {
    Optional<ImageMapper> findByName(String name);
    Optional<ImageMapper> findByUrl(String url);
}
