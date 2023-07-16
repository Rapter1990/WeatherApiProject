package com.skyapi.weatherforecast.daily.repository;

import com.skyapi.weatherforecast.base.BaseRepositoryTests;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;
import com.skyapi.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class DailyWeatherRepositoryTests extends BaseRepositoryTests {

    @Autowired
    private DailyWeatherRepository repo;

    @Test
    public void testAdd() {
        String locationCode = "DANA_VN";

        Location location = new Location().code(locationCode);

        DailyWeather forecast = new DailyWeather()
                .location(location)
                .dayOfMonth(16)
                .month(7)
                .minTemp(23)
                .maxTemp(32)
                .precipitation(40)
                .status("Cloudy");

        DailyWeather addedForecast = repo.save(forecast);

        assertThat(addedForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
    }

    @Test
    public void testDelete() {
        String locationCode = "DELHI_IN";

        Location location = new Location().code(locationCode);

        DailyWeatherId id = new DailyWeatherId(16, 7, location);

        repo.deleteById(id);

        Optional<DailyWeather> result = repo.findById(id);

        assertThat(result).isNotPresent();
    }
}
