package GroupCommunication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Client3 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("Broadcast", "fanout");
        Scanner sc = new Scanner(System.in);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "Broadcast", "");



        Thread one = new Thread(() -> {
            while (true)
            {
                try {
                    String s = sc.nextLine();
                    channel.basicPublish("Broadcast", "", null, s.getBytes());
                    System.out.println(" [x] Sent message: " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        one.start();
    }




}
