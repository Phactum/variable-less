package at.phactum.camunda.workflowaggregate.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * A ProcessEntityRepository stores Process Entities
 */
@NoRepositoryBean
public interface WorkflowAggregateRepository<P extends WorkflowAggregate> extends JpaRepository<P, String> {
}
