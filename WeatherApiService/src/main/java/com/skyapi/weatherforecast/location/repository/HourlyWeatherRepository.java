package com.skyapi.weatherforecast.location.repository;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, HourlyWeatherId> {

    @Query("""
			SELECT h FROM HourlyWeather h WHERE
			h.id.location.code = ?1 AND h.id.hourOfDay > ?2
			AND h.id.location.trashed = false
			""")
    public List<HourlyWeather> findByLocationCode(String locationCode, int currentHour);
}
