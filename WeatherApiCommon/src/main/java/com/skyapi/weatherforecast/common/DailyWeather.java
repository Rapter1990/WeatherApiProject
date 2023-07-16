package com.skyapi.weatherforecast.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "weather_daily")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DailyWeather {

    @EmbeddedId
    private DailyWeatherId id = new DailyWeatherId();

    private int minTemp;
    private int maxTemp;
    private int precipitation;

    @Column(length = 50)
    private String status;

    public DailyWeather precipitation(int precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public DailyWeather status(String status) {
        setStatus(status);
        return this;
    }

    public DailyWeather location(Location location) {
        this.id.setLocation(location);
        return this;
    }

    public DailyWeather dayOfMonth(int day) {
        this.id.setDayOfMonth(day);
        return this;
    }

    public DailyWeather month(int month) {
        this.id.setMonth(month);
        return this;
    }

    public DailyWeather minTemp(int temp) {
        setMinTemp(temp);
        return this;
    }

    public DailyWeather maxTemp(int temp) {
        setMaxTemp(temp);
        return this;
    }
}
