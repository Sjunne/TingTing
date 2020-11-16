package PublishSub;

import PublishSub.Model.FodboldMatch;
import PublishSub.Model.TennisMatch;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Publisher {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        Gson gson = new Gson();


        //channel.queueDeclare("Fodbold", false, false, false, null);
        //channel.queueDeclare("Tennis", false, false, false, null);
        channel.queueDeclare("Bowling", false, false, false, null);
        channel.exchangeDeclare("Tennis", "fanout");
        channel.exchangeDeclare("Fodbold", "fanout");


        Thread one = new Thread(() -> {
            while (true)
            {
                String s = makeFootballMatch();
                try {
                    channel.basicPublish("Fodbold", "", null, s.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(" [x] Sent fodboldMatch Result'");
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        one.start();

        Thread two = new Thread(() -> {
            while(true)
            {
                String s = makeTennisMatch();
                try {
                    channel.basicPublish("Tennis", "", null, s.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(" [x] Sent TennisMatch Result'");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        two.start();

    }

    private static String makeTennisMatch() {
        Gson gson = new Gson();
        Random m = new Random();
        ArrayList<String> teams = new ArrayList<>();
        teams.add("Danmark");
        teams.add("Holland");
        teams.add("Spanien");
        teams.add("Sverige");
        teams.add("Norge");

        String team1 = teams.get(m.nextInt(5));
        String team2 = teams.get(m.nextInt(5));
        String score =  team1 +  "=" + m.nextInt(7) + "vs" + team2 + "=" + m.nextInt(6);
        TennisMatch match = new TennisMatch(score);
        return gson.toJson(match);
    }

    private static String makeFootballMatch() {
        Gson gson = new Gson();
        Random m = new Random();
        ArrayList<String> teams = new ArrayList<>();
        teams.add("Danmark");
        teams.add("Holland");
        teams.add("Spanien");
        teams.add("Sverige");
        teams.add("Norge");

        String team1 = teams.get(m.nextInt(5));
        String team2 = teams.get(m.nextInt(5));
        String score =  team1 +  "=" + m.nextInt(7) + "vs" + team2 + "=" + m.nextInt(6);
        FodboldMatch match = new FodboldMatch(score);
        return gson.toJson(match);
    }
}
