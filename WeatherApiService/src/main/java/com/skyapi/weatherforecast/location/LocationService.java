package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository repo;

    public Location add(Location location) {
        return repo.save(location);
    }

    public List<Location> list() {
        return repo.findUntrashed();
    }

    public Location get(String code) {
        return repo.findByCode(code);
    }
}
