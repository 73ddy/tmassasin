package com.tmassasin.jsf.converter;
import com.tmassasin.model.WorkLog;
import com.tmassasin.service.WorkLogService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@FacesConverter("com.tmassasin.jsf.converter.WorkLogConverter")
public class WorkLogConverter implements javax.faces.convert.Converter, org.springframework.beans.factory.aspectj.ConfigurableObject {

	@Autowired
    WorkLogService workLogService;

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return workLogService.findWorkLog(id);
    }

	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof WorkLog ? ((WorkLog) value).getId().toString() : "";
    }
}
