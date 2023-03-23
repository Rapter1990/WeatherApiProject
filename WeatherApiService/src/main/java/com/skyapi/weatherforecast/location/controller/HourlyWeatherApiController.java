package com.skyapi.weatherforecast.location.controller;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.exception.GeolocationException;
import com.skyapi.weatherforecast.exception.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeolocationService;
import com.skyapi.weatherforecast.location.service.HourlyWeatherService;
import com.skyapi.weatherforecast.location.utils.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/hourly")
@RequiredArgsConstructor
public class HourlyWeatherApiController {

    private final HourlyWeatherService hourlyWeatherService;
    private final GeolocationService locationService;

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

        try {
            Location locationFromIP = locationService.getLocation(ipAddress);

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP, currentHour);

            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(hourlyForecast);

        } catch (GeolocationException ex) {

            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException ex) {

            return ResponseEntity.notFound().build();
        }

    }
}
