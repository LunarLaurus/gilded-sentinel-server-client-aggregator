package net.laurus.queue;

import static net.laurus.util.RabbitMqUtils.preparePayload;

import java.io.IOException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.laurus.data.dto.system.ClientCommonSystemDto;

/**
 * Handles queue operations for sending updates to client queues.
 * <p>
 * Provides methods to send authenticated and unauthenticated client data
 * to their respective RabbitMQ queues with efficient compression.
 * </p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SendClientUpdatesQueueHandler {
	
	private static final String QUEUE_NAME = "clientCommonSystemDtoQueue";

    private final RabbitTemplate rabbitQueue;

    /**
     * Sends authenticated client data to the queue.
     *
     * @param client The authenticated IloClient object to send.
     */
    public void sendClientData(ClientCommonSystemDto client) {
        log.info("Placing data on queue: {}, client: {}", QUEUE_NAME, client);
        send(QUEUE_NAME, client);
    }

    /**
     * Sends a serialized and compressed object to the specified RabbitMQ queue.
     * <p>
     * Handles serialization, compression, and error logging internally.
     * </p>
     *
     * @param queueName    The name of the queue to send the object to.
     * @param clientObject The object to send, which will be serialized and compressed.
     */
    private void send(String queueName, Object clientObject) {
        try {
            byte[] compressedData = preparePayload(clientObject, true);
            rabbitQueue.convertAndSend(queueName, compressedData);
            log.debug("Successfully sent data to queue: {}", queueName);
        } catch (IOException e) {
            log.error("Error placing request on {}: {}", queueName, e.getMessage(), e);
        }
    }
}
