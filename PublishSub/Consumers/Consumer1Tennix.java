package PublishSub.Consumers;

import PublishSub.Model.FodboldMatch;
import PublishSub.Model.TennisMatch;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer1Tennix {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Gson gson = new Gson();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = channel.queueDeclare().getQueue();
        //channel.queueDeclare("Tennis", false, false, false, null);
        channel.exchangeDeclare("Tennis", "fanout");
        channel.queueBind(queueName,"Tennis","");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            TennisMatch match = gson.fromJson(message, TennisMatch.class);

            System.out.println(" [x] Received TennisMatch '" + match.getScore() + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        /*
             channel.basicConsume("Tennis", true, (s, delivery) -> {
            String m = new String(delivery.getBody(), "UTF-8");
            TennisMatch match = gson.fromJson(m, TennisMatch.class);
            System.out.println("I just recieved a TennisUpdate: " + match.getScore());
        }, s -> {
        });
         */

    }
}
