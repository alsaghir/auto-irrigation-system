package com.github.alsaghir.autoirrigationsystem.application;

import com.github.alsaghir.autoirrigationsystem.domain.IrrigationRequest;
import com.github.alsaghir.autoirrigationsystem.domain.Plot;
import com.github.alsaghir.autoirrigationsystem.domain.PlotRepository;
import com.github.alsaghir.autoirrigationsystem.domain.TimeSlot;
import com.github.alsaghir.autoirrigationsystem.domain.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlotService {
    private final PlotRepository plotRepository;

    private final TimeSlotRepository timeSlotRepository;


    public Plot addPlot(Plot plot) {
        return plotRepository.save(plot);
    }


    public TimeSlot configurePlot(Long plotId, TimeSlot timeSlot) {
        Plot plot = plotRepository.findById(plotId).orElseThrow(() -> new RuntimeException("Plot not found"));
        timeSlot.setPlot(plot);
        return timeSlotRepository.save(timeSlot);
    }


    public Plot editPlot(Long plotId, Plot plot) {
        Plot existingPlot = plotRepository.findById(plotId).orElseThrow(() -> new RuntimeException("Plot not found"));
        existingPlot.setName(plot.getName());
        existingPlot.setCultivatedArea(plot.getCultivatedArea());
        existingPlot.setCropType(plot.getCropType());
        return plotRepository.save(existingPlot);
    }


    public List<Plot> listPlots() {
        return plotRepository.findAll();
    }


    public TimeSlot irrigatePlot(Long plotId, IrrigationRequest request) {
        plotRepository.findById(plotId)
                .orElseThrow(() -> new RuntimeException("Plot not found"));
        TimeSlot slot = timeSlotRepository.findById(request.slotId()).
                orElseThrow(() -> new RuntimeException("Time slot not found"));
        // integrate with the sensor device and update the slot status
        boolean success = integrateWithSensorDevice(request);
        if (success) {
            slot.setStatus("Irrigated");
            timeSlotRepository.save(slot);
        } else {
            // retry the integration
            boolean retrySuccess = retryIntegration(request);
            if (retrySuccess) {
                slot.setStatus("Irrigated");
                timeSlotRepository.save(slot);
            } else {
                // alert the failure
                alertFailure(request);
            }
        }
        return slot;
    }

    private boolean integrateWithSensorDevice(IrrigationRequest request) {
        // integrate with the sensor device using the request data
        // return true or false based on the integration success
        return true;
    }

    private boolean retryIntegration(IrrigationRequest request) {
        // retry the integration with the sensor device based on the pre-configured number of retries
        // return true or false based on the integration success
        return false;
    }

    private void alertFailure(IrrigationRequest request) {
        // alert the system admin or user about the integration failure after exceeding the retry times
        // send an email or a notification to the relevant parties
    }


    public List<TimeSlot> getPlotTimeSlots(Long plotId) {
        Plot plot = plotRepository.findById(plotId).orElseThrow(() -> new RuntimeException("Plot not found"));
        return timeSlotRepository.findByPlot(plot);
    }

    // bonus points: predict the amount of water for a given crop and cultivated area

    public Double predictWaterRequirement(String cropType, Double cultivatedArea) {
        // use a machine learning model or a pre-defined formula to predict the water requirement
        // for the given crop and cultivated area
        return 0.0;
    }
}
