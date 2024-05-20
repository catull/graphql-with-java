package com.graphqljava.tutorial.jpa.chinook;

import com.graphqljava.tutorial.models.ChinookModels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<ChinookModels.Artist, Integer> {
}
