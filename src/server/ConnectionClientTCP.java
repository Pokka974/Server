/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Object.Event;
import Object.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 *
 * @author Laurent Brinon
 */
public class ConnectionClientTCP extends Thread
{

    protected Socket clientSocket;
    PrintWriter out;
    BufferedReader in;

    static final String numbers = "0123456789";
    static SecureRandom random = new SecureRandom();
    
    public ConnectionClientTCP(Socket clientSoc)
    {
        clientSocket = clientSoc;
        start();
    }

    public void run()
    {

        System.out.println("New Communication Thread Started");

        try
        {

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {

                System.out.println("Server receive : " + inputLine);

                String message[] = inputLine.split("#");

                System.out.println("Msg[1]" + message[1] + " Msg[2] " + message[2]);

                processMsg(message[1], message);

//            out.println(inputLine); 
                if (inputLine.equals("Bye."))
                {
                    break;
                }
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e)
        {

            System.err.println("Problem with Communication Server");
            System.exit(1);

        }
    }

    //Méthode pour parser les messages et traiter les ordres
    private void processMsg(String orderS, String data[])
    {

        int order = Integer.parseInt(orderS);
        
        for(int i = 0; i < data.length; i++)
        {
            System.out.println("\tRecu : data["+i+"] = " + data[i]);
        }
        
        switch (order)
        {

            case (1):
                User u = new User();                                                //Création d'un utilisateur
                u.stringToObject(data);                                             //On convertit le String reçu en Objet User
                
                System.out.println("Send user to Client : " + u.toString());
                
                Manager.UserManager.get_Instance().putUser("", u);      //On ajoute le nouvel utilisateur à la base de donnée
                
                ServerTCPRodUnBus.sendMsgToAllClient("#1#" + u.toString());         //On renvoi un message aux clients comme retour
                break;

            case (2):
                System.out.println("Ajout d'un nouvel évenement");
                
                Event newEvent = new Event();
                newEvent.stringToObject(data);
                
                newEvent.setId(generateID(numbers.length()));
                System.out.println("Event ajouté : " + newEvent.toString());
                //sendMessage("#2#Nouveau Marker ajouté");
               
                Manager.EventManager.get_Instance().putEvent("E" + newEvent.getId(), newEvent);
                
                ServerTCPRodUnBus.sendMsgToAllClient("#2#" + newEvent.toString());
                break;

            case (3):
                
                //Demande DB : getUser pour tester si l'Email et le Pseudo est unique à l'inscription
                
                if(Manager.UserManager.get_Instance().getGUserList().isEmpty())
                {
                    sendMsg("#3#ok");
                    System.out.println("list vide");
                    return;
                }
                
                ArrayList <User> allU = new ArrayList<>();
                allU = Manager.UserManager.get_Instance().getGUserList();
                
                for(User all : allU)
                {
                    if(all.getNickname().equals(data[2]) || all.getEmail().equals(data[3]))
                    {
                        sendMsg("#6#"); //message d'erreur
                    }
                    else
                    {
                        sendMsg("#3#ok");
                        System.out.println("Identifiants disponibles !");
                    }
                }
                break;

            case (4):
                
                
                // Ajout d'user dans la base de donnée
                ArrayList<User> allUs = Manager.UserManager.get_Instance().getGUserList();
                
                System.out.println("\tTentative d'ajout d'utilisateur dans la DB...");
               
                User newU = new User();
                newU.stringToObject(data);
                
                //Test si les identifiants sont disponibles
                if(!allUs.isEmpty())
                {
                    for(User uT : allUs)
                    {
                        if(uT.getNickname().equals(data[2]) || uT.getEmail().equals(data[3]))
                        {
                            sendMsg("#4#error");
                            return;
                        }
                    }
                }
                else
                    System.out.println("Aucun user dans la liste");
                
                
                newU.setId(generateID(numbers.length()));
                Manager.UserManager.get_Instance().putUser("U" + newU.getId(), newU);
                        
                System.out.println("Ajout d'un nouvel User dans la DB : " + newU.toString() + " / " + newU.getId());
               
                
                sendMsg("#4#"+newU.toString());
                
                break;
                
            case(5):
                
                //Test de connection
                System.out.println("\tTentative de connection de  : " + data[3] + " " + data[4]);
                
                boolean noUser = true;
                
                User u2 = new User();
                u2.stringToObject(data);
                
                ArrayList<User> allUsers = Manager.UserManager.get_Instance().getGUserList();
                
                for(User us : allUsers)
                {
                    if (us.getEmail().equals(u2.getEmail()) && us.getPassword().equals(u2.getPassword()))
                    {
                        sendMsg("#5#"+us.toString());
                        System.out.println("\n\t" + us.getNickname() + " = " + u2.getNickname());
                        System.out.println("\t" + us.getEmail() + " = " + u2.getEmail());
                        System.out.println("\t" + us.getPassword() + " = " + u2.getPassword());
                        System.out.println("\t" + us.getId());
                        System.out.println("\n\t*** BINGO ***\n");
                        
                        noUser = false;
                        return;
                    }
                    else
                    {
                        System.out.println(us.getNickname() + " != " + u2.getNickname());
                        System.out.println(us.getEmail() + " != " + u2.getEmail());
                        System.out.println(us.getPassword() + " != " + u2.getPassword());
                    }  
                }
                
                if(noUser)
                    sendMsg("#5#no");
              
                break;
                
                case(6):
                    
                    /* Modification Infos Utilisateur */
                    
                    
                    User editedUser = new User();
                    editedUser.stringToObject(data);
                    
                    Manager.UserManager.get_Instance().putUser("U" + editedUser.getId(), editedUser);
                        
                    System.out.println("Ajout d'un nouvel User dans la DB : " + editedUser.toString() + " / " + editedUser.getId());
                    
                    sendMsg("#6#"+ editedUser.toString());
                    break;
                
                case(7):
                    
                    String recherche = data[2];
                    System.out.println("Recherche en cours ...  --> " + recherche);
                   
                    ArrayList<User> userList = new ArrayList<>();
                    userList = Manager.UserManager.get_Instance().getGUserList();
                    
                    for(User user : userList)
                    {
                        if(user.getNickname().equals(recherche))
                        {
                            System.out.println("Utilisateur trouvé ! --> " + user.getNickname());
                            sendMsg("#7#"+user.toString());
                        }
                       
                    }
                    break;
                    
                case(8):
                    
                    /* Ajouter le nouvel ami dans la List View */
                    
                    String id = data[2];
                    
                    User newFriend = new User();
                    newFriend = Manager.UserManager.get_Instance().getUser("U"+id);
                    
                    System.out.println("User à ajouter : " + newFriend.toString());
                    
                    sendMsg("#8#"+newFriend.toString());
                    
                    break;
                    
                case (9):
                    
                    /* On envoie les marqueurs présents dans la DB */
                    
                    System.out.println("Synch des evenements...");
                    
                    if(data[2].equals("sync"))
                    {
                        ArrayList<Event> allEvent = Manager.EventManager.get_Instance().getGEventList();

                        for(Event e : allEvent)
                        {
                            sendMsg("#9#" + e.toString());
                        }
                    }
                    break;
                    
                    
                    case(10):
                        
                        /* Synchro des marqueur constants */
                        
                        Event constEvent = new Event();
                        constEvent.stringToObject(data);
                        constEvent.setId(generateID(8));
                        
                        Manager.EventManager.get_Instance().putEvent(constEvent.getId(), constEvent);
                        
                        break;
                        
                        case(12):
                            
                            /* Modification User */
                            
                            User userToedit = new User();
                            userToedit.stringToObject(data);
                            
                            Manager.UserManager.get_Instance().putUser(userToedit.getId(), userToedit);
                           
                            
                            break;
            default:
                break;

        }
    }

    public void sendMsg(String msg)
    {
        out.println(msg);
    }
    
    static String generateID(int length)
    {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            sb.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return sb.toString();
    }
}
