package server;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerTCPRodUnBus extends Thread
{
    private static ArrayList<ConnectionClientTCP> listOfClient =   new ArrayList();
    
    static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException
    {
        serverSocket = null;

        try
        {
            serverSocket = new ServerSocket(1234);
            System.out.println("Connection Socket Created");
            try
            {
                while (true)
                {
                    System.out.println("Waiting for Connection");
                    listOfClient.add(new ConnectionClientTCP(serverSocket.accept()));
                   //new ServerTCPRodUnBus(serverSocket.accept());
                }
            } catch (IOException e)
            {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e)
        {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        } finally
        {
            try
            {
                serverSocket.close();
            } catch (IOException e)
            {
                System.err.println("Could not close port: 1234.");
                System.exit(1);
            }
        }
    }

    
    public static void sendMsgToAllClient(String msg)
    {
        for(ConnectionClientTCP client: listOfClient) {
            client.sendMsg(msg);
        }
    }
}
