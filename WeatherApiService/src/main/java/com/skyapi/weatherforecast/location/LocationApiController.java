package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/locations")
@RequiredArgsConstructor
public class LocationApiController {

    private final LocationService service;

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody @Valid Location location) {
        Location addedLocation = service.add(location);
        URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

        return ResponseEntity.created(uri).body(addedLocation);
    }
}
