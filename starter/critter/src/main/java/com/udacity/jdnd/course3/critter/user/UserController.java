package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to customers and employees.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetService petService;

    public UserController(CustomerService customerService,
                          EmployeeService employeeService,
                          PetService petService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        Customer savedCustomer = customerService.saveCustomer(customer);

        return convertCustomerEntityToDTO(savedCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers()
                .stream()
                .map(this::convertCustomerEntityToDTO)
                .toList();
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);

        return convertCustomerEntityToDTO(pet.getOwner());
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        Employee savedEmployee = employeeService.saveEmployee(employee);

        return convertEmployeeEntityToDTO(savedEmployee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee =
                employeeService.getEmployeeById(employeeId);

        return convertEmployeeEntityToDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(
            @RequestBody Set<DayOfWeek> daysAvailable,
            @PathVariable long employeeId) {

        employeeService.setAvailability(employeeId, daysAvailable);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(
            @RequestBody EmployeeRequestDTO employeeDTO) {

        return employeeService.findAvailableEmployees(
                        employeeDTO.getSkills(),
                        employeeDTO.getDate()
                )
                .stream()
                .map(this::convertEmployeeEntityToDTO)
                .toList();
    }

    private Customer convertCustomerDTOToEntity(
            CustomerDTO customerDTO) {

        Customer customer = new Customer();

        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());

        return customer;
    }

    private CustomerDTO convertCustomerEntityToDTO(
            Customer customer) {

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());

        if (customer.getPets() == null) {
            customerDTO.setPetIds(Collections.emptyList());
        } else {
            customerDTO.setPetIds(
                    customer.getPets()
                            .stream()
                            .map(Pet::getId)
                            .toList()
            );
        }

        return customerDTO;
    }

    private Employee convertEmployeeDTOToEntity(
            EmployeeDTO employeeDTO) {

        Employee employee = new Employee();

        employee.setName(employeeDTO.getName());

        if (employeeDTO.getSkills() != null) {
            employee.setSkills(employeeDTO.getSkills());
        }

        if (employeeDTO.getDaysAvailable() != null) {
            employee.setDaysAvailable(
                    employeeDTO.getDaysAvailable()
            );
        }

        return employee;
    }

    private EmployeeDTO convertEmployeeEntityToDTO(
            Employee employee) {

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());

        if (employee.getDaysAvailable() == null
                || employee.getDaysAvailable().isEmpty()) {
            employeeDTO.setDaysAvailable(null);
        } else {
            employeeDTO.setDaysAvailable(
                    employee.getDaysAvailable()
            );
        }

        return employeeDTO;
    }
}