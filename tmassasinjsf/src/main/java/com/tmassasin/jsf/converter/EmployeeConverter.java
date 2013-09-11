package com.tmassasin.jsf.converter;
import com.tmassasin.model.Employee;
import com.tmassasin.service.EmployeeService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@FacesConverter("com.tmassasin.jsf.converter.EmployeeConverter")
@Configurable
public class EmployeeConverter implements javax.faces.convert.Converter, org.springframework.beans.factory.aspectj.ConfigurableObject {

	@Autowired
    EmployeeService employeeService;

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return employeeService.findEmployee(id);
    }

	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Employee ? ((Employee) value).getId().toString() : "";
    }
}
