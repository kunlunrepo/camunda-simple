package com.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.Execution;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-09-17 15:33
 */
public interface SequenceFlowService {

    boolean fz(DelegateExecution execution, String flowId);

    boolean fz1(DelegateExecution execution);

    boolean fz2(DelegateExecution execution);

    boolean fz3(DelegateExecution execution);

}
