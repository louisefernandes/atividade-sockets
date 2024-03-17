package com.gugawag.so.ipc;

import java.net.*;
import java.io.*;
import java.util.Date;

public class DateMultiClientServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6013)) {
            System.out.println("=== Servidor iniciado ===\n");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Servidor recebeu comunicação do ip: " + clientSocket.getInetAddress() + "-" + clientSocket.getPort());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                out.println(new Date().toString() + " - Louise Fernandes");

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("O cliente me disse:" + line);
                }
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }
}
