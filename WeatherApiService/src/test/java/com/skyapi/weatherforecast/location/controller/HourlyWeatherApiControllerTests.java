package com.skyapi.weatherforecast.location.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.base.BaseRestControllerTest;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherId;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.service.GeolocationService;
import com.skyapi.weatherforecast.location.service.HourlyWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HourlyWeatherApiControllerTests extends BaseRestControllerTest {

    private static final String END_POINT_PATH = "/v1/hourly";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    HourlyWeatherService hourlyWeatherService;

    @MockBean
    GeolocationService locationService;

    @Mock
    HttpServletRequest request;

    @Test
    public void testListHourlyForecastByIPAddress() throws Exception {

        String ipAddress = "127.0.0.1";
        int currentHour = 5;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Current-Hour", String.valueOf(currentHour));

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        HourlyWeather hourlyWeather1 = HourlyWeather.builder()
                .id(new HourlyWeatherId(12, location))
                .precipitation(10)
                .temperature(20)
                .status("Cloudy")
                .build();
        HourlyWeather hourlyWeather2 = HourlyWeather.builder()
                .id(new HourlyWeatherId(13, location))
                .precipitation(10)
                .temperature(20)
                .status("Cloudy")
                .build();

        List<HourlyWeather> hourlyForecastList = List.of(hourlyWeather1,hourlyWeather2);

        // when
        when(locationService.getLocation(eq(ipAddress))).thenReturn(location);
        when(hourlyWeatherService.getByLocation(eq(location), eq(currentHour))).thenReturn(hourlyForecastList);


        MockHttpServletRequestBuilder requestBuilder = get(END_POINT_PATH)
                .header("X-Current-Hour", String.valueOf(currentHour))
                .contentType("application/json")
                .with(request -> {
                    request.setRemoteAddr(ipAddress);
                    return request;
                });

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].temperature", is(20)))
                .andExpect(jsonPath("$[0].precipitation", is(10)))
                .andExpect(jsonPath("$[1].temperature", is(20)))
                .andExpect(jsonPath("$[1].precipitation", is(10)))
                .andDo(print());
    }
}
