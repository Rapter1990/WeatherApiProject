package com.skyapi.weatherforecast.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.base.BaseRestControllerTest;
import com.skyapi.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

public class LocationApiControllerTests extends BaseRestControllerTest {

    private static final String END_POINT_PATH = "/v1/locations";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    LocationService service;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {

        // given
        Location location = new Location();

        // when

        String bodyContent = mapper.writeValueAsString(location);

        // then
        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {

        // given
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        // when
        Mockito.when(service.add(location)).thenReturn(location);

        String bodyContent = mapper.writeValueAsString(location);

        // then
        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code", is("NYC_USA")))
                .andExpect(jsonPath("$.city_name", is("New York City")))
                .andExpect(header().string("Location", "/v1/locations/NYC_USA"))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {

        // given

        // when
        Mockito.when(service.list()).thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testListShouldReturnLocationList() throws Exception {

        // given
        Location location1 = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        Location location2 = Location.builder()
                .code("LACA_USA")
                .cityName("Los Angeles")
                .regionName("California")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        // when
        Mockito.when(service.list()).thenReturn(List.of(location1, location2));

        // then
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].code", is("NYC_USA")))
                .andExpect(jsonPath("$[0].city_name", is("New York City")))
                .andExpect(jsonPath("$[0].region_name", is("New York")))
                .andExpect(jsonPath("$[0].country_name", is("United States of America")))
                .andExpect(jsonPath("$[0].country_code", is("US")))
                .andExpect(jsonPath("$[0].enabled", is(true)))
                .andExpect(jsonPath("$[1].code", is("LACA_USA")))
                .andExpect(jsonPath("$[1].city_name", is("Los Angeles")))
                .andExpect(jsonPath("$[1].region_name", is("California")))
                .andExpect(jsonPath("$[1].country_name", is("United States of America")))
                .andExpect(jsonPath("$[1].country_code", is("US")))
                .andExpect(jsonPath("$[1].enabled", is(true)))
                .andDo(print());
    }

    @Test
    public void testGetLocationShouldReturn405MethodNotAllowed() throws Exception {

        // given
        // when

        // then
        String requestURI = END_POINT_PATH + "/ABCDEF";
        mockMvc.perform(post(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    public void testGetLocationShouldReturn404NotFound() throws Exception {

        // given
        // when

        // then
        String requestURI = END_POINT_PATH + "/ABCDEF";
        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetLocationShouldReturn200OK() throws Exception {

        // given
        String code = "LACA_USA";
        String requestURI = END_POINT_PATH + "/" + code;

        Location location = Location.builder()
                .code("LACA_USA")
                .cityName("Los Angeles")
                .regionName("California")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        // when
        Mockito.when(service.get(code)).thenReturn(location);

        // then
        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code", is("LACA_USA")))
                .andExpect(jsonPath("$.city_name", is("Los Angeles")))
                .andExpect(jsonPath("$.region_name", is("California")))
                .andExpect(jsonPath("$.country_name", is("United States of America")))
                .andExpect(jsonPath("$.country_code", is("US")))
                .andExpect(jsonPath("$.enabled", is(true)))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        Location location = new Location();
        location.setCode("ABCDEF");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(service.update(location)).thenThrow(new LocationNotFoundException("No location found"));

        String bodyContent = mapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        Location location = new Location();
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        String bodyContent = mapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(service.update(location)).thenReturn(location);

        String bodyContent = mapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code", is("NYC_USA")))
                .andExpect(jsonPath("$.city_name", is("New York City")))
                .andExpect(jsonPath("$.region_name", is("New York")))
                .andExpect(jsonPath("$.country_code", is("US")))
                .andExpect(jsonPath("$.country_name", is("United States of America")))
                .andExpect(jsonPath("$.enabled", is(true)))
                .andDo(print());
    }
}
