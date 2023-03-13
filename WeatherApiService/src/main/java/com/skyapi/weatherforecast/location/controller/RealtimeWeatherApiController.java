package com.skyapi.weatherforecast.location.controller;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.exception.GeolocationException;
import com.skyapi.weatherforecast.exception.LocationNotFoundException;
import com.skyapi.weatherforecast.location.dto.RealtimeWeatherDTO;
import com.skyapi.weatherforecast.location.service.GeolocationService;
import com.skyapi.weatherforecast.location.service.RealtimeWeatherService;
import com.skyapi.weatherforecast.location.utils.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/realtime")
@RequiredArgsConstructor
public class RealtimeWeatherApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);

    private final GeolocationService locationService;
    private final RealtimeWeatherService realtimeWeatherService;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location locationFromIP = locationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);

            RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

            return ResponseEntity.ok(dto);

        } catch (GeolocationException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
        try {
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
            RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

            return ResponseEntity.ok(dto);

        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode,
                                                   @RequestBody @Valid RealtimeWeather realtimeWeatherInRequest) {

        realtimeWeatherInRequest.setLocationCode(locationCode);

        try {
            RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeatherInRequest);

            RealtimeWeatherDTO dto = modelMapper.map(updatedRealtimeWeather, RealtimeWeatherDTO.class);

            return ResponseEntity.ok(dto);

        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
