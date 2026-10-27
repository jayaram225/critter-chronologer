package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found with id: " + employeeId
                        )
                );
    }

    public Employee setAvailability(Long employeeId,
                                    Set<DayOfWeek> daysAvailable) {
        Employee employee = getEmployeeById(employeeId);
        employee.setDaysAvailable(daysAvailable);

        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAvailableEmployees(
            Set<EmployeeSkill> requestedSkills,
            LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Employee availability date must not be null"
            );
        }

        DayOfWeek requestedDay = date.getDayOfWeek();

        return employeeRepository.findAll()
                .stream()
                .filter(employee ->
                        employee.getDaysAvailable() != null
                                && employee.getDaysAvailable()
                                .contains(requestedDay)
                )
                .filter(employee ->
                        requestedSkills == null
                                || requestedSkills.isEmpty()
                                || (employee.getSkills() != null
                                && employee.getSkills()
                                .containsAll(requestedSkills))
                )
                .toList();
    }
}