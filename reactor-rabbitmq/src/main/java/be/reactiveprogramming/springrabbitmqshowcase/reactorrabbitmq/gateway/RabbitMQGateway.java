package be.reactiveprogramming.springrabbitmqshowcase.reactorrabbitmq.gateway;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.RpcClient;
import reactor.rabbitmq.Sender;

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

        Sender sender = RabbitFlux.createSender();
        rpcClient = sender.rpcClient(exchange, routingKey);
    }

    @PreDestroy
    public void preDestroy() {
        rpcClient.close();
    }

    public Mono<String> sendMessage(String input) {
        return rpcClient.rpc(Mono.just(
                new RpcClient.RpcRequest(toBinary(input))
        )).map(delivery -> fromBinary(delivery.getBody(), String.class));
    }
}
