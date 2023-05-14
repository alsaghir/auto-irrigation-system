package com.github.alsaghir.autoirrigationsystem.presentation;

import com.github.alsaghir.autoirrigationsystem.application.PlotService;
import com.github.alsaghir.autoirrigationsystem.domain.IrrigationRequest;
import com.github.alsaghir.autoirrigationsystem.domain.Plot;
import com.github.alsaghir.autoirrigationsystem.domain.TimeSlot;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/land/plots")
public class PlotController {


    private final PlotService plotService;

    @PostMapping("")
    public Plot addPlot(@RequestBody Plot plot) {
        return plotService.addPlot(plot);
    }

    @PostMapping("/{plotId}")
    public TimeSlot configurePlot(@PathVariable Long plotId, @RequestBody TimeSlot timeSlot) {
        return plotService.configurePlot(plotId, timeSlot);
    }

    @PutMapping("/{plotId}")
    public Plot editPlot(@PathVariable Long plotId, @RequestBody Plot plot) {
        return plotService.editPlot(plotId, plot);
    }

    @GetMapping("")
    public List<Plot> listPlots() {
        return plotService.listPlots();
    }

    @PostMapping("/{plotId}/irrigate")
    public TimeSlot irrigatePlot(@PathVariable Long plotId, @RequestBody IrrigationRequest request) {
        return plotService.irrigatePlot(plotId, request);
    }

    @GetMapping("/{plotId}/timeslots")
    public List<TimeSlot> getPlotTimeSlots(@PathVariable Long plotId) {
        return plotService.getPlotTimeSlots(plotId);
    }

    @GetMapping("/predict-water-requirement")
    public Double predictWaterRequirement(@RequestParam String cropType, @RequestParam Double cultivatedArea) {
        return plotService.predictWaterRequirement(cropType, cultivatedArea);
    }
}
