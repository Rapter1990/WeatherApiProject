package com.skyapi.weatherforecast.location.service;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.exception.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.HourlyWeatherRepository;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HourlyWeatherService {

    private final HourlyWeatherRepository hourlyWeatherRepo;
    private final LocationRepository locationRepo;

    public List<HourlyWeather> getByLocation(Location location, int currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationInDB == null) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return hourlyWeatherRepo.findByLocationCode(locationInDB.getCode(), currentHour);
    }
}
