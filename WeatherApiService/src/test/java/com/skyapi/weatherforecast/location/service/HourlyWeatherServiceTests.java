package com.skyapi.weatherforecast.location.service;

import com.skyapi.weatherforecast.base.BaseServiceTest;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherId;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.exception.LocationNotFoundException;
import com.skyapi.weatherforecast.hourly.service.HourlyWeatherService;
import com.skyapi.weatherforecast.hourly.repository.HourlyWeatherRepository;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class HourlyWeatherServiceTests extends BaseServiceTest {

    @Mock
    private HourlyWeatherRepository hourlyWeatherRepo;

    @Mock
    private LocationRepository locationRepo;

    @InjectMocks
    private HourlyWeatherService hourlyWeatherService;

    @Test
    public void testGetByLocation() throws LocationNotFoundException {

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        HourlyWeather mockedHourlyWeather = HourlyWeather.builder()
                .id(new HourlyWeatherId(10,location))
                .temperature(15)
                .precipitation(10)
                .status("Sunny")
                .build();

        // when
        when(locationRepo.findByCountryCodeAndCityName(countryCode, cityName)).thenReturn(location);
        when(hourlyWeatherRepo.findByLocationCode(anyString(), anyInt())).thenReturn(Collections.singletonList(mockedHourlyWeather));

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getByLocation(location, 10);

        // then
        assertEquals(1, hourlyWeatherList.size());
        assertThat(hourlyWeatherList).contains(mockedHourlyWeather);
    }

    @Test
    public void testGetByLocationCode() throws LocationNotFoundException {

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        String locationCode = location.getCode();

        HourlyWeather mockedHourlyWeather = HourlyWeather.builder()
                .id(new HourlyWeatherId(10,location))
                .temperature(15)
                .precipitation(10)
                .status("Sunny")
                .build();

        // when
        when(locationRepo.findByCode(locationCode)).thenReturn(location);
        when(hourlyWeatherRepo.findByLocationCode(anyString(), anyInt())).thenReturn(Collections.singletonList(mockedHourlyWeather));

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getByLocationCode(locationCode, 10);

        // then
        assertEquals(1, hourlyWeatherList.size());
        assertThat(hourlyWeatherList).contains(mockedHourlyWeather);

    }
}
