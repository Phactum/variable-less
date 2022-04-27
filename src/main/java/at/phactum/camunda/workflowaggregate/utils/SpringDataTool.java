package at.phactum.camunda.workflowaggregate.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;

@Component
public class SpringDataTool {

    private static final Map<Class<?>, JpaRepository<?, String>> REPOSITORY_MAP = new HashMap<>();
    
    private final ApplicationContext applicationContext;

    public SpringDataTool(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <O> JpaRepository<? super O, String> getJpaRepository(O object) {

        //noinspection unchecked
        return getJpaRepository((Class<O>) object.getClass());

    }

    @SuppressWarnings("unchecked")
    public <O> JpaRepository<? super O, String> getJpaRepository(Class<O> type) {

        Class<? super O> cls = type;

        if (REPOSITORY_MAP.containsKey(cls))
            return (JpaRepository<? super O, String>) REPOSITORY_MAP.get(cls);

        var repositories = new Repositories(applicationContext);

        Optional<Object> repository;

        do {
            repository = repositories.getRepositoryFor(cls);
            cls = repository.isPresent() ? cls : cls.getSuperclass();
        } while (repository.isEmpty() && cls != Object.class);

        if (repository.isPresent()) {
            REPOSITORY_MAP.put(cls, (JpaRepository<?, String>) repository.get());
            return (JpaRepository<? super O, String>) REPOSITORY_MAP.get(cls);
        }

        throw new IllegalStateException(
            String.format("No Spring Data repository defined for %s", cls.getCanonicalName())
        );

    }

}
