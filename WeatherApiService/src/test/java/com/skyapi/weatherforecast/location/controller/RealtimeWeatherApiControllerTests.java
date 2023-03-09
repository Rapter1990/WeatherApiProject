package com.skyapi.weatherforecast.location.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.base.BaseRestControllerTest;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.exception.GeolocationException;
import com.skyapi.weatherforecast.exception.LocationNotFoundException;
import com.skyapi.weatherforecast.location.dto.RealtimeWeatherDTO;
import com.skyapi.weatherforecast.location.service.GeolocationService;
import com.skyapi.weatherforecast.location.service.RealtimeWeatherService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.hamcrest.CoreMatchers.is;

public class RealtimeWeatherApiControllerTests extends BaseRestControllerTest {

    private static final String END_POINT_PATH = "/v1/realtime";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RealtimeWeatherService realtimeWeatherService;

    @MockBean
    GeolocationService locationService;

    @MockBean
    ModelMapper modelMapper;

    @Test
    public void testGetShouldReturnStatus400BadRequest() throws Exception {
        when(locationService.getLocation(anyString())).thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus404NotFound() throws Exception {
        Location location = new Location();

        when(locationService.getLocation(anyString())).thenReturn(location);
        when(realtimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus200OK() throws Exception {

        // given
        Location location = Location.builder()
                .code("SFCA_USA")
                .cityName("San Franciso")
                .regionName("California")
                .countryName("United States of America")
                .countryCode("US")
                .build();

        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .temperature(12)
                .humidity(32)
                .lastUpdated(LocalDateTime.of(2023, 3, 2, 15, 30))
                .precipitation(88)
                .status("Cloudy")
                .windSpeed(5)
                .build();

        RealtimeWeatherDTO realtimeWeatherDTO = RealtimeWeatherDTO.builder()
                .location(location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName())
                .temperature(12)
                .humidity(32)
                .precipitation(88)
                .status("Cloudy")
                .windSpeed(5)
                .lastUpdated(LocalDateTime.of(2023, 3, 2, 15, 30))
                .build();


        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);


        when(locationService.getLocation(anyString())).thenReturn(location);
        when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);
        when(modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class)).thenReturn(realtimeWeatherDTO);

        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(expectedLocation)))
                .andDo(print());
    }
}
