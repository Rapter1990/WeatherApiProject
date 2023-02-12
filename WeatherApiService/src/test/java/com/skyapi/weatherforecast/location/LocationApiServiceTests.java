package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.base.BaseServiceTest;
import com.skyapi.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class LocationApiServiceTests extends BaseServiceTest {

    @InjectMocks
    private LocationService service;

    @Mock
    private LocationRepository locationRepository;


    @Test
    public void shouldReturnLocationWhenNewLocation(){

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
        when(locationRepository.save(location)).thenReturn(location);

        // then
        Location savedLocation = service.add(location);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
        assertThat(savedLocation.getCityName()).isEqualTo("New York City");
        assertThat(savedLocation.getRegionName()).isEqualTo("New York");
        assertThat(savedLocation.getCountryCode()).isEqualTo("US");
        assertThat(savedLocation.getCountryName()).isEqualTo("United States of America");
        assertThat(savedLocation.isEnabled()).isEqualTo(true);
    }

}
