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
    public void testLocationListSuccess() {

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

    @Test
    public void testGetLocationNotFound() {

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        String code = "ABCD";

        // when
        repository.save(location);
        Location findlocationByCode = repository.findByCode(code);

        // then
        assertThat(findlocationByCode).isNull();
    }

    @Test
    public void testGetLocationFound() {

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .build();

        String code = "NYC_USA";

        // when
        repository.save(location);
        Location findlocationByCode = repository.findByCode(code);

        // then
        assertThat(findlocationByCode).isNotNull();
        assertThat(findlocationByCode.getCode()).isEqualTo(code);
    }

    @Test
    public void testUpdateLocation() {

        // given
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United States of America")
                .enabled(true)
                .trashed(false)
                .build();

        String code = "NYC_USA";

        // when
        repository.save(location);

        Location findlocationByCode = repository.findByCode(code);

        findlocationByCode.setCityName("New York City Updated");
        findlocationByCode.setRegionName("New York Updated");
        findlocationByCode.setCountryCode("US");
        findlocationByCode.setCountryName("United States of America Updated");
        findlocationByCode.setEnabled(true);
        findlocationByCode.setTrashed(false);

        // when
        repository.save(findlocationByCode);

        Location updatedLocation = repository.findByCode(code);

        // then
        assertThat(updatedLocation).isNotNull();
        assertThat(updatedLocation.getCode()).isEqualTo("NYC_USA");
        assertThat(updatedLocation.getCityName()).isEqualTo("New York City Updated");
        assertThat(updatedLocation.getRegionName()).isEqualTo("New York Updated");
        assertThat(updatedLocation.getCountryCode()).isEqualTo("US");
        assertThat(updatedLocation.getCountryName()).isEqualTo("United States of America Updated");
        assertThat(updatedLocation.isEnabled()).isEqualTo(true);

    }
}
