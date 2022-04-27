package at.phactum.camunda.it.taxiride;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import at.phactum.camunda.it.taxiride.ItConfiguration.MockDriverService;
import at.phactum.camunda.it.taxiride.domain.TaxiRide;
import at.phactum.camunda.it.taxiride.domain.TaxiRideRepository;
import at.phactum.camunda.workflowaggregate.WorkflowService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ItConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = { "server.port=8080" }
    )
public class TaxiRideIT {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private TransactionTemplate transaction;

    @Autowired
    private MockDriverService driverService;

    @Autowired
    private TaxiRideRepository rideRepository;
    
    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testCompleteRide() throws Exception {

        final var businessKey = UUID.randomUUID().toString();

        transaction.executeWithoutResult(status -> {

            final var ride = new TaxiRide(businessKey);
            ride.setPickupTime(OffsetDateTime.now());
            ride.setPickupLocation("start-road 47");
            ride.setTargetLocation("end-road 11");

            workflowService.correlateMessage(ride, "RideBooked");

        });

        driverService.waitForAllDriversRequestedForRides();

        Thread.sleep(500);

        transaction.executeWithoutResult(status -> {

            final var ride = rideRepository.findById(businessKey).get();
            ride.setDriver("Martin");

            workflowService.correlateMessage(ride, "ConfirmRide");

        });

        Thread.sleep(500);

        transaction.executeWithoutResult(status -> {

            final var ride = rideRepository.findById(businessKey).get();

            workflowService.correlateMessage(ride, "FinishRide");

        });

        driverService.waitForDriverBeingPayed();
        
        Thread.sleep(500);

        final var processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        Assertions.assertNull(processInstance);

    }

    @Test
    public void testNoRideAvailable() throws Exception {

        final var businessKey = UUID.randomUUID().toString();

        transaction.executeWithoutResult(status -> {

            final var ride = new TaxiRide(businessKey);
            ride.setPickupTime(OffsetDateTime.now());
            ride.setPickupLocation("start-road 47");
            ride.setTargetLocation("end-road 11");

            workflowService.correlateMessage(ride, "RideBooked");

        });

        driverService.waitForAllDriversRequestedForRides();
        
        ClockUtil.offset(3 * 60 * 1000l); // jump three minutes into future to force timer-event

        Thread.sleep(500);

        final var processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        Assertions.assertNull(processInstance);
        
        ClockUtil.offset(-3 * 60 * 1000l); // revert clock change

    }
    
}
