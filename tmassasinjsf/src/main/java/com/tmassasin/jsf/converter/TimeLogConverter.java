package com.tmassasin.jsf.converter;
import com.tmassasin.model.TimeLog;
import com.tmassasin.service.TimeLogService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@FacesConverter("com.tmassasin.jsf.converter.TimeLogConverter")
@Configurable
public class TimeLogConverter implements javax.faces.convert.Converter, org.springframework.beans.factory.aspectj.ConfigurableObject {

	@Autowired
    TimeLogService timeLogService;

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return timeLogService.findTimeLog(id);
    }

	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof TimeLog ? ((TimeLog) value).getId().toString() : "";
    }
}
