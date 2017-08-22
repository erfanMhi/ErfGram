package com.ErMedia.server.ServerHandlers;

import com.ErMedia.LogicModules.DataStructure.ArrayListI;
import com.ErMedia.LogicModules.LogicComponents.Number;
import com.ErMedia.LogicModules.LogicComponents.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXTextArea;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by root on 1/10/17.
 */
public class Server extends Thread{
    private ArrayListI serverConnections;
    private boolean isStart = true ;
    private ServerSocket serverSocket;
    private ArrayList<User> users;
    private int port;
    private ArrayList<Number> numbers;
    private JFXTextArea logTA;

//    private ArrayList<Message> messages;
//
//    private ArrayList<Request> requests;

    private static Gson gson ;
    private FileWriter writer;


    public Server(int port, JFXTextArea logTA){
        gson = new Gson();
        //reading from users.jsonthis.port = port;
        serverConnections = new ArrayListI();
        this.logTA = logTA;
        this.port = port;
    }

    @Override
    public void run(){
        try {
            readingUsers();
            readingNumbers();
            serverSocket = new ServerSocket(port);
            while (isStart){
                Socket socket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(socket,this);
                serverConnection.start();
                serverConnections.add(serverConnection);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void readingUsers() throws FileNotFoundException {
        BufferedReader br = new BufferedReader( new FileReader("src/com/ErMedia/server/resources/Data/users.json"));

        Type listOfTestObject = new TypeToken<List<User>>(){}.getType();
        users = gson.fromJson(br, listOfTestObject);
        setUserIdCounter();
    }

    private void setUserIdCounter() {
        int maxId = users.get(0).getId();
        for (int i = 1; i < users.size() ; i++) {
            if(users.get(i).getId() > maxId){
                maxId = users.get(i).getId();
            }
        }
        User.setIdCounter(maxId+1);
    }

    public void readingNumbers() throws FileNotFoundException {
        BufferedReader br = new BufferedReader( new FileReader("src/com/ErMedia/server/resources/Data/numbers.json"));
        Type listOfTestObject = new TypeToken<List<Number>>(){}.getType();
        numbers = gson.fromJson(br, listOfTestObject);
    }

//    public void readingMessages() throws FileNotFoundException {
//        BufferedReader br = new BufferedReader( new FileReader("src/com/ErMedia/Data/messages.json"));
//        Type listOfTestObject = new TypeToken<List<Message>>(){}.getType();
//        messages = gson.fromJson(br, listOfTestObject);
//    }

//    public void readingRequests() throws FileNotFoundException {
//        BufferedReader br = new BufferedReader( new FileReader("src/com/ErMedia/Data/requests.json"));
//        Type listOfTestObject = new TypeToken<List<Request>>(){}.getType();
//        requests = gson.fromJson(br, listOfTestObject);
//    }

    public User getUser(String username){
        for (int i = 0; i < users.size() ; i++) {
            if(users.get(i).getName().equals(username)){
                return users.get(i);
            }
        }
        return null;
    }

    public User getUser(int id){
        for (int i = 0; i < users.size() ; i++) {
            if(users.get(i).getId()== id){
                return users.get(i);
            }
        }
        return null;
    }

    public Number getNumber(long number){
        for (int i = 0; i < numbers.size() ; i++) {
            if(numbers.get(i).getNumber() == (number)){
                return numbers.get(i);
            }
        }
        return null;
    }



    //TODO
    public void writingToFile(User user,String address){
        users.add(user);
        try {
            Type listOfTestObject = new TypeToken<List<User>>(){}.getType();
            String json = gson.toJson(users,listOfTestObject);
            writer = new FileWriter("src/com/ErMedia/Data/" + address);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            Type userType = new TypeToken<List<User>>(){}.getType();
            String userJson = gson.toJson(users,userType);
            Type numberType = new TypeToken<List<Number>>(){}.getType();
            String numberJson = gson.toJson(numbers,numberType);
            writer = new FileWriter("src/com/ErMedia/server/resources/Data/users.json");
            writer.write(userJson);
            writer.close();
            writer = new FileWriter("src/com/ErMedia/server/resources/Data/numbers.json");
            writer.write(numberJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList users) {
        this.users = users;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public ArrayListI getServerConnections() {
        return serverConnections;
    }

    public void setServerConnections(ArrayListI serverConnections) {
        this.serverConnections = serverConnections;
    }

    public void stopServer(){
        isStart = false;
    }

    public ArrayList<Number> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<Number> numbers) {
        this.numbers = numbers;
    }

    public JFXTextArea getLogTA() {
        return logTA;
    }

    public void setLogTA(JFXTextArea logTA) {
        this.logTA = logTA;
    }
}
