package com.tmassasin.service;
import com.tmassasin.model.TimeLog;
import java.util.List;

public interface TimeLogService {

	public abstract long countAllTimeLogs();


	public abstract void deleteTimeLog(TimeLog timeLog);


	public abstract TimeLog findTimeLog(Long id);


	public abstract List<TimeLog> findAllTimeLogs();


	public abstract List<TimeLog> findTimeLogEntries(int firstResult, int maxResults);


	public abstract void saveTimeLog(TimeLog timeLog);


	public abstract TimeLog updateTimeLog(TimeLog timeLog);

}
