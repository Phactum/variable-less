package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DMN")
public class Process_DMN extends TestEntity {

    public Process_DMN() {
        super();
    }

    public Process_DMN(String businessKey) {
        super(businessKey);
    }

}
