package com.tmassasin.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;

import com.tmassasin.jsf.constants.JSFConstants;
import com.tmassasin.jsf.converter.EmployeeConverter;
import com.tmassasin.jsf.util.MessageFactory;
import com.tmassasin.model.Employee;
import com.tmassasin.model.TimeLog;
import com.tmassasin.service.EmployeeService;
import com.tmassasin.service.TimeLogService;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@ManagedBean(name = "timeLogBean")
@SessionScoped
@Configurable
public class TimeLogBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Autowired
    TimeLogService timeLogService;

	@Autowired
    EmployeeService employeeService;

	private String name = "TimeLogs";

	private TimeLog timeLog;

	private List<TimeLog> allTimeLogs;

	private boolean dataVisible = false;

	private List<String> columns;

	private transient HtmlPanelGrid createPanelGrid;

	private transient HtmlPanelGrid editPanelGrid;

	private transient HtmlPanelGrid viewPanelGrid;

	private boolean createDialogVisible = false;

	@PostConstruct
    public void init() {
        columns = new ArrayList<String>();
        columns.add("employee");
        columns.add("entryTime");
        columns.add("exitTime");
        columns.add("dayOfLog");
    }

	public String getName() {
        return name;
    }

	public List<String> getColumns() {
        return columns;
    }

	public List<TimeLog> getAllTimeLogs() {
        return allTimeLogs;
    }

	public void setAllTimeLogs(List<TimeLog> allTimeLogs) {
        this.allTimeLogs = allTimeLogs;
    }

	public String findAllTimeLogs() {
        allTimeLogs = timeLogService.findAllTimeLogs();
        dataVisible = !allTimeLogs.isEmpty();
        return null;
    }
	
	public boolean isDataVisible() {
        return dataVisible;
    }

	public void setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }

	public HtmlPanelGrid getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }

	public void setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }

	public HtmlPanelGrid getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }

	public void setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }

	public HtmlPanelGrid getViewPanelGrid() {
        return populateViewPanel();
    }

	public void setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }

	public HtmlPanelGrid populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel employeeCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        employeeCreateOutput.setFor("employeeCreateInput");
        employeeCreateOutput.setId("employeeCreateOutput");
        employeeCreateOutput.setValue("Employee:");
        htmlPanelGrid.getChildren().add(employeeCreateOutput);
        
        AutoComplete employeeCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        employeeCreateInput.setId("employeeCreateInput");
        employeeCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.employee}", Employee.class));
        employeeCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{timeLogBean.completeEmployee}", List.class, new Class[] { String.class }));
        employeeCreateInput.setDropdown(true);
        employeeCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "employee", String.class));
        employeeCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{employee.username}", String.class));
        employeeCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{employee}", Employee.class));
        employeeCreateInput.setConverter(new EmployeeConverter());
        employeeCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(employeeCreateInput);
        
        Message employeeCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        employeeCreateInputMessage.setId("employeeCreateInputMessage");
        employeeCreateInputMessage.setFor("employeeCreateInput");
        employeeCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(employeeCreateInputMessage);
        
        OutputLabel entryTimeCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        entryTimeCreateOutput.setFor("entryTimeCreateInput");
        entryTimeCreateOutput.setId("entryTimeCreateOutput");
        entryTimeCreateOutput.setValue("Entry Time:");
        htmlPanelGrid.getChildren().add(entryTimeCreateOutput);
        
        Calendar entryTimeCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        entryTimeCreateInput.setId("entryTimeCreateInput");
        entryTimeCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.entryTime}", Date.class));
        entryTimeCreateInput.setNavigator(true);
        entryTimeCreateInput.setEffect("slideDown");
        entryTimeCreateInput.setTimeOnly(true);
        entryTimeCreateInput.setPattern(JSFConstants.TIME_ONLY_FORMAT);
        entryTimeCreateInput.setRequired(false);
        entryTimeCreateInput.setStepMinute(10);
        htmlPanelGrid.getChildren().add(entryTimeCreateInput);
        
        Message entryTimeCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        entryTimeCreateInputMessage.setId("entryTimeCreateInputMessage");
        entryTimeCreateInputMessage.setFor("entryTimeCreateInput");
        entryTimeCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(entryTimeCreateInputMessage);
        
        OutputLabel exitTimeCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        exitTimeCreateOutput.setFor("exitTimeCreateInput");
        exitTimeCreateOutput.setId("exitTimeCreateOutput");
        exitTimeCreateOutput.setValue("Exit Time:");
        htmlPanelGrid.getChildren().add(exitTimeCreateOutput);
        
        Calendar exitTimeCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        exitTimeCreateInput.setId("exitTimeCreateInput");
        exitTimeCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.exitTime}", Date.class));
        exitTimeCreateInput.setNavigator(true);
        exitTimeCreateInput.setEffect("slideDown");
        exitTimeCreateInput.setTimeOnly(true);
        exitTimeCreateInput.setPattern(JSFConstants.TIME_ONLY_FORMAT);
        exitTimeCreateInput.setStepMinute(10);
        exitTimeCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(exitTimeCreateInput);
        
        Message exitTimeCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        exitTimeCreateInputMessage.setId("exitTimeCreateInputMessage");
        exitTimeCreateInputMessage.setFor("exitTimeCreateInput");
        exitTimeCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(exitTimeCreateInputMessage);
        
        OutputLabel dayOfLogCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        dayOfLogCreateOutput.setFor("dayOfLogCreateInput");
        dayOfLogCreateOutput.setId("dayOfLogCreateOutput");
        dayOfLogCreateOutput.setValue("Day Of Log:");
        htmlPanelGrid.getChildren().add(dayOfLogCreateOutput);
        
        Calendar dayOfLogCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        dayOfLogCreateInput.setId("dayOfLogCreateInput");
        dayOfLogCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.dayOfLog}", Date.class));
        dayOfLogCreateInput.setNavigator(true);
        dayOfLogCreateInput.setEffect("slideDown");
        dayOfLogCreateInput.setPattern(JSFConstants.DATE_ONLY_FORMAT);
        dayOfLogCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(dayOfLogCreateInput);
        
        Message dayOfLogCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        dayOfLogCreateInputMessage.setId("dayOfLogCreateInputMessage");
        dayOfLogCreateInputMessage.setFor("dayOfLogCreateInput");
        dayOfLogCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(dayOfLogCreateInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel employeeEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        employeeEditOutput.setFor("employeeEditInput");
        employeeEditOutput.setId("employeeEditOutput");
        employeeEditOutput.setValue("Employee:");
        htmlPanelGrid.getChildren().add(employeeEditOutput);
        
        AutoComplete employeeEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        employeeEditInput.setId("employeeEditInput");
        employeeEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.employee}", Employee.class));
        employeeEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{timeLogBean.completeEmployee}", List.class, new Class[] { String.class }));
        employeeEditInput.setDropdown(true);
        employeeEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "employee", String.class));
        employeeEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{employee.username}", String.class));
        employeeEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{employee}", Employee.class));
        employeeEditInput.setConverter(new EmployeeConverter());
        employeeEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(employeeEditInput);
        
        Message employeeEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        employeeEditInputMessage.setId("employeeEditInputMessage");
        employeeEditInputMessage.setFor("employeeEditInput");
        employeeEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(employeeEditInputMessage);
        
        OutputLabel entryTimeEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        entryTimeEditOutput.setFor("entryTimeEditInput");
        entryTimeEditOutput.setId("entryTimeEditOutput");
        entryTimeEditOutput.setValue("Entry Time:");
        htmlPanelGrid.getChildren().add(entryTimeEditOutput);
        
        Calendar entryTimeEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        entryTimeEditInput.setId("entryTimeEditInput");
        entryTimeEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.entryTime}", Date.class));
        entryTimeEditInput.setNavigator(true);
        entryTimeEditInput.setEffect("slideDown");
        entryTimeEditInput.setPattern(JSFConstants.TIME_ONLY_FORMAT);
        entryTimeEditInput.setTimeOnly(true);
        entryTimeEditInput.setStepMinute(10);
        entryTimeEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(entryTimeEditInput);
        
        Message entryTimeEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        entryTimeEditInputMessage.setId("entryTimeEditInputMessage");
        entryTimeEditInputMessage.setFor("entryTimeEditInput");
        entryTimeEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(entryTimeEditInputMessage);
        
        OutputLabel exitTimeEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        exitTimeEditOutput.setFor("exitTimeEditInput");
        exitTimeEditOutput.setId("exitTimeEditOutput");
        exitTimeEditOutput.setValue("Exit Time:");
        htmlPanelGrid.getChildren().add(exitTimeEditOutput);
        
        Calendar exitTimeEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        exitTimeEditInput.setId("exitTimeEditInput");
        exitTimeEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.exitTime}", Date.class));
        exitTimeEditInput.setNavigator(true);
        exitTimeEditInput.setEffect("slideDown");
        exitTimeEditInput.setPattern(JSFConstants.TIME_ONLY_FORMAT);
        exitTimeEditInput.setTimeOnly(true);
        exitTimeEditInput.setStepMinute(10);
        exitTimeEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(exitTimeEditInput);
        
        Message exitTimeEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        exitTimeEditInputMessage.setId("exitTimeEditInputMessage");
        exitTimeEditInputMessage.setFor("exitTimeEditInput");
        exitTimeEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(exitTimeEditInputMessage);
        
        OutputLabel dayOfLogEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        dayOfLogEditOutput.setFor("dayOfLogEditInput");
        dayOfLogEditOutput.setId("dayOfLogEditOutput");
        dayOfLogEditOutput.setValue("Day Of Log:");
        htmlPanelGrid.getChildren().add(dayOfLogEditOutput);
        
        Calendar dayOfLogEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        dayOfLogEditInput.setId("dayOfLogEditInput");
        dayOfLogEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.dayOfLog}", Date.class));
        dayOfLogEditInput.setNavigator(true);
        dayOfLogEditInput.setEffect("slideDown");
        dayOfLogEditInput.setPattern(JSFConstants.DATE_ONLY_FORMAT);
        dayOfLogEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(dayOfLogEditInput);
        
        Message dayOfLogEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        dayOfLogEditInputMessage.setId("dayOfLogEditInputMessage");
        dayOfLogEditInputMessage.setFor("dayOfLogEditInput");
        dayOfLogEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(dayOfLogEditInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText employeeLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        employeeLabel.setId("employeeLabel");
        employeeLabel.setValue("Employee:");
        htmlPanelGrid.getChildren().add(employeeLabel);
        
        HtmlOutputText employeeValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        employeeValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.employee}", Employee.class));
        employeeValue.setConverter(new EmployeeConverter());
        htmlPanelGrid.getChildren().add(employeeValue);
        
        HtmlOutputText entryTimeLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        entryTimeLabel.setId("entryTimeLabel");
        entryTimeLabel.setValue("Entry Time:");
        htmlPanelGrid.getChildren().add(entryTimeLabel);
        
        HtmlOutputText entryTimeValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        entryTimeValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.entryTime}", Date.class));
        DateTimeConverter entryTimeValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        entryTimeValueConverter.setPattern(JSFConstants.TIME_ONLY_FORMAT);
        entryTimeValue.setConverter(entryTimeValueConverter);
        htmlPanelGrid.getChildren().add(entryTimeValue);
        
        HtmlOutputText exitTimeLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        exitTimeLabel.setId("exitTimeLabel");
        exitTimeLabel.setValue("Exit Time:");
        htmlPanelGrid.getChildren().add(exitTimeLabel);
        
        HtmlOutputText exitTimeValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        exitTimeValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.exitTime}", Date.class));
        DateTimeConverter exitTimeValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        exitTimeValueConverter.setPattern(JSFConstants.TIME_ONLY_FORMAT);
        exitTimeValue.setConverter(exitTimeValueConverter);
        htmlPanelGrid.getChildren().add(exitTimeValue);
        
        HtmlOutputText dayOfLogLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        dayOfLogLabel.setId("dayOfLogLabel");
        dayOfLogLabel.setValue("Day Of Log:");
        htmlPanelGrid.getChildren().add(dayOfLogLabel);
        
        HtmlOutputText dayOfLogValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        dayOfLogValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{timeLogBean.timeLog.dayOfLog}", Date.class));
        DateTimeConverter dayOfLogValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        dayOfLogValueConverter.setPattern(JSFConstants.DATE_ONLY_FORMAT);
        dayOfLogValue.setConverter(dayOfLogValueConverter);
        htmlPanelGrid.getChildren().add(dayOfLogValue);
        
        return htmlPanelGrid;
    }

	public TimeLog getTimeLog() {
        if (timeLog == null) {
            timeLog = new TimeLog();
        }
        return timeLog;
    }

	public void setTimeLog(TimeLog timeLog) {
        this.timeLog = timeLog;
    }

	public List<Employee> completeEmployee(String query) {
        List<Employee> suggestions = new ArrayList<Employee>();
        for (Employee employee : employeeService.findAllEmployees()) {
            String employeeStr = String.valueOf(employee.getUsername());
            if (employeeStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(employee);
            }
        }
        return suggestions;
    }

	public String onEdit() {
        return null;
    }

	public boolean isCreateDialogVisible() {
        return createDialogVisible;
    }

	public void setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }

	public String displayList() {
        createDialogVisible = false;
        findAllTimeLogs();
        return "timeLog";
    }

	public String displayCreateDialog() {
        timeLog = new TimeLog();
        createDialogVisible = true;
        return "timeLog";
    }

	public String persist() {
        String message = "";
        java.util.Calendar dayOfLogCalendar = java.util.Calendar
				.getInstance();
		dayOfLogCalendar.setTime(timeLog.getDayOfLog());
        if (null != timeLog.getEntryTime()) {
			java.util.Calendar entryTimeCalendar = java.util.Calendar
					.getInstance();
			entryTimeCalendar.setTime(timeLog.getEntryTime());
			entryTimeCalendar.set(
					dayOfLogCalendar.get(java.util.Calendar.YEAR),
					dayOfLogCalendar.get(java.util.Calendar.MONTH),
					dayOfLogCalendar.get(java.util.Calendar.DAY_OF_MONTH));
			timeLog.setEntryTime(entryTimeCalendar.getTime());
        }
        if (null != timeLog.getExitTime()) {
			java.util.Calendar exitTimeCalendar = java.util.Calendar
					.getInstance();
			exitTimeCalendar.setTime(timeLog.getExitTime());
			exitTimeCalendar.set(
					dayOfLogCalendar.get(java.util.Calendar.YEAR),
					dayOfLogCalendar.get(java.util.Calendar.MONTH),
					dayOfLogCalendar.get(java.util.Calendar.DAY_OF_MONTH));
			timeLog.setExitTime(exitTimeCalendar.getTime());
        }
        if (timeLog.getId() != null) {
            timeLogService.updateTimeLog(timeLog);
            message = "message_successfully_updated";
        } else {
            timeLogService.saveTimeLog(timeLog);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "TimeLog");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTimeLogs();
    }

	public String delete() {
        timeLogService.deleteTimeLog(timeLog);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "TimeLog");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllTimeLogs();
    }

	public void reset() {
        timeLog = null;
        createDialogVisible = false;
    }

	public void handleDialogClose(CloseEvent event) {
        reset();
    }
}
