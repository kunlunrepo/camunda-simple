package com.camunda;

import cn.hutool.core.lang.UUID;
import com.fasterxml.uuid.impl.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-09-16 13:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Import({SpringBootProcessApplication.class})
public class DeploymentTest {

    @Autowired
    private RepositoryService repositoryService;

    // 部署流程图
    @Test
    public void deployProcess() throws Exception {
        log.info("【部署文件】---部署开始");
        // 从指定位置读取文件
        File file = new File("C:\\Users\\TopSecret\\Desktop\\包容网关.bpmn");
        FileInputStream fileInputStream=new FileInputStream(file);
        InputStream inputStream = new BufferedInputStream(fileInputStream);

        // 将文件部署至camunda框架中
        BpmnModelInstance bpmnModelInst = Bpmn.readModelFromStream(inputStream);
        String bpmnFileName = UUID.fastUUID() + ".bpmn";
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addModelInstance(bpmnFileName, bpmnModelInst);
        DeploymentWithDefinitions deploymentResult = deployment.deployWithResult();

        log.info("【部署文件】---部署完成 \n{}", deploymentResult);
    }


}
