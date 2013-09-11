package com.tmassasin.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
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
import javax.faces.validator.LengthValidator;
import javax.faces.validator.RegexValidator;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.tmassasin.jsf.constants.JSFConstants;
import com.tmassasin.jsf.util.MessageFactory;
import com.tmassasin.model.Employee;
import com.tmassasin.model.TimeLog;
import com.tmassasin.model.WorkLog;
import com.tmassasin.service.EmployeeService;

@ManagedBean(name = "employeeBean")
@SessionScoped
@Configurable
public class EmployeeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2989763587895842793L;

	@Autowired
    EmployeeService employeeService;

	private String name = "Employees";

	private Employee employee;

	private List<Employee> allEmployees;

	private boolean dataVisible = false;

	private List<String> columns;

	private transient HtmlPanelGrid createPanelGrid;

	private transient HtmlPanelGrid editPanelGrid;

	private transient HtmlPanelGrid viewPanelGrid;

	private boolean createDialogVisible = false;

	private List<TimeLog> selectedTimeLogs;

	private List<WorkLog> selectedWorkLogs;

	@PostConstruct
    public void init() {
        columns = new ArrayList<String>();
        columns.add("username");
        columns.add("email");
    }

	public String getName() {
        return name;
    }

	public List<String> getColumns() {
        return columns;
    }

	public List<Employee> getAllEmployees() {
        return allEmployees;
    }

	public void setAllEmployees(List<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }

	public String findAllEmployees() {
        allEmployees = employeeService.findAllEmployees();
        dataVisible = !allEmployees.isEmpty();
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
        
        OutputLabel usernameCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        usernameCreateOutput.setFor("usernameCreateInput");
        usernameCreateOutput.setId("usernameCreateOutput");
        usernameCreateOutput.setValue("Username:");
        htmlPanelGrid.getChildren().add(usernameCreateOutput);
        
        InputText usernameCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        usernameCreateInput.setId("usernameCreateInput");
        usernameCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.username}", String.class));
        LengthValidator usernameCreateInputValidator = new LengthValidator();
        usernameCreateInputValidator.setMaximum(30);
        usernameCreateInput.addValidator(usernameCreateInputValidator);
        usernameCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(usernameCreateInput);
        
        Message usernameCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        usernameCreateInputMessage.setId("usernameCreateInputMessage");
        usernameCreateInputMessage.setFor("usernameCreateInput");
        usernameCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(usernameCreateInputMessage);
        
        OutputLabel passwordCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        passwordCreateOutput.setFor("passwordCreateInput");
        passwordCreateOutput.setId("passwordCreateOutput");
        passwordCreateOutput.setValue("Password:");
        htmlPanelGrid.getChildren().add(passwordCreateOutput);
        
        InputText passwordCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        passwordCreateInput.setId("passwordCreateInput");
        passwordCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.password}", String.class));
        LengthValidator passwordCreateInputValidator = new LengthValidator();
        passwordCreateInputValidator.setMaximum(30);
        passwordCreateInput.addValidator(passwordCreateInputValidator);
        passwordCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(passwordCreateInput);
        
        Message passwordCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        passwordCreateInputMessage.setId("passwordCreateInputMessage");
        passwordCreateInputMessage.setFor("passwordCreateInput");
        passwordCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(passwordCreateInputMessage);
        
        OutputLabel emailCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        emailCreateOutput.setFor("emailCreateInput");
        emailCreateOutput.setId("emailCreateOutput");
        emailCreateOutput.setValue("Email:");
        htmlPanelGrid.getChildren().add(emailCreateOutput);
        
        InputText emailCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        emailCreateInput.setId("emailCreateInput");
        emailCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.email}", String.class));
        LengthValidator emailCreateInputValidator = new LengthValidator();
        emailCreateInputValidator.setMaximum(50);
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setPattern(JSFConstants.EMAIL_PATTERN);
        emailCreateInput.addValidator(emailCreateInputValidator);
        emailCreateInput.addValidator(regexValidator);
        emailCreateInput.setValidatorMessage(JSFConstants.INVALID_EMAIL_MSG);
        emailCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(emailCreateInput);
        
        Message emailCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        emailCreateInputMessage.setId("emailCreateInputMessage");
        emailCreateInputMessage.setFor("emailCreateInput");
        emailCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(emailCreateInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText timeLogsEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        timeLogsEditOutput.setId("timeLogsEditOutput");
        timeLogsEditOutput.setValue("Time Logs:");
        htmlPanelGrid.getChildren().add(timeLogsEditOutput);
        
        HtmlOutputText timeLogsEditInput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        timeLogsEditInput.setId("timeLogsEditInput");
        timeLogsEditInput.setValue("This relationship is managed from the TimeLog side");
        htmlPanelGrid.getChildren().add(timeLogsEditInput);
        
        Message timeLogsEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        timeLogsEditInputMessage.setId("timeLogsEditInputMessage");
        timeLogsEditInputMessage.setFor("timeLogsEditInput");
        timeLogsEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(timeLogsEditInputMessage);
        
        HtmlOutputText workLogsEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        workLogsEditOutput.setId("workLogsEditOutput");
        workLogsEditOutput.setValue("Work Logs:");
        htmlPanelGrid.getChildren().add(workLogsEditOutput);
        
        HtmlOutputText workLogsEditInput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        workLogsEditInput.setId("workLogsEditInput");
        workLogsEditInput.setValue("This relationship is managed from the WorkLog side");
        htmlPanelGrid.getChildren().add(workLogsEditInput);
        
        Message workLogsEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        workLogsEditInputMessage.setId("workLogsEditInputMessage");
        workLogsEditInputMessage.setFor("workLogsEditInput");
        workLogsEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(workLogsEditInputMessage);
        
        OutputLabel usernameEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        usernameEditOutput.setFor("usernameEditInput");
        usernameEditOutput.setId("usernameEditOutput");
        usernameEditOutput.setValue("Username:");
        htmlPanelGrid.getChildren().add(usernameEditOutput);
        
        InputText usernameEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        usernameEditInput.setId("usernameEditInput");
        usernameEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.username}", String.class));
        LengthValidator usernameEditInputValidator = new LengthValidator();
        usernameEditInputValidator.setMaximum(30);
        usernameEditInput.addValidator(usernameEditInputValidator);
        usernameEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(usernameEditInput);
        
        Message usernameEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        usernameEditInputMessage.setId("usernameEditInputMessage");
        usernameEditInputMessage.setFor("usernameEditInput");
        usernameEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(usernameEditInputMessage);
        
        OutputLabel passwordEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        passwordEditOutput.setFor("passwordEditInput");
        passwordEditOutput.setId("passwordEditOutput");
        passwordEditOutput.setValue("Password:");
        htmlPanelGrid.getChildren().add(passwordEditOutput);
        
        InputText passwordEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        passwordEditInput.setId("passwordEditInput");
        passwordEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.password}", String.class));
        LengthValidator passwordEditInputValidator = new LengthValidator();
        passwordEditInputValidator.setMaximum(30);
        passwordEditInput.addValidator(passwordEditInputValidator);
        passwordEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(passwordEditInput);
        
        Message passwordEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        passwordEditInputMessage.setId("passwordEditInputMessage");
        passwordEditInputMessage.setFor("passwordEditInput");
        passwordEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(passwordEditInputMessage);
        
        OutputLabel emailEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        emailEditOutput.setFor("emailEditInput");
        emailEditOutput.setId("emailEditOutput");
        emailEditOutput.setValue("Email:");
        htmlPanelGrid.getChildren().add(emailEditOutput);
        
        InputText emailEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        emailEditInput.setId("emailEditInput");
        emailEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.email}", String.class));
        LengthValidator emailEditInputValidator = new LengthValidator();
        emailEditInputValidator.setMaximum(50);
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setPattern(JSFConstants.EMAIL_PATTERN);
        emailEditInput.addValidator(emailEditInputValidator);
        emailEditInput.addValidator(regexValidator);
        emailEditInput.setValidatorMessage(JSFConstants.INVALID_EMAIL_MSG);
        emailEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(emailEditInput);
        
        Message emailEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        emailEditInputMessage.setId("emailEditInputMessage");
        emailEditInputMessage.setFor("emailEditInput");
        emailEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(emailEditInputMessage);
        
        return htmlPanelGrid;
    }

	public HtmlPanelGrid populateViewPanel() {
         FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText usernameLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        usernameLabel.setId("usernameLabel");
        usernameLabel.setValue("Username:");
        htmlPanelGrid.getChildren().add(usernameLabel);
        
        HtmlOutputText usernameValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        usernameValue.setId("usernameValue");
        usernameValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.username}", String.class));
        htmlPanelGrid.getChildren().add(usernameValue);
        
        HtmlOutputText emailLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        emailLabel.setId("emailLabel");
        emailLabel.setValue("Email:");
        htmlPanelGrid.getChildren().add(emailLabel);
        
        HtmlOutputText emailValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        emailValue.setId("emailValue");
        emailValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.email}", String.class));
        htmlPanelGrid.getChildren().add(emailValue);
        
        
        HtmlOutputText lastModifiedLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        lastModifiedLabel.setId("lastModifiedLabel");
        lastModifiedLabel.setValue("Last Modified:");
        htmlPanelGrid.getChildren().add(lastModifiedLabel);
        
        HtmlOutputText lastModifiedValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        lastModifiedValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{employeeBean.employee.lastModifiedAsDate}", java.util.Date.class));
        DateTimeConverter lastModifiedValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        lastModifiedValueConverter.setPattern(JSFConstants.DATE_TIME_FORMAT);
        lastModifiedValue.setConverter(lastModifiedValueConverter);
        htmlPanelGrid.getChildren().add(lastModifiedValue);
        
        return htmlPanelGrid;
    }

	public Employee getEmployee() {
        if (employee == null) {
            employee = new Employee();
        }
        return employee;
    }

	public void setEmployee(Employee employee) {
        this.employee = employee;
    }

	public List<TimeLog> getSelectedTimeLogs() {
        return selectedTimeLogs;
    }

	public void setSelectedTimeLogs(List<TimeLog> selectedTimeLogs) {
        if (selectedTimeLogs != null) {
            employee.setTimeLogs(new HashSet<TimeLog>(selectedTimeLogs));
        }
        this.selectedTimeLogs = selectedTimeLogs;
    }

	public List<WorkLog> getSelectedWorkLogs() {
        return selectedWorkLogs;
    }

	public void setSelectedWorkLogs(List<WorkLog> selectedWorkLogs) {
        if (selectedWorkLogs != null) {
            employee.setWorkLogs(new HashSet<WorkLog>(selectedWorkLogs));
        }
        this.selectedWorkLogs = selectedWorkLogs;
    }

	public String onEdit() {
        if (employee != null && employee.getTimeLogs() != null) {
            selectedTimeLogs = new ArrayList<TimeLog>(employee.getTimeLogs());
        }
        if (employee != null && employee.getWorkLogs() != null) {
            selectedWorkLogs = new ArrayList<WorkLog>(employee.getWorkLogs());
        }
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
        findAllEmployees();
        return "employee";
    }

	public String displayCreateDialog() {
        employee = new Employee();
        createDialogVisible = true;
        return "employee";
    }

	public String persist() {
        String message = "";
        employee.setLastModified(java.util.Calendar.getInstance());
        if (employee.getId() != null) {
            employeeService.updateEmployee(employee);
            message = "message_successfully_updated";
        } else {
            employeeService.saveEmployee(employee);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "Employee");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllEmployees();
    }

	public String delete() {
        employeeService.deleteEmployee(employee);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "Employee");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllEmployees();
    }

	public void reset() {
        employee = null;
        selectedTimeLogs = null;
        selectedWorkLogs = null;
        createDialogVisible = false;
    }

	public void handleDialogClose(CloseEvent event) {
        reset();
    }
}
