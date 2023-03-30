package com.skyapi.weatherforecast.location.controller;

import com.skyapi.weatherforecast.base.BaseRestControllerTest;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherId;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.exception.GeolocationException;
import com.skyapi.weatherforecast.exception.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeolocationService;
import com.skyapi.weatherforecast.hourly.service.HourlyWeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HourlyWeatherApiControllerTests extends BaseRestControllerTest {

    private static final String END_POINT_PATH = "/v1/hourly";

    private static final String X_CURRENT_HOUR = "X-Current-Hour";

    @Autowired
    MockMvc mockMvc;


    @MockBean
    HourlyWeatherService hourlyWeatherService;

    @MockBean
    GeolocationService locationService;


    @Test
    public void testListHourlyForecastByIPAddress() throws Exception {

        String ipAddress = "127.0.0.1";
        int currentHour = 5;

        HttpHeaders headers = new HttpHeaders();
        headers.set(X_CURRENT_HOUR, String.valueOf(currentHour));

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        String expectedLocation = location.toString();

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
                .header(X_CURRENT_HOUR, String.valueOf(currentHour))
                .contentType("application/json")
                .with(request -> {
                    request.setRemoteAddr(ipAddress);
                    return request;
                });

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(expectedLocation)))
                .andExpect(jsonPath("$.hourly_forecast[0].temperature", is(20)))
                .andExpect(jsonPath("$.hourly_forecast[0].precipitation", is(10)))
                .andExpect(jsonPath("$.hourly_forecast[1].temperature", is(20)))
                .andExpect(jsonPath("$.hourly_forecast[1].precipitation", is(10)))
                .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseNoHeaderXCurrentHour() throws Exception {
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {

        when(locationService.getLocation(anyString())).thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, "9"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn404NotFound() throws Exception {
        Location location = new Location().code("DELHI_IN");
        int currentHour = 9;

        when(locationService.getLocation(anyString())).thenReturn(location);
        when(hourlyWeatherService.getByLocation(location, currentHour)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn400BadRequest() throws Exception {
        String locationCode = "DELHI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        mockMvc.perform(get(requestURI))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn404NotFound() throws Exception {
        int currentHour = 9;
        String locationCode = "DELHI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn204NoContent() throws Exception {
        int currentHour = 9;
        String locationCode = "DELHI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn200OK() throws Exception {

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        int currentHour = 11;
        String locationCode = location.getCode();
        String requestURI = END_POINT_PATH + "/" + locationCode;

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

        when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(hourlyForecastList);

        mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(location.toString())))
                .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(12)))
                .andExpect(jsonPath("$.hourly_forecast[1].hour_of_day", is(13)))
                .andDo(print());
    }
}
