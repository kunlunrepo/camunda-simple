package com.camunda.service.impl;

import com.camunda.service.SequenceFlowService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.springframework.stereotype.Service;

/**
 * description : 分支服务
 *
 * @author kunlunrepo
 * date :  2023-09-17 15:34
 */
@Service("sequenceFlowService")
@Slf4j
public class SequenceFlowServiceImpl implements SequenceFlowService {

    @Override
    public boolean fz(DelegateExecution execution, String flowId) {
        String id = execution.getId();
        String actKey = execution.getCurrentActivityId();
        String actName = execution.getCurrentActivityName();
        String processInstanceId = execution.getProcessInstanceId();
        log.info("【路径-分支】开始--- processInstanceId: {} actKey: {} actName: {} flowId: {} ", processInstanceId, actKey, actName, flowId);

        log.info("【路径-分支】结束--- processInstanceId: {} actKey: {} actName: {} flowId: {} ", processInstanceId, actKey, actName, flowId);
        return true;
    }

    @Override
    public boolean fz1(DelegateExecution execution) {
        String id = execution.getId();
        String actKey = execution.getCurrentActivityId();
        String actName = execution.getCurrentActivityName();
        FlowElement bpmnModelElementInstance = execution.getBpmnModelElementInstance();
        String id1 = bpmnModelElementInstance.getId();
        String processInstanceId = execution.getProcessInstanceId();
        log.info("【路径-分支1】开始--- processInstanceId: {} actKey: {} actName: {} {} {}", processInstanceId, actKey, actName, id, id1);

        log.info("【路径-分支1】结束--- processInstanceId: {} actKey: {} actName: {} {} {}", processInstanceId, actKey, actName, id, id1);
        return true;
    }

    @Override
    public boolean fz2(DelegateExecution execution) {
        String id = execution.getId();
        String actKey = execution.getCurrentActivityId();
        String actName = execution.getCurrentActivityName();
        String processInstanceId = execution.getProcessInstanceId();
        log.info("【路径-分支2】开始--- processInstanceId: {} actKey: {} actName: {} {}", processInstanceId, actKey, actName, id);

        log.info("【路径-分支2】结束--- processInstanceId: {} actKey: {} actName: {} {}", processInstanceId, actKey, actName, id);
        return false;
    }

    @Override
    public boolean fz3(DelegateExecution execution) {
        String id = execution.getId();
        String actKey = execution.getCurrentActivityId();
        String actName = execution.getCurrentActivityName();
        String processInstanceId = execution.getProcessInstanceId();
        log.info("【路径-分支3】开始--- processInstanceId: {} actKey: {} actName: {} {}", processInstanceId, actKey, actName, id);

        log.info("【路径-分支3】结束--- processInstanceId: {} actKey: {} actName: {} {}", processInstanceId, actKey, actName, id);
        return false;
    }
}
