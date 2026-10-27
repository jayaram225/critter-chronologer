package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           EmployeeRepository employeeRepository,
                           PetRepository petRepository) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    public Schedule createSchedule(Schedule schedule,
                                   List<Long> employeeIds,
                                   List<Long> petIds) {

        List<Employee> employees =
                employeeRepository.findAllById(employeeIds);

        List<Pet> pets =
                petRepository.findAllById(petIds);

        if (employees.size() != employeeIds.size()) {
            throw new IllegalArgumentException(
                    "One or more employee IDs are invalid"
            );
        }

        if (pets.size() != petIds.size()) {
            throw new IllegalArgumentException(
                    "One or more pet IDs are invalid"
            );
        }

        schedule.setEmployees(employees);
        schedule.setPets(pets);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        employees.forEach(employee ->
                employee.getSchedules().add(savedSchedule)
        );

        pets.forEach(pet ->
                pet.getSchedules().add(savedSchedule)
        );

        return savedSchedule;
    }

    @Transactional(readOnly = true)
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();

        schedules.forEach(this::initializeScheduleRelationships);

        return schedules;
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForPet(Long petId) {
        if (!petRepository.existsById(petId)) {
            throw new IllegalArgumentException(
                    "Pet not found with id: " + petId
            );
        }

        List<Schedule> schedules =
                scheduleRepository.findDistinctByPetsId(petId);

        schedules.forEach(this::initializeScheduleRelationships);

        return schedules;
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException(
                    "Employee not found with id: " + employeeId
            );
        }

        List<Schedule> schedules =
                scheduleRepository.findDistinctByEmployeesId(employeeId);

        schedules.forEach(this::initializeScheduleRelationships);

        return schedules;
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        List<Schedule> schedules =
                scheduleRepository.findDistinctByPetsOwnerId(customerId);

        schedules.forEach(this::initializeScheduleRelationships);

        return schedules;
    }
    private void initializeScheduleRelationships(Schedule schedule) {
        schedule.getEmployees().size();
        schedule.getPets().size();
        schedule.getActivities().size();
    }
}