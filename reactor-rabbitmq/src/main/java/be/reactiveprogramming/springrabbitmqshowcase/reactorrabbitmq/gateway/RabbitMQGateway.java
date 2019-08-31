package be.reactiveprogramming.springrabbitmqshowcase.reactorrabbitmq.gateway;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.RpcClient;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static be.reactiveprogramming.springrabbitmqshowcase.reactorrabbitmq.binaryconverter.BinaryConverter.fromBinary;
import static be.reactiveprogramming.springrabbitmqshowcase.reactorrabbitmq.binaryconverter.BinaryConverter.toBinary;

@Component
public class RabbitMQGateway {

    private RpcClient rpcClient;

    @PostConstruct
    public void onInit() {
        final String exchange = "rpc.reverse";
        final String routingKey = "reverse";

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        SenderOptions senderOptions = new SenderOptions().connectionFactory(connectionFactory);

        Sender sender = RabbitFlux.createSender(senderOptions);
        rpcClient = sender.rpcClient(exchange, routingKey);
    }

    @PreDestroy
    public void preDestroy() {
        rpcClient.close();
    }

    public Mono<String> reverseNameAndPrintOnExternalSystem(String input) {
        return rpcClient.rpc(Mono.just(
                new RpcClient.RpcRequest(toBinary(input))
        )).map(delivery -> fromBinary(delivery.getBody(), String.class));
    }
}
