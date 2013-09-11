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
import javax.faces.validator.LengthValidator;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.tmassasin.jsf.constants.JSFConstants;
import com.tmassasin.jsf.converter.EmployeeConverter;
import com.tmassasin.jsf.util.MessageFactory;
import com.tmassasin.model.Employee;
import com.tmassasin.model.WorkLog;
import com.tmassasin.service.EmployeeService;
import com.tmassasin.service.WorkLogService;

@Configurable
@ManagedBean(name = "workLogBean")
@SessionScoped
public class WorkLogBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4731027064342883472L;

	@Autowired
    WorkLogService workLogService;

	@Autowired
    EmployeeService employeeService;

	private String name = "WorkLogs";

	private WorkLog workLog;

	private List<WorkLog> allWorkLogs;

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
        columns.add("dayDivision");
        columns.add("request");
        columns.add("dayOfLog");
    }

	public String getName() {
        return name;
    }

	public List<String> getColumns() {
        return columns;
    }

	public List<WorkLog> getAllWorkLogs() {
        return allWorkLogs;
    }

	public void setAllWorkLogs(List<WorkLog> allWorkLogs) {
        this.allWorkLogs = allWorkLogs;
    }

	public String findAllWorkLogs() {
        allWorkLogs = workLogService.findAllWorkLogs();
        dataVisible = !allWorkLogs.isEmpty();
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
        employeeCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.employee}", Employee.class));
        employeeCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{workLogBean.completeEmployee}", List.class, new Class[] { String.class }));
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
        
        OutputLabel dayDivisionCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        dayDivisionCreateOutput.setFor("dayDivisionCreateInput");
        dayDivisionCreateOutput.setId("dayDivisionCreateOutput");
        dayDivisionCreateOutput.setValue("Day Division:");
        htmlPanelGrid.getChildren().add(dayDivisionCreateOutput);
        
        Spinner dayDivisionCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        dayDivisionCreateInput.setId("dayDivisionCreateInput");
        dayDivisionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.dayDivision}", Integer.class));
        dayDivisionCreateInput.setRequired(true);
        
        htmlPanelGrid.getChildren().add(dayDivisionCreateInput);
        
        Message dayDivisionCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        dayDivisionCreateInputMessage.setId("dayDivisionCreateInputMessage");
        dayDivisionCreateInputMessage.setFor("dayDivisionCreateInput");
        dayDivisionCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(dayDivisionCreateInputMessage);
        
        OutputLabel commentCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        commentCreateOutput.setFor("commentCreateInput");
        commentCreateOutput.setId("commentCreateOutput");
        commentCreateOutput.setValue("Comment:");
        htmlPanelGrid.getChildren().add(commentCreateOutput);
        
        InputTextarea commentCreateInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        commentCreateInput.setId("commentCreateInput");
        commentCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.comment}", String.class));
        LengthValidator commentCreateInputValidator = new LengthValidator();
        commentCreateInputValidator.setMaximum(1000);
        commentCreateInput.addValidator(commentCreateInputValidator);
        commentCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(commentCreateInput);
        
        Message commentCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        commentCreateInputMessage.setId("commentCreateInputMessage");
        commentCreateInputMessage.setFor("commentCreateInput");
        commentCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(commentCreateInputMessage);
        
        OutputLabel filesCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        filesCreateOutput.setFor("filesCreateInput");
        filesCreateOutput.setId("filesCreateOutput");
        filesCreateOutput.setValue("Files:");
        htmlPanelGrid.getChildren().add(filesCreateOutput);
        
        InputTextarea filesCreateInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        filesCreateInput.setId("filesCreateInput");
        filesCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.files}", String.class));
        LengthValidator filesCreateInputValidator = new LengthValidator();
        filesCreateInputValidator.setMaximum(1000);
        filesCreateInput.addValidator(filesCreateInputValidator);
        filesCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(filesCreateInput);
        
        Message filesCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        filesCreateInputMessage.setId("filesCreateInputMessage");
        filesCreateInputMessage.setFor("filesCreateInput");
        filesCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(filesCreateInputMessage);
        
        OutputLabel requestCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        requestCreateOutput.setFor("requestCreateInput");
        requestCreateOutput.setId("requestCreateOutput");
        requestCreateOutput.setValue("Request:");
        htmlPanelGrid.getChildren().add(requestCreateOutput);
        
        InputText requestCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        requestCreateInput.setId("requestCreateInput");
        requestCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.request}", String.class));
        LengthValidator requestCreateInputValidator = new LengthValidator();
        requestCreateInputValidator.setMaximum(30);
        requestCreateInput.addValidator(requestCreateInputValidator);
        requestCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(requestCreateInput);
        
        Message requestCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        requestCreateInputMessage.setId("requestCreateInputMessage");
        requestCreateInputMessage.setFor("requestCreateInput");
        requestCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(requestCreateInputMessage);
        
        OutputLabel dayOfLogCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        dayOfLogCreateOutput.setFor("dayOfLogCreateInput");
        dayOfLogCreateOutput.setId("dayOfLogCreateOutput");
        dayOfLogCreateOutput.setValue("Day Of Log:");
        htmlPanelGrid.getChildren().add(dayOfLogCreateOutput);
        
        Calendar dayOfLogCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        dayOfLogCreateInput.setId("dayOfLogCreateInput");
        dayOfLogCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.dayOfLog}", Date.class));
        dayOfLogCreateInput.setNavigator(true);
        dayOfLogCreateInput.setEffect("slideDown");
        dayOfLogCreateInput.setPattern("dd/MM/yyyy");
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
        employeeEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.employee}", Employee.class));
        employeeEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{workLogBean.completeEmployee}", List.class, new Class[] { String.class }));
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
        
        OutputLabel dayDivisionEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        dayDivisionEditOutput.setFor("dayDivisionEditInput");
        dayDivisionEditOutput.setId("dayDivisionEditOutput");
        dayDivisionEditOutput.setValue("Day Division:");
        htmlPanelGrid.getChildren().add(dayDivisionEditOutput);
        
        Spinner dayDivisionEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        dayDivisionEditInput.setId("dayDivisionEditInput");
        dayDivisionEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.dayDivision}", Integer.class));
        dayDivisionEditInput.setRequired(true);
        
        htmlPanelGrid.getChildren().add(dayDivisionEditInput);
        
        Message dayDivisionEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        dayDivisionEditInputMessage.setId("dayDivisionEditInputMessage");
        dayDivisionEditInputMessage.setFor("dayDivisionEditInput");
        dayDivisionEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(dayDivisionEditInputMessage);
        
        OutputLabel commentEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        commentEditOutput.setFor("commentEditInput");
        commentEditOutput.setId("commentEditOutput");
        commentEditOutput.setValue("Comment:");
        htmlPanelGrid.getChildren().add(commentEditOutput);
        
        InputTextarea commentEditInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        commentEditInput.setId("commentEditInput");
        commentEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.comment}", String.class));
        LengthValidator commentEditInputValidator = new LengthValidator();
        commentEditInputValidator.setMaximum(1000);
        commentEditInput.addValidator(commentEditInputValidator);
        commentEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(commentEditInput);
        
        Message commentEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        commentEditInputMessage.setId("commentEditInputMessage");
        commentEditInputMessage.setFor("commentEditInput");
        commentEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(commentEditInputMessage);
        
        OutputLabel filesEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        filesEditOutput.setFor("filesEditInput");
        filesEditOutput.setId("filesEditOutput");
        filesEditOutput.setValue("Files:");
        htmlPanelGrid.getChildren().add(filesEditOutput);
        
        InputTextarea filesEditInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        filesEditInput.setId("filesEditInput");
        filesEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.files}", String.class));
        LengthValidator filesEditInputValidator = new LengthValidator();
        filesEditInputValidator.setMaximum(1000);
        filesEditInput.addValidator(filesEditInputValidator);
        filesEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(filesEditInput);
        
        Message filesEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        filesEditInputMessage.setId("filesEditInputMessage");
        filesEditInputMessage.setFor("filesEditInput");
        filesEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(filesEditInputMessage);
        
        OutputLabel requestEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        requestEditOutput.setFor("requestEditInput");
        requestEditOutput.setId("requestEditOutput");
        requestEditOutput.setValue("Request:");
        htmlPanelGrid.getChildren().add(requestEditOutput);
        
        InputText requestEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        requestEditInput.setId("requestEditInput");
        requestEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.request}", String.class));
        LengthValidator requestEditInputValidator = new LengthValidator();
        requestEditInputValidator.setMaximum(30);
        requestEditInput.addValidator(requestEditInputValidator);
        requestEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(requestEditInput);
        
        Message requestEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        requestEditInputMessage.setId("requestEditInputMessage");
        requestEditInputMessage.setFor("requestEditInput");
        requestEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(requestEditInputMessage);
        
        OutputLabel dayOfLogEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        dayOfLogEditOutput.setFor("dayOfLogEditInput");
        dayOfLogEditOutput.setId("dayOfLogEditOutput");
        dayOfLogEditOutput.setValue("Day Of Log:");
        htmlPanelGrid.getChildren().add(dayOfLogEditOutput);
        
        Calendar dayOfLogEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        dayOfLogEditInput.setId("dayOfLogEditInput");
        dayOfLogEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.dayOfLog}", Date.class));
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
        employeeValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.employee}", Employee.class));
        employeeValue.setConverter(new EmployeeConverter());
        htmlPanelGrid.getChildren().add(employeeValue);
        
        HtmlOutputText dayDivisionLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        dayDivisionLabel.setId("dayDivisionLabel");
        dayDivisionLabel.setValue("Day Division:");
        htmlPanelGrid.getChildren().add(dayDivisionLabel);
        
        HtmlOutputText dayDivisionValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        dayDivisionValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.dayDivision}", String.class));
        dayDivisionValue.setStyle("align:right");
        htmlPanelGrid.getChildren().add(dayDivisionValue);
        
        HtmlOutputText commentLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        commentLabel.setId("commentLabel");
        commentLabel.setValue("Comment:");
        htmlPanelGrid.getChildren().add(commentLabel);
        
        InputTextarea commentValue = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        commentValue.setId("commentValue");
        commentValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.comment}", String.class));
        commentValue.setReadonly(true);
        commentValue.setAutoResize(false);
        htmlPanelGrid.getChildren().add(commentValue);
        
        HtmlOutputText filesLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        filesLabel.setId("filesLabel");
        filesLabel.setValue("Files:");
        htmlPanelGrid.getChildren().add(filesLabel);
        
        InputTextarea filesValue = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        filesValue.setId("filesValue");
        filesValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.files}", String.class));
        filesValue.setReadonly(true);
        filesValue.setAutoResize(false);
        htmlPanelGrid.getChildren().add(filesValue);
        
        HtmlOutputText requestLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        requestLabel.setId("requestLabel");
        requestLabel.setValue("Request:");
        htmlPanelGrid.getChildren().add(requestLabel);
        
        HtmlOutputText requestValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        requestValue.setId("requestValue");
        requestValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.request}", String.class));
        htmlPanelGrid.getChildren().add(requestValue);
        
        HtmlOutputText dayOfLogLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        dayOfLogLabel.setId("dayOfLogLabel");
        dayOfLogLabel.setValue("Day Of Log:");
        htmlPanelGrid.getChildren().add(dayOfLogLabel);
        
        HtmlOutputText dayOfLogValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        dayOfLogValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.dayOfLog}", Date.class));
        DateTimeConverter dayOfLogValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        dayOfLogValueConverter.setPattern(JSFConstants.DATE_ONLY_FORMAT);
        dayOfLogValue.setConverter(dayOfLogValueConverter);
        htmlPanelGrid.getChildren().add(dayOfLogValue);
        
        HtmlOutputText lastModifiedLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        lastModifiedLabel.setId("lastModifiedLabel");
        lastModifiedLabel.setValue("Last Modified:");
        htmlPanelGrid.getChildren().add(lastModifiedLabel);
        
        HtmlOutputText lastModifiedValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        lastModifiedValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{workLogBean.workLog.lastModifiedAsDate}", Date.class));
        DateTimeConverter lastModifiedValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        lastModifiedValueConverter.setPattern(JSFConstants.DATE_TIME_FORMAT);
        lastModifiedValue.setConverter(lastModifiedValueConverter);
        htmlPanelGrid.getChildren().add(lastModifiedValue);
        
        return htmlPanelGrid;
    }

	public WorkLog getWorkLog() {
        if (workLog == null) {
            workLog = new WorkLog();
        }
        return workLog;
    }

	public void setWorkLog(WorkLog workLog) {
        this.workLog = workLog;
    }

	public List<Employee> completeEmployee(String query) {
        List<Employee> suggestions = new ArrayList<Employee>();
        for (Employee employee : employeeService.findAllEmployees()) {
            String employeeStr = String.valueOf(employee.getUsername() +  " "  + employee.getPassword() +  " "  + employee.getEmail() +  " "  + employee.getLastModified());
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
        findAllWorkLogs();
        return "workLog";
    }

	public String displayCreateDialog() {
        workLog = new WorkLog();
        createDialogVisible = true;
        return "workLog";
    }

	public String persist() {
        String message = "";
        workLog.setLastModified(java.util.Calendar.getInstance());
        if (workLog.getId() != null) {
            workLogService.updateWorkLog(workLog);
            message = "message_successfully_updated";
        } else {
            workLogService.saveWorkLog(workLog);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "WorkLog");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllWorkLogs();
    }

	public String delete() {
        workLogService.deleteWorkLog(workLog);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "WorkLog");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllWorkLogs();
    }

	public void reset() {
        workLog = null;
        createDialogVisible = false;
    }

	public void handleDialogClose(CloseEvent event) {
        reset();
    }
}
