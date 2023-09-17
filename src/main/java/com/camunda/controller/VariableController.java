package com.camunda.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * description : 变量接口
 *
 * @author kunlunrepo
 * date :  2023-09-16 16:07
 */
@RestController
@Slf4j
public class VariableController {

    @Autowired
    private RuntimeService runtimeService;

    // 设置变量
    @PostMapping("/set")
    public Map setVariable(@RequestParam("processInstanceId")String processInstanceId, @RequestBody Map variableMap) {
        long startTime = System.currentTimeMillis();
        log.info("【接口】-[setVariable]--- {}", processInstanceId);
        runtimeService.setVariables(processInstanceId, variableMap);
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        log.info("【接口】-[setVariable]---完成 {} 耗时：{} 详情：\n{} ", processInstanceId, System.currentTimeMillis() - startTime, variables);
        return variables;
    }

    // 获取变量
    @GetMapping("/get")
    public Map getVariable(@RequestParam("processInstanceId")String processInstanceId) {
        long startTime = System.currentTimeMillis();
        log.info("【接口】-[getVariable]--- {}", processInstanceId);
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        log.info("【接口】-[getVariable]---完成 {} 耗时：{} 详情：\n{} ", processInstanceId, System.currentTimeMillis() - startTime, variables);
        return variables;
    }

}
