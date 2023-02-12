package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    public List<Location> findUntrashed();

    @Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code = ?1")
    public Location findByCode(String code);
}
