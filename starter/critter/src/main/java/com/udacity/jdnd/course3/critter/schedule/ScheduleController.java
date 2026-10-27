package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(
            @RequestBody ScheduleDTO scheduleDTO) {

        Schedule schedule = convertDTOToEntity(scheduleDTO);

        Schedule savedSchedule = scheduleService.createSchedule(
                schedule,
                scheduleDTO.getEmployeeIds(),
                scheduleDTO.getPetIds()
        );

        return convertEntityToDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules()
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(
            @PathVariable long petId) {

        return scheduleService.getSchedulesForPet(petId)
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(
            @PathVariable long employeeId) {

        return scheduleService.getSchedulesForEmployee(employeeId)
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(
            @PathVariable long customerId) {

        return scheduleService.getSchedulesForCustomer(customerId)
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    private Schedule convertDTOToEntity(
            ScheduleDTO scheduleDTO) {

        Schedule schedule = new Schedule();

        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        return schedule;
    }

    private ScheduleDTO convertEntityToDTO(
            Schedule schedule) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());

        scheduleDTO.setEmployeeIds(
                schedule.getEmployees()
                        .stream()
                        .map(Employee::getId)
                        .toList()
        );

        scheduleDTO.setPetIds(
                schedule.getPets()
                        .stream()
                        .map(Pet::getId)
                        .toList()
        );

        return scheduleDTO;
    }
}