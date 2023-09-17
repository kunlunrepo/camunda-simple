package com.camunda.listener;


import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.runtime.ProcessInstanceModificationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-09-17 13:34
 */
@Service("TaskCreateListener")
@Slf4j
public class TaskCreateListener implements TaskListener {

    private final static String INCL_GATEWAY_START = "Activity_1ttac2c"; // 包容网关开始节点编号

    private final static String INCL_GATEWAY_END = "Activity_0dc06n3"; // 包容网关结束节点编号

    private final static String BRANCH_1 = "Activity_1vgazyv"; // 分支1节点编号

    private final static String BRANCH_2 = "Activity_13fb7m5"; // 分支2节点编号

    private final static String BRANCH_3 = "Activity_1d8i5wt"; // 分支3节点编号

    private final static String BRANCH_4 = "Activity_1wx3lbm"; // 分支4节点编号

    private final static String AGG_BRANCH_1 = "Activity_1roq411"; // 聚合后的分支1节点编号

    private final static int INIT_BRANCH_NUM = 2; // 初始化分支数

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {

        // 基础数据
        String processInstanceId = delegateTask.getProcessInstanceId();
        String taskId = delegateTask.getId();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String taskName = delegateTask.getName();
        String activityInstanceId = delegateTask.getExecution().getActivityInstanceId();

        log.info("【任务监听器】开始--- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, processInstanceId);

        if (StringUtils.equals(taskDefinitionKey, INCL_GATEWAY_START)) {
            log.info("【任务监听器】包容(开始) 开始 --- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, activityInstanceId);
            // ☆ 包容网关-开始 ☆
            ProcessInstanceModificationBuilder builder = runtimeService.createProcessInstanceModification(processInstanceId);
            // 取消自己
            builder.cancelAllForActivity(taskDefinitionKey);
            // 开启另外两个
            builder.startBeforeActivity(BRANCH_1);
            if (INIT_BRANCH_NUM > 1) { // 其余分支
                builder.startBeforeActivity(BRANCH_4);
            }
            // 实际执行
            builder.execute();
            log.info("【任务监听器】包容(开始) 结束 --- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, activityInstanceId);
        } else if (StringUtils.equals(taskDefinitionKey, INCL_GATEWAY_END)) {
            log.info("【任务监听器】包容(结束) 开始 --- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, activityInstanceId);
            // ☆ 包容网关-结束 ☆
            // 判断自己是否已生成，生成则取消本次生成
            List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(taskDefinitionKey).list();
            log.info("【任务监听器】包容(结束) 任务 --- taskId:{} {} {} list: {}", taskId, taskDefinitionKey, taskName, list);
            if (CollectionUtil.isNotEmpty(list)) {
                ProcessInstanceModificationBuilder builder = runtimeService.createProcessInstanceModification(processInstanceId);
                // 取消自己
                builder.cancelActivityInstance(activityInstanceId);
                // 实际执行
                builder.execute();
            }

            // 第一种方案：首次经过时即 生成结束包容(结束)任务 --- 实际中判断自动执行的条件需要改造  注意事项：整体应该使用异步的方式 + 跳转的方式
            if (INIT_BRANCH_NUM == 1 && list.size() == 0 ) { // 初始化了几个 && 当前分支数是多少
                // 包容网关只有一个分支时
                log.info("【任务监听器】包容(结束) 自动执行(单分支) --- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, activityInstanceId);
                ProcessInstanceModificationBuilder builder = runtimeService.createProcessInstanceModification(processInstanceId);
                // 取消自己
                builder.cancelActivityInstance(activityInstanceId);
                // 聚合后的任务
                builder.startBeforeActivity(AGG_BRANCH_1);
                // 实际执行
                builder.execute();
            } else if ( /* 为0 && */  list.size() == 1) {
                // 包容网关有多个分支时
                log.info("【任务监听器】包容(结束) 自动执行(多分支) --- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, activityInstanceId);
                ProcessInstanceModificationBuilder builder = runtimeService.createProcessInstanceModification(processInstanceId);
                // 取消包容网关(结束)
                builder.cancelAllForActivity(list.get(0).getTaskDefinitionKey());
                // 聚合后的任务
                builder.startBeforeActivity(AGG_BRANCH_1); // 待查询出来
                // 实际执行
                builder.execute();

//                taskService.complete(taskId);
            }

            // 第二种方案：直到条件达成 才结束包容(结束)任务  【待验证一下】
//            if (list.size() == 0 ) { // 初始化了几个 && 当前分支数是多少
//                // 包容网关只有一个分支时
//                log.info("【任务监听器】包容(结束) 自动执行(单分支) --- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, activityInstanceId);
//                ProcessInstanceModificationBuilder builder = runtimeService.createProcessInstanceModification(processInstanceId);
//                // 取消自己
//                builder.cancelActivityInstance(activityInstanceId);
//                // 聚合后的任务
//                builder.startBeforeActivity(AGG_BRANCH_1);
//                // 实际执行
//                builder.execute();
//            }

            log.info("【任务监听器】包容(结束) 结束 --- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, activityInstanceId);
        }

        log.info("【任务监听器】结束--- taskId:{} {} {} {}", taskId, taskDefinitionKey, taskName, processInstanceId);

    }
}
