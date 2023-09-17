package com.camunda.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceModificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description : 特定操作接口
 *
 * @author kunlunrepo
 * date :  2023-09-16 17:21
 */
@RestController
@Slf4j
public class SpecificController {

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("/skip")
    public String skip(@RequestParam("processInstanceId")String processInstanceId,
                       @RequestParam("cancelActKeysStr")String cancelActKeysStr,
                       @RequestParam("startActKeysStr")String startActKeysStr) {
        long startTime = System.currentTimeMillis();
        log.info("【接口】-[skip]--- {}", processInstanceId);

        // 针对那个流程的处理
        ProcessInstanceModificationBuilder builder = runtimeService.createProcessInstanceModification(processInstanceId);

        // 取消节点
        if (StringUtils.isNotEmpty(cancelActKeysStr)) {
            String[] cancelActKeys = cancelActKeysStr.split(",");
            for (String cancelActKey : cancelActKeys) {
                builder.cancelAllForActivity(cancelActKey);
            }
        }

        // 启动节点
        if (StringUtils.isNotEmpty(startActKeysStr)) {
            String[] startActKeys = startActKeysStr.split(",");
            for (String startActKey : startActKeys) {
                builder.startAfterActivity(startActKey);
            }
        }

        // 最终执行
        builder.execute();

        log.info("【接口】-[skip]---完成 {} 耗时：{} 详情：\n{} ", processInstanceId, System.currentTimeMillis() - startTime);
        return "跳转节点成功";
    }
}
