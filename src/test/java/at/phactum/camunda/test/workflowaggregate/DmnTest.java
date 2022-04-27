package at.phactum.camunda.test.workflowaggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.junit.jupiter.api.Test;

public class DmnTest {

    @Test
    public void testDecisionDmn() throws Exception {
        
        final var dmnEngine = ((DefaultDmnEngineConfiguration) DmnEngineConfiguration
                .createDefaultDmnEngineConfiguration())
                .buildEngine();
        
        try (final var dmn = getClass().getClassLoader().getResourceAsStream("bpmn/decision.dmn")) {
            
            final var result = dmnEngine.evaluateDecision("SimpleDecision", dmn, Map.of("input", Integer.valueOf(2)));
            
            assertEquals("b", result.getSingleEntry());
            
        }
        
    }
    
}
