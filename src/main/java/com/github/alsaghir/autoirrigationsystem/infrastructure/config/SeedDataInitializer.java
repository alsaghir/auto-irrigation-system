package com.github.alsaghir.autoirrigationsystem.infrastructure.config;

import com.github.alsaghir.autoirrigationsystem.domain.Plot;
import com.github.alsaghir.autoirrigationsystem.domain.PlotRepository;
import com.github.alsaghir.autoirrigationsystem.domain.TimeSlot;
import com.github.alsaghir.autoirrigationsystem.domain.TimeSlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class SeedDataInitializer implements CommandLineRunner {
    private final PlotRepository plotRepository;
    private final TimeSlotRepository timeSlotRepository;

    public SeedDataInitializer(PlotRepository plotRepository, TimeSlotRepository timeSlotRepository) {
        this.plotRepository = plotRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public void run(String... args) {
        Plot plot1 = Plot.builder()
                .name("Test Plot 1")
                .cultivatedArea(10.0)
                .cropType("Test Crop")
                .build();

        Plot plot2 = Plot.builder()
                .name("Test Plot 2")
                .cultivatedArea(15.0)
                .cropType("Test Crop")
                .build();

        plotRepository.saveAll(Arrays.asList(plot1, plot2));

        TimeSlot timeSlot1 = TimeSlot.builder()
                .plot(plot1)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .amountOfWater(5.0)
                .status("Pending")
                .build();

        TimeSlot timeSlot2 = TimeSlot.builder()
                .plot(plot1)
                .startTime(LocalDateTime.now().plusHours(3))
                .endTime(LocalDateTime.now().plusHours(4))
                .amountOfWater(7.0)
                .status("Pending")
                .build();

        TimeSlot timeSlot3 = TimeSlot.builder()
                .plot(plot2)
                .startTime(LocalDateTime.now().plusHours(2))
                .endTime(LocalDateTime.now().plusHours(3))
                .amountOfWater(6.0)
                .status("Pending")
                .build();

        timeSlotRepository.saveAll(Arrays.asList(timeSlot1, timeSlot2, timeSlot3));
    }
}
