package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository repo;

    public Location add(Location location) {
        return repo.save(location);
    }
}
