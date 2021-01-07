package MessageQueue;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkerBee2 {
        public static void main(String[] args) throws IOException, TimeoutException {
            ConnectionFactory factory = new ConnectionFactory();
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            Gson gson = new Gson();
            channel.queueDeclare("WorkQueue", false, false, false, null);


            Thread one = new Thread(() -> {
                while (true) {
                    try {
                        channel.basicConsume("WorkQueue", true, (s, delivery) -> {
                            String m = new String(delivery.getBody(), "UTF-8");
                            Opgave opgave = gson.fromJson(m, Opgave.class);
                            opgave.sum = opgave.one - opgave.two;
                            System.out.println("I just finished a task: " + opgave.sum);
                        }, s -> {
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            one.start();

        }
    }

