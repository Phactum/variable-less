package at.phactum.camunda.workflowaggregate.adapters;

import java.beans.FeatureDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Iterator;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.javax.el.ELContext;
import org.camunda.bpm.engine.impl.javax.el.ELResolver;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregateRepository;

/*
 * Custom expression language resolver to resolve process entities
 * by using correspondingly named spring data repositories.
 */
public class WorkflowAggregateELResolver extends ELResolver {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowAggregateELResolver.class);

    private final ApplicationContext applicationContext;

    public WorkflowAggregateELResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return Object.class;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return Object.class;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {

        if (base != null) {
            return null;
        }

        var execution = (ExecutionEntity) context
                .getELResolver()
                .getValue(context, null, "execution");
        
        // convention: BPMN process ID is the same as entity's simple class name
        var entityName = execution.getProcessDefinition().getKey();

        var workflowEntityClass = new Class[] { null };
        var repositoryBean = applicationContext
                .getBeansWithAnnotation(Repository.class) // find all repository beans
                .values()
                .stream()
                .filter(b -> b instanceof WorkflowAggregateRepository) // which is are of type ProcessEntityRepository
                .map(b -> (WorkflowAggregateRepository<?>) b)
                .filter(b -> Arrays                                       // filter that repositories
                                .stream(b.getClass().getInterfaces())     
                                .filter(i -> WorkflowAggregateRepository.class.isAssignableFrom(i))
                                .flatMap(i -> Arrays.stream(i.getGenericInterfaces())) // to match it's generic interface'
                                .filter(i -> i instanceof ParameterizedType)
                                .map(i -> (ParameterizedType) i)
                                .filter(i -> WorkflowAggregateRepository.class.equals(i.getRawType())) // (which is ProcessEntityRepository)
                                .flatMap(i -> Arrays.stream(i.getActualTypeArguments()))
                                .findFirst()                              // first argument (the process entity class)
                                .map(t -> {
                                    workflowEntityClass[0] = (Class<?>) t;
                                    return workflowEntityClass[0].getSimpleName();
                                })
                                .get()
                        .equals(entityName))                            // to matching the requested entity bean name
                .findFirst();

        if (repositoryBean.isEmpty()) {
            return null;
        }

        var businessKey = execution.getProcessBusinessKey();
        if (businessKey == null) {
            return null;
        }

        var repository = repositoryBean.get();
        var entity = repository.findById(businessKey);
        if (entity.isEmpty()) {
            return null;
        }

        return getValue(context, workflowEntityClass[0], entity.get(), property);

    }
    
    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {

        if (base == null && getValue(context, null, property) != null) {
            throw new ProcessEngineException("Cannot set value of '" + property +
                "', it resolves to a process entity bound to the process instance.");
        }

    }

    private Object getValue(
            final ELContext context,
            final Class<?> workflowDomainEntityClass,
            final Object domainEntity,
            final Object property) {

        // use getter
        final var getterName = "get"
                + firstCharacterToUpperCase(property.toString());
        try {
            final var result = workflowDomainEntityClass
                    .getMethod(getterName)
                    .invoke(domainEntity);
            context.setPropertyResolved(true);
            return result;
        } catch (NoSuchMethodException e) {
            /* ignored */
        } catch (Exception e) {
            logger.warn("Could not access '{}#{}'",
                    workflowDomainEntityClass.getName(), getterName, e);
            return null;
        }

        // use getter for booleans
        final var isGetterName = "is"
                + firstCharacterToUpperCase(property.toString());
        try {
            final var result = workflowDomainEntityClass
                    .getMethod(isGetterName)
                    .invoke(domainEntity);
            context.setPropertyResolved(true);
            return result;
        } catch (NoSuchMethodException e) {
            /* ignored */
        } catch (Exception e) {
            logger.warn("Could not access '{}#{}'",
                    workflowDomainEntityClass.getName(), isGetterName, e);
            return null;
        }
        
        // use property
        try {
            final var field = workflowDomainEntityClass
                    .getDeclaredField(property.toString());
            field.setAccessible(true);
            context.setPropertyResolved(true);
            return field.get(domainEntity);
        } catch (NoSuchFieldException e) {
            /* ignored */
        } catch (Exception e) {
            logger.warn("Could not access property '{}' in class '{}'",
                    property.toString(), workflowDomainEntityClass.getName(), e);
            return null;
        }
        
        return null;
        
    }

    private String firstCharacterToUpperCase(final String str) {

        if (str == null) {
            return null;
        }

        final var result = new StringBuffer();
        if (str.length() > 0) {
            result.append(Character.toUpperCase(str.charAt(0)));
        }
        if (str.length() > 1) {
            result.append(str, 1, str.length());
        }

        return result.toString();

    }

}
