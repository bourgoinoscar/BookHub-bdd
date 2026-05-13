package com.example.backend.Repository;

import com.example.backend.Entity.Revue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRevueRepository extends JpaRepository<Revue, Integer> {
    List<Revue> findByLivreId(Integer livreId);
}