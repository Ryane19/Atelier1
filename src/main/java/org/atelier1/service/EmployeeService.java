package org.atelier1.service;

import org.atelier1.exception.CannotDeleteAdminException;
import org.atelier1.exception.EmailAlreadyExistsException;
import org.atelier1.exception.EmployeeNotFoundException;
import org.atelier1.model.Employee;
import java.util.*;


public class EmployeeService {
    private Map<Long, Employee> employeeDatabase = new HashMap<>();
    private Long idCounter = 1L;

    public Employee addEmployee(String firstName, String lastName, String email, String position, Date hireDate) {
        if (employeeDatabase.values().stream().anyMatch(emp -> emp.getEmail().equals(email))) {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        }
        Employee employee = new Employee(idCounter++, firstName, lastName, email, position, hireDate);
        employeeDatabase.put(employee.getId(), employee);
        return employee;
    }

    public void deleteEmploye(Long id){

        Employee employee = employeeDatabase.get(id);
        if(employee == null){
            throw new EmployeeNotFoundException( id + "l'employee n'existe pas");
        }
         if(isOnlyAdmin() && employee.getPosition().equalsIgnoreCase("Admin")){
             throw new CannotDeleteAdminException("vous ne pouvez pas supprimer le dernier Admin");
         }
        employeeDatabase.remove(id);

    }

    private boolean isOnlyAdmin() {
        return employeeDatabase.values().stream()
                .filter(emp -> emp.getPosition().equalsIgnoreCase("admin"))
                .count() == 1;
    }
}
