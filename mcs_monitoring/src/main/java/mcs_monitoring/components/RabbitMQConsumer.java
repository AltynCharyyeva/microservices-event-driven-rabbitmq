package mcs_monitoring.components;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "device-events")
    public void receiveMessage(String message)
    {
        // Handle the received message here
        System.out.println("Received message: " + message);
    }
}
