package com.skyapi.weatherforecast.location.repository;

import com.skyapi.weatherforecast.common.RealtimeWeather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather, String> {

}
