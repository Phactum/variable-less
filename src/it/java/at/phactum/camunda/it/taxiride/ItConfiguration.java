package at.phactum.camunda.it.taxiride;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import at.phactum.camunda.workflowaggregate.WorkflowAggregateCamundaPlugin;

@Configuration
@EnableAutoConfiguration
@EnableProcessApplication
@EnableJpaAuditing
@ComponentScan(
        basePackageClasses = { WorkflowAggregateCamundaPlugin.class, ItConfiguration.class })
public class ItConfiguration {

    public static class MockDriverService implements DriverService {

        private static final LinkedBlockingQueue<String> EVENTS = new LinkedBlockingQueue<>();

        @Override
        public Collection<String> determineNearbyDrivers(String pickupLocation, OffsetDateTime pickupTime,
                String targetLocation) {

            return List.of("Martin", "Stephan");

        }

        @Override
        public void requestRide(String driver, String rideId) {

            EVENTS.add(rideId);

        }

        public void waitForAllDriversRequestedForRides() throws Exception {

            // expect two events
            EVENTS.poll(10, TimeUnit.SECONDS);
            EVENTS.poll(10, TimeUnit.SECONDS);

        }

        @Override
        public void payDriver(String driver, float amount) {

            EVENTS.add(driver);

        }

        public void waitForDriverBeingPayed() throws Exception {

            // expect one event
            EVENTS.poll(10, TimeUnit.SECONDS);

        }

    }

    @Bean
    public MockDriverService mockDriverService() {

        return new MockDriverService();

    }

}
