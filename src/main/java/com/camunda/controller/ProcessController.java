package com.camunda.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * description : 流程接口
 *
 * @author kunlunrepo
 * date :  2023-09-16 16:07
 */
@RestController
@Slf4j
public class ProcessController {

    @Autowired
    private RuntimeService runtimeService;


    @GetMapping("/start")
    public Map startProcess(@RequestParam("processDefinitionId")String processDefinitionId) {
        long startTime = System.currentTimeMillis();
        log.info("【接口】-[startProcess]--- {}", processDefinitionId);

        ProcessInstantiationBuilder builder = runtimeService.createProcessInstanceById(processDefinitionId);
        ProcessInstanceWithVariables procResult = builder.executeWithVariablesInReturn();


        Map<String, String> result = new HashMap<>();
        result.put("processDefinitionId", procResult.getProcessDefinitionId());
        result.put("processInstanceId", procResult.getProcessInstanceId());
        result.put("rootProcessInstanceId", procResult.getRootProcessInstanceId());
        result.put("businessKey", procResult.getBusinessKey());
        log.info("【接口】-[startProcess]---完成 {} 耗时：{} 部署详情：\n{} ", processDefinitionId, System.currentTimeMillis() - startTime, procResult);
        return result;
    }

}
