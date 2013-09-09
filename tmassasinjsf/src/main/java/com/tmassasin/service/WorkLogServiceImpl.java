package com.tmassasin.service;

import com.tmassasin.model.WorkLog;

import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkLogServiceImpl implements WorkLogService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long countAllWorkLogs() {
        return WorkLog.countWorkLogs();
    }

	public void deleteWorkLog(WorkLog workLog) {
        workLog.remove();
    }

	public WorkLog findWorkLog(Long id) {
        return WorkLog.findWorkLog(id);
    }

	public List<WorkLog> findAllWorkLogs() {
        return WorkLog.findAllWorkLogs();
    }

	public List<WorkLog> findWorkLogEntries(int firstResult, int maxResults) {
        return WorkLog.findWorkLogEntries(firstResult, maxResults);
    }

	public void saveWorkLog(WorkLog workLog) {
        workLog.persist();
    }

	public WorkLog updateWorkLog(WorkLog workLog) {
        return workLog.merge();
    }
}
