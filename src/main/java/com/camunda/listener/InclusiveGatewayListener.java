package com.camunda.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

import java.beans.ExceptionListener;

/**
 * description : 包容网关监听器
 *
 * @author kunlunrepo
 * date :  2023-09-17 15:23
 */
@Service("InclusiveGatewayListener")
@Slf4j
public class InclusiveGatewayListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();
        String executionId = execution.getId();
        String id = execution.getCurrentActivityId();
        String name = execution.getCurrentActivityName();
        log.info("【包容网关监听器】开始--- actKey:{} actName:{} executionId:{} processInstanceId:{}", id, name, executionId, processInstanceId);

        log.info("【包容网关监听器】开始--- actKey:{} actName:{} executionId:{} processInstanceId:{}", id, name, executionId, processInstanceId);
    }
}
