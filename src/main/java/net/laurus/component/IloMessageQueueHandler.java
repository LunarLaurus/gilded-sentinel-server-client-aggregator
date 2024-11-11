package net.laurus.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.laurus.data.dto.ipmi.ilo.IloRegistrationRequest;
import net.laurus.ilo.AuthenticatedIloClient;
import net.laurus.ilo.UnauthenticatedIloClient;

@Service
public class IloMessageQueueHandler {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public IloMessageQueueHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUnauthenticatedIloClientData(UnauthenticatedIloClient clientObject) {
        rabbitTemplate.convertAndSend("unauthenticatedIloClientQueue", clientObject);
    }

    public void sendAuthenticatedIloClientData(AuthenticatedIloClient clientObject) {
        rabbitTemplate.convertAndSend("authenticatedIloClientQueue", clientObject);
    }

    @RabbitListener(queues = "newClientRequestQueue")
    public void processNewClientRequest(IloRegistrationRequest clientObject) {
        System.out.println("Received request to map new client: " + clientObject);
    }
}
