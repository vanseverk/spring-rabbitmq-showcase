package be.reactiveprogramming.springrabbitmqshowcase.reactorrabbitmq;

import be.reactiveprogramming.springrabbitmqshowcase.reactorrabbitmq.gateway.RabbitMQGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactorRabbitMQApplication {

    private final Logger LOG = LoggerFactory.getLogger(ReactorRabbitMQApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReactorRabbitMQApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RabbitMQGateway rabbitMQGateway) {
        return args ->
                Flux.just("Tom", "Jerry", "Mickey", "Donald", "Goofy")
                        .flatMap(rabbitMQGateway::reverseNameAndPrintOnExternalSystem)
                        .map(result -> {
                            LOG.info(String.format("Printed out %s", result));
                            return result;
                        }).blockLast();
    }
}
