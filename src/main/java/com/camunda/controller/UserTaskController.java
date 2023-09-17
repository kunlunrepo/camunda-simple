package com.camunda.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description : 用户任务接口
 *
 * @author kunlunrepo
 * date :  2023-09-16 16:07
 */
@RestController
@Slf4j
public class UserTaskController {

    @Autowired
    private TaskService taskService;

    // 查询任务
    @GetMapping("/getTask")
    public List<Map> getTask(@RequestParam("processInstanceId")String processInstanceId) {
        long startTime = System.currentTimeMillis();
        log.info("【接口】-[getTask]--- {}", processInstanceId);

        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processInstanceId);
        List<Task> taskList = taskQuery.list();

        List<Map> result = new ArrayList<>();
        for (Task task : taskList) {
            Map<String, Object> map = new HashMap<>();
            map.put("processDefinitionId", task.getProcessDefinitionId());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("executionId", task.getExecutionId());
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            map.put("taskName", task.getName());
            map.put("taskInstanceId", task.getId());
            map.put("createTime", task.getCreateTime());
            result.add(map);
        }

        log.info("【接口】-[getTask]---完成 {} 耗时：{} 详情：\n{} ", processInstanceId, System.currentTimeMillis() - startTime, result);
        return result;
    }

    // 完成任务
    @GetMapping("/complete")
    public String complete(@RequestParam("taskInstanceId")String taskInstanceId) {
        long startTime = System.currentTimeMillis();
        log.info("【接口】-[complete]--- {}", taskInstanceId);

        taskService.complete(taskInstanceId);

        log.info("【接口】-[complete]---完成 {} 耗时：{} 详情：\n{} ", taskInstanceId, System.currentTimeMillis() - startTime);
        return taskInstanceId + " 完成任务成功";
    }

}
