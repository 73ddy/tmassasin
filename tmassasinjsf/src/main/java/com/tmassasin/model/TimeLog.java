package com.tmassasin.model;
import java.io.Serializable;
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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "time_log")
public class TimeLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3365389592886528464L;

	@ManyToOne
    @JoinColumn(name = "employee", referencedColumnName = "id", nullable=false)
    private Employee employee;

	@Column(name = "entry_time")
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(style = "M-")
    private Date entryTime;

	@Column(name = "exit_time")
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(style = "M-")
    private Date exitTime;

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

	public Date getEntryTime() {
        return entryTime;
    }

	public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

	public Date getExitTime() {
        return exitTime;
    }

	public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

	public Date getDayOfLog() {
        return dayOfLog;
    }

	public void setDayOfLog(Date dayOfLog) {
        this.dayOfLog = dayOfLog;
    }

	public static TypedQuery<TimeLog> findTimeLogsByEmployee(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("The employee argument is required");
        EntityManager em = TimeLog.entityManager();
        TypedQuery<TimeLog> q = em.createQuery("SELECT o FROM TimeLog AS o WHERE o.employee = :employee", TimeLog.class);
        q.setParameter("employee", employee);
        return q;
    }

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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new TimeLog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTimeLogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TimeLog o", Long.class).getSingleResult();
    }

	public static List<TimeLog> findAllTimeLogs() {
        return entityManager().createQuery("SELECT o FROM TimeLog o", TimeLog.class).getResultList();
    }

	public static TimeLog findTimeLog(Long id) {
        if (id == null) return null;
        return entityManager().find(TimeLog.class, id);
    }

	public static List<TimeLog> findTimeLogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TimeLog o", TimeLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TimeLog attached = TimeLog.findTimeLog(this.id);
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
    public TimeLog merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TimeLog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("employee").toString();
    }
}
