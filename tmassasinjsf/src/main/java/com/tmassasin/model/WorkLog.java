package com.tmassasin.model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "work_log")
@Configurable
public class WorkLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -361483174679496188L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public static TypedQuery<WorkLog> findWorkLogsByEmployee(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("The employee argument is required");
        EntityManager em = WorkLog.entityManager();
        TypedQuery<WorkLog> q = em.createQuery("SELECT o FROM WorkLog AS o WHERE o.employee = :employee", WorkLog.class);
        q.setParameter("employee", employee);
        return q;
    }

	@ManyToOne
    @JoinColumn(name = "employee", referencedColumnName = "id", nullable=false)
    private Employee employee;

	@Column(name = "day_division")
    @NotNull
    private Integer dayDivision;

	@Column(name = "comment", length = 1000, nullable=false)
    @NotNull
    private String comment;

	@Column(name = "files", length = 1000)
    private String files;

	@Column(name = "last_modified", nullable=false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar lastModified;

	@Column(name = "request", length = 30)
    @NotNull
    private String request;

	@Column(name = "day_of_log", nullable=false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date dayOfLog;

	public Employee getEmployee() {
        return employee;
    }

	public void setEmployee(Employee employee) {
        this.employee = employee;
    }

	public Integer getDayDivision() {
        return dayDivision;
    }

	public void setDayDivision(Integer dayDivision) {
        this.dayDivision = dayDivision;
    }

	public String getComment() {
        return comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }

	public String getFiles() {
        return files;
    }

	public void setFiles(String files) {
        this.files = files;
    }

	public Calendar getLastModified() {
        return lastModified;
    }

	public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

	public String getRequest() {
        return request;
    }

	public void setRequest(String request) {
        this.request = request;
    }

	public Date getDayOfLog() {
        return dayOfLog;
    }

	public void setDayOfLog(Date dayOfLog) {
        this.dayOfLog = dayOfLog;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("employee").toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new WorkLog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countWorkLogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM WorkLog o", Long.class).getSingleResult();
    }

	public static List<WorkLog> findAllWorkLogs() {
        return entityManager().createQuery("SELECT o FROM WorkLog o", WorkLog.class).getResultList();
    }

	public static WorkLog findWorkLog(Long id) {
        if (id == null) return null;
        return entityManager().find(WorkLog.class, id);
    }

	public static List<WorkLog> findWorkLogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM WorkLog o", WorkLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            WorkLog attached = WorkLog.findWorkLog(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public WorkLog merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        WorkLog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	public Date getlastModifiedAsDate() {
		if (null != lastModified) {
			return lastModified.getTime();
		}
		return null;
	}
}
