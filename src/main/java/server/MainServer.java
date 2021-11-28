package server;

import socket.serverTCP;

public class MainServer  {

    public static void main(String[] args) {
        new Thread(() -> runServer()).start();
    }

    public static void runServer() {
        try {
            serverTCP.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
