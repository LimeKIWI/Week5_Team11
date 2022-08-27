package com.example.intermediate.repository;


import com.example.intermediate.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByIdAndPidAndRole(Long id, Long pid,Role_Enum role_enum);
    List<Like> findByMember (Member member);

}
