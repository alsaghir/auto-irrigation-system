package com.github.alsaghir.autoirrigationsystem.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alsaghir.autoirrigationsystem.domain.IrrigationRequest;
import com.github.alsaghir.autoirrigationsystem.domain.Plot;
import com.github.alsaghir.autoirrigationsystem.domain.PlotRepository;
import com.github.alsaghir.autoirrigationsystem.domain.TimeSlot;
import com.github.alsaghir.autoirrigationsystem.domain.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlotRepository plotRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        timeSlotRepository.deleteAll();
        plotRepository.deleteAll();
        // objectMapper.registerModules(new JavaTimeModule());
    }

    @Test
    public void testAddPlot() throws Exception {
        Plot plot = Plot.builder().name("Test Plot").cultivatedArea(10.0).cropType("Test Crop").build();

        String requestBody = objectMapper.writeValueAsString(plot);
        mockMvc.perform(post("/api/v1/land/plots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(plot.getName()))
                .andExpect(jsonPath("$.cultivatedArea").value(plot.getCultivatedArea()))
                .andExpect(jsonPath("$.cropType").value(plot.getCropType()));
    }

    @Test
    public void testConfigurePlot() throws Exception {
        Plot plot = plotRepository.save(Plot.builder().name("Test Plot").cultivatedArea(10.0).cropType("Test Crop").build());

        TimeSlot timeSlot = TimeSlot.builder()
                .plot(plot)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .amountOfWater(5.0)
                .status("Pending")
                .build();

        String requestBody = objectMapper.writeValueAsString(timeSlot);
        mockMvc.perform(post("/api/v1/land/plots/{plotId}", plot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.plot.id").value(plot.getId()))
                .andExpect(jsonPath("$.startTime").value(timeSlot.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.endTime").value(timeSlot.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.amountOfWater").value(timeSlot.getAmountOfWater()))
                .andExpect(jsonPath("$.status").value(timeSlot.getStatus()));
    }

    @Test
    public void testEditPlot() throws Exception {
        Plot plot = plotRepository.save(Plot.builder()
                .name("Test Plot")
                .cultivatedArea(10.0)
                .cropType("Test Crop")
                .build());
        Plot updatedPlot = Plot.builder()
                .name("Updated Plot")
                .cultivatedArea(10.0)
                .cropType("Updated Crop")
                .build();

        String requestBody = objectMapper.writeValueAsString(updatedPlot);
        mockMvc.perform(put("/api/v1/land/plots/{plotId}", plot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(plot.getId()))
                .andExpect(jsonPath("$.name").value(updatedPlot.getName()))
                .andExpect(jsonPath("$.cultivatedArea").value(updatedPlot.getCultivatedArea()))
                .andExpect(jsonPath("$.cropType").value(updatedPlot.getCropType()));
    }

    @Test
    public void testListPlots() throws Exception {
        List<Plot> plots = plotRepository.saveAll(Arrays.asList(
                Plot.builder()
                        .name("Test Plot 1")
                        .cultivatedArea(10.0)
                        .cropType("Test Crop")
                        .build(),
                Plot.builder()
                        .name("Test Plot 2")
                        .cultivatedArea(15.0)
                        .cropType("Test Crop")
                        .build()
        )).stream().toList();
        mockMvc.perform(get("/api/v1/land/plots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(plots.size())))
                .andExpect(jsonPath("$[0].name").value(plots.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(plots.get(1).getName()));
    }

    @Test
    public void testIrrigatePlot() throws Exception {
        Plot plot = plotRepository.save(Plot.builder()
                .name("Test Plot")
                .cultivatedArea(10.0)
                .cropType("Test Crop")
                .build());

        TimeSlot timeSlot = timeSlotRepository.save(TimeSlot.builder()
                .plot(plot)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .amountOfWater(5.0)
                .status("Pending")
                .build());
        IrrigationRequest request = new IrrigationRequest(timeSlot.getId());
        String requestBody = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/land/plots/" + plot.getId() + "/irrigate", plot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(timeSlot.getId()))
                .andExpect(jsonPath("$.plot.id").value(plot.getId()))
                .andExpect(jsonPath("$.status").value("Irrigated"));
    }

    @Test
    public void testGetPlotTimeSlots() throws Exception {
        Plot plot = plotRepository.save(Plot.builder()
                .name("Test Plot")
                .cultivatedArea(10.0)
                .cropType("Test Crop")
                .build());

        List<TimeSlot> timeSlots = timeSlotRepository.saveAll(Arrays.asList(
                TimeSlot.builder()
                        .plot(plot)
                        .startTime(LocalDateTime.now().plusHours(1))
                        .endTime(LocalDateTime.now().plusHours(2))
                        .amountOfWater(5.0)
                        .status("Pending")
                        .build(),
                TimeSlot.builder()
                        .plot(plot)
                        .startTime(LocalDateTime.now().plusHours(3))
                        .endTime(LocalDateTime.now().plusHours(4))
                        .amountOfWater(7.0)
                        .status("Pending")
                        .build()
        )).stream().toList();
        mockMvc.perform(get("/api/v1/land/plots/{plotId}/timeslots", plot.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(timeSlots.size())))
                .andExpect(jsonPath("$[0].id").value(timeSlots.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(timeSlots.get(1).getId()));
    }

    @Test
    public void testPredictWaterRequirement() throws Exception {
        String cropType = "Test Crop";
        double cultivatedArea = 10.0;
        mockMvc.perform(get("/api/v1/land/plots/predict-water-requirement")
                        .param("cropType", cropType)
                        .param("cultivatedArea", Double.toString(cultivatedArea)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber());
    }
}
