package com.tmassasin.service;

import com.tmassasin.model.Employee;

import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long countAllEmployees() {
        return Employee.countEmployees();
    }

	public void deleteEmployee(Employee employee) {
        employee.remove();
    }

	public Employee findEmployee(Long id) {
        return Employee.findEmployee(id);
    }

	public List<Employee> findAllEmployees() {
        return Employee.findAllEmployees();
    }

	public List<Employee> findEmployeeEntries(int firstResult, int maxResults) {
        return Employee.findEmployeeEntries(firstResult, maxResults);
    }

	public void saveEmployee(Employee employee) {
        employee.persist();
    }

	public Employee updateEmployee(Employee employee) {
        return employee.merge();
    }
}
