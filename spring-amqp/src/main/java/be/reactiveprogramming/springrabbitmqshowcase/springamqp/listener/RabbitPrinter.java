package be.reactiveprogramming.springrabbitmqshowcase.springamqp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Address;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static be.reactiveprogramming.springrabbitmqshowcase.springamqp.binaryconverter.BinaryConverter.fromBinary;

@Service
public class RabbitPrinter {

    private final Logger LOG = LoggerFactory.getLogger(RabbitPrinter.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitPrinter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "print.queue")
    public void listen(Message message) {
        Address replyTo = message.getMessageProperties().getReplyToAddress();

        LOG.info(fromBinary(message.getBody(), String.class));

        rabbitTemplate.convertAndSend(replyTo.getExchangeName(), replyTo.getRoutingKey(), message.getBody(), outgoingMessage -> {
            outgoingMessage.getMessageProperties().setCorrelationId(message.getMessageProperties().getCorrelationId());
            return outgoingMessage;
        });
    }
}
