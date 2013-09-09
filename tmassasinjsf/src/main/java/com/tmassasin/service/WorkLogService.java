package com.tmassasin.service;
import com.tmassasin.model.WorkLog;
import java.util.List;

public interface WorkLogService {

	public abstract long countAllWorkLogs();


	public abstract void deleteWorkLog(WorkLog workLog);


	public abstract WorkLog findWorkLog(Long id);


	public abstract List<WorkLog> findAllWorkLogs();


	public abstract List<WorkLog> findWorkLogEntries(int firstResult, int maxResults);


	public abstract void saveWorkLog(WorkLog workLog);


	public abstract WorkLog updateWorkLog(WorkLog workLog);

}
