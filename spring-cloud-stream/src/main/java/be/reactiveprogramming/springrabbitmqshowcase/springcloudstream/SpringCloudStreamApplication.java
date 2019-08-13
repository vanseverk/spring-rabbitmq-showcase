package be.reactiveprogramming.springrabbitmqshowcase.springcloudstream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
@EnableBinding(Processor.class)
public class SpringCloudStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamApplication.class, args);
    }

    private final Logger LOG = LoggerFactory.getLogger(SpringCloudStreamApplication.class);

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public String handle(String input) {
        final String reversedString = new StringBuilder(input).reverse().toString();

        LOG.info("Reversed {} to {}", input, reversedString);

        return reversedString;
    }
}

