package com.challenge.backend.repository;

import com.challenge.backend.model.entity.TracksByWeatherStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TracksByWeatherStatisticsRepository extends JpaRepository<TracksByWeatherStatistics, Long> {
}
