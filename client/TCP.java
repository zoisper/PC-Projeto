package client;

import java.io.*;
import java.net.*;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


public class TCP {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    StringBuilder sb = new StringBuilder();

    public TCP(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
    }

    public void send(String message) {
        out.println(message);
        out.flush();
    }

    public String receive() throws IOException {
        String message = in.readLine();
        return message;
    }

    public void login(String username, String password) throws IOException, InvalidPassword, InvalidAccount {
        sb.append("login#");
        sb.append(username);
        sb.append(" ");
        sb.append(password);
        this.send(sb.toString()); // login#username password
        sb.setLength(0);

        String response = this.receive();
        switch (response) {
            case "done":
                break;
            case "invalid_account":
                throw new InvalidAccount("Invalid account.");
            case "invalid_password":
                throw new InvalidPassword("Invalid password.");
        }
    }

    public void logout(String username, String password) throws IOException, InvalidPassword, InvalidAccount {
        sb.append("logout#");
        sb.append(username);
        sb.append(" ");
        sb.append(password);
        this.send(sb.toString()); // logout#username password
        sb.setLength(0);

        String response = this.receive();
        switch (response) {
            case "done":
                break;
            case "invalid_account":
                throw new InvalidAccount("Invalid account.");
            case "invalid_password":
                throw new InvalidPassword("Invalid password.");
        }
    }

    public void create_account(String username, String password) throws IOException, InvalidPassword, UserExists {
        sb.append("create_account#");
        sb.append(username);
        sb.append(" ");
        sb.append(password);
        this.send(sb.toString()); // create_account#username password
        sb.setLength(0);

        String response = this.receive();
        switch (response) {
            case "done":
                break;
            case "user_exists":
                throw new UserExists("User already exists.");
            case "invalid_password":
                throw new InvalidPassword("Invalid password.");
        }
    }

    public void remove_account(String username, String password) throws IOException, InvalidPassword, InvalidAccount {
        sb.append("remove_account#");
        sb.append(username);
        sb.append(" ");
        sb.append(password);
        this.send(sb.toString()); // remove_account#username password
        sb.setLength(0);

        String response = this.receive();
        switch (response) {
            case "done":
                break;
            case "invalid_account":
                throw new InvalidAccount("Invalid account.");
            case "invalid_password":
                throw new InvalidPassword("Invalid password.");
        }
    }

    public Set<Tuple<String, Integer>> leaderboard() throws IOException {
        sb.append("leaderboard#");
        this.send(sb.toString()); // leaderboard#
        sb.setLength(0);

        String response = this.receive();
        String[] playerStrings = response.split("\\|");

        Comparator<Tuple<String, Integer>> comp = (a1, a2) -> (a1.second != a2.second) ? (a2.second - a1.second) : a1.first.compareTo(a2.first);
        Set<Tuple<String, Integer>> players = new TreeSet<>(comp);

        for (String playerString : playerStrings) {
            String[] playerInfo = playerString.split(" ");
            Tuple<String, Integer> newPlayer = new Tuple<>(playerInfo[0], Integer.parseInt(playerInfo[1]));
            players.add(newPlayer);
        }

        return players;
    }

    public Set<String> online() throws IOException {
        sb.append("online#");
        this.send(sb.toString());
        sb.setLength(0);

        String response = this.receive();
        String[] playerStrings = response.split(" ");
        Set<String> users = new TreeSet<>();

        for (String user : playerStrings) users.add(user);

        return users;
    }
}