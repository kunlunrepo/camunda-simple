package com.camunda.controller;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * description : 部署接口
 *
 * @author kunlunrepo
 * date :  2023-09-16 14:29
 */
@RestController
@Slf4j
public class DeployController {


    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("/deploy")
    public String deploy(@RequestParam("filePath") String filePath) throws FileNotFoundException {
        if (StringUtils.isEmpty(filePath)) {
            return "输入路径不能为空";
        }
        long startTime = System.currentTimeMillis();
        log.info("【接口】-[deploy]--- {}", repositoryService);

        // 从指定位置读取文件
        File file = new File(filePath);
        FileInputStream fileInputStream=new FileInputStream(file);
        InputStream inputStream = new BufferedInputStream(fileInputStream);

        // 将文件部署至camunda框架中
        BpmnModelInstance bpmnModelInst = Bpmn.readModelFromStream(inputStream);
        String bpmnFileName = UUID.fastUUID() + ".bpmn";
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addModelInstance(bpmnFileName, bpmnModelInst);
        DeploymentWithDefinitions deploymentResult = deployment.deployWithResult();

        log.info("【接口】-[deploy]---完成 耗时：{} 部署详情：\n{} ", System.currentTimeMillis() - startTime, deploymentResult);
        return deploymentResult.getDeployedProcessDefinitions().toString();
    }

}
