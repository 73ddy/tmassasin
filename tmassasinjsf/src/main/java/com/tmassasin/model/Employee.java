package com.tmassasin.model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

@Configurable
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -729542885468312942L;

	public static TypedQuery<Employee> findEmployeesByUsernameEquals(String username) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        EntityManager em = Employee.entityManager();
        TypedQuery<Employee> q = em.createQuery("SELECT o FROM Employee AS o WHERE o.username = :username", Employee.class);
        q.setParameter("username", username);
        return q;
    }

	public String toString() {
        return username;
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
        EntityManager em = new Employee().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countEmployees() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Employee o", Long.class).getSingleResult();
    }

	public static List<Employee> findAllEmployees() {
        return entityManager().createQuery("SELECT o FROM Employee o", Employee.class).getResultList();
    }

	public static Employee findEmployee(Long id) {
        if (id == null) return null;
        return entityManager().find(Employee.class, id);
    }

	public static List<Employee> findEmployeeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Employee o", Employee.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Employee attached = Employee.findEmployee(this.id);
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
    public Employee merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Employee merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<TimeLog> timeLogs;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<WorkLog> workLogs;

	@Column(name = "username", length = 30)
    @NotNull
    private String username;

	@Column(name = "password", length = 30)
    @NotNull
    private String password;

	@Column(name = "email", length = 50)
    @NotNull
    private String email;

	@Column(name = "last_modified")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar lastModified;

	public Set<TimeLog> getTimeLogs() {
        return timeLogs;
    }

	public void setTimeLogs(Set<TimeLog> timeLogs) {
        this.timeLogs = timeLogs;
    }

	public Set<WorkLog> getWorkLogs() {
        return workLogs;
    }

	public void setWorkLogs(Set<WorkLog> workLogs) {
        this.workLogs = workLogs;
    }

	public String getUsername() {
        return username;
    }

	public void setUsername(String username) {
        this.username = username;
    }

	public String getPassword() {
        return password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public String getEmail() {
        return email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public Calendar getLastModified() {
        return lastModified;
    }

	public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }
}
