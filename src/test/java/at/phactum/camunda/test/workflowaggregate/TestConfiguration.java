package at.phactum.camunda.test.workflowaggregate;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import at.phactum.camunda.workflowaggregate.WorkflowAggregateCamundaPlugin;

@Configuration
@ComponentScan(
        basePackageClasses = { WorkflowAggregateCamundaPlugin.class, TestConfiguration.class })
@EnableAutoConfiguration
@EnableProcessApplication
@EnableJpaAuditing
class TestConfiguration {

}
