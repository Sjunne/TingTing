package MessageQueue;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class MainFrame {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        Gson gson = new Gson();


        Thread one = new Thread(() -> {
            while (true) {
                Opgave s = ConstructVeryHardJob();
                String s1 = gson.toJson(s);
                try {
                    channel.basicPublish("", "WorkQueue", null, s1.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(" [x] I have put work in the queue'");
                System.out.println(s.toStringOne());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        one.start();

    }

    private static Opgave ConstructVeryHardJob() {
        Random r = new Random();
        Opgave randomWork = new Opgave();
        randomWork.setOne(r.nextInt(10));
        randomWork.setTwo(r.nextInt(10));
        return randomWork;
    }
}
