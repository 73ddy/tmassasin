package com.tmassasin.service;

import com.tmassasin.model.TimeLog;

import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TimeLogServiceImpl implements TimeLogService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long countAllTimeLogs() {
        return TimeLog.countTimeLogs();
    }

	public void deleteTimeLog(TimeLog timeLog) {
        timeLog.remove();
    }

	public TimeLog findTimeLog(Long id) {
        return TimeLog.findTimeLog(id);
    }

	public List<TimeLog> findAllTimeLogs() {
        return TimeLog.findAllTimeLogs();
    }

	public List<TimeLog> findTimeLogEntries(int firstResult, int maxResults) {
        return TimeLog.findTimeLogEntries(firstResult, maxResults);
    }

	public void saveTimeLog(TimeLog timeLog) {
        timeLog.persist();
    }

	public TimeLog updateTimeLog(TimeLog timeLog) {
        return timeLog.merge();
    }
}
