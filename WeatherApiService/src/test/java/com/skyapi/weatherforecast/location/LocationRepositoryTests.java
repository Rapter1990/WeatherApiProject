package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.base.BaseRepositoryTests;
import com.skyapi.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class LocationRepositoryTests extends BaseRepositoryTests {

    @Autowired
    private LocationRepository repository;

    @Test
    public void testAddSuccess() {

        // given
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        // when
        Location savedLocation = repository.save(location);

        // then
        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
    }

    @Test
    public void testListSuccess() {

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        // when
        repository.save(location);
        List<Location> locations = repository.findUntrashed();

        // then
        assertThat(locations).isNotEmpty();
        locations.forEach(System.out::println);
    }
}
