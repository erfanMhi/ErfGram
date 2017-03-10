package com.ErMedia.server.ServerHandlers;

import com.ErMedia.LogicModules.DataStructure.ArrayListI;
import com.ErMedia.LogicModules.DataStructure.Queue;
import com.ErMedia.LogicModules.LogicComponents.Message;
import com.ErMedia.LogicModules.LogicComponents.Number;
import com.ErMedia.LogicModules.LogicComponents.Request;
import com.ErMedia.LogicModules.LogicComponents.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 1/10/17.
 */
public class ServerConnection extends Thread{
    private Server server;
    private Socket socket;

    private DataInputStream iStream;
    private DataOutputStream oStream;

    private User user;

    private Number number ;

    private Gson gson ;
    private boolean isStart = true;

    public ServerConnection(Socket socket,Server server){
        super("SocialNetworkServer");
        this.number = null;
        this.user = null;
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            gson = new Gson();
            iStream = new DataInputStream(socket.getInputStream());
            oStream = new DataOutputStream(socket.getOutputStream());
            while (isStart){
                System.out.println("hereee");
                while (iStream.available() == 0 ){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("available");
                String cOrder = iStream.readUTF();
                System.out.println(cOrder);
                checkOrder(cOrder);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close(){
        try {
            iStream.close();
            oStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkOrder(String cOrder) {
        String command = cOrder.split(" ")[0];
        String post ;


        if(command.equals("message")){
            System.out.println("message");
            post = cOrder.substring(8);
            handleMessage(post);
        } else if(command.equals("file")) {
            System.out.println(cOrder);
            post = cOrder.substring(5).split(" ")[0];
            String fileName = cOrder.substring(5).split(" ")[1];
            String dist = cOrder.substring(5).split(" ")[2];
            long distNumber = Long.parseLong(cOrder.substring(5).split(" ")[3]);
            server.getLogTA().appendText("\n" + user.getName() + "sent " + fileName + " file to" + dist);
            int fileSize = Integer.parseInt(post);
            File file = saveFile(fileName,fileSize);
            handleSendingFile(file,distNumber);
        } else if(command.equals("numberLogin")){
            post = cOrder.substring(12);
            System.out.println("numberLogin");
            handleNumberLogin(post);
        } else if(command.equals("sendRequest")){
            post = cOrder.substring(12);
            System.out.println("sendRequest");
            handleSendRequest(post);
        } else if(command.equals("getRequest")){
            post = cOrder.substring(11);
            System.out.println("getRequest");
            handleGetRequest();
        } else if(command.equals("acceptRequest")){
            post = cOrder.substring(14);
            System.out.println("acceptRequest");
            handleRequestAcception(post);
        } else if(command.equals("rejectRequest")){
            post = cOrder.substring(14);
            System.out.println("rejectRequest");
            handleRequestRejection(post);
        } else if(command.equals("levelSearch")){
            post = cOrder.substring(12);
            System.out.println("levelSearch");
            handleLevelSearch(Integer.parseInt(post));
        } else if(command.equals("pathFinder")){
            System.out.println("pathFinder");
            post = cOrder.substring(11);
            handlePathFinder(post);
        } else if(command.equals("addNumber")){
            post = cOrder.substring(10);
            System.out.println("addNumber");
            handleNumberAdding(post);
        } else if(command.equals("register")){
            System.out.println("register");
            post = cOrder.substring(9);
            handleRegistering(post);
        } else if(command.equals("login")){
            System.out.println("login");
            post = cOrder.substring(6);
            handleLogin(post);
        }
    }

    private void handleSendingFile(File file,long dist) {
        for (int i = 0; i < server.getServerConnections().size(); i++) {
            System.out.println("sendfile " + dist + " " +((ServerConnection)server.getServerConnections().get(i)).getUser().getName());
            if(((ServerConnection)server.getServerConnections().get(i)).getNumber().getNumber() == (dist)) {
                try {
                    ((ServerConnection)server.getServerConnections().get(i)).sendFile(file,user.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
        }
        System.out.println("sending file too user is ended");
    }

    public void sendFile(File file,String src) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        sendMessage("file "+file.length() + " " + file.getName() + " " + src);
        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            oStream.write(buffer);
        }
//        oStream.flush();
        System.out.println("end sending file");
        fis.close();
    }

    private File saveFile(String fileName,int fileSize) {
        File file = new File("src/com/ErMedia/server/recievedFiles/" + fileName);
        if(file.exists())
            for (int i = 1; file.exists(); i++) {
                file = new File("src/com/ErMedia/user/recievedFiles/" + fileName + "(" + i + ")");
            }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];

//        int filesize = 15123; // Send file size in separate msg
            int read = 0;
//            int totalRead = 0;
            int remaining = fileSize;
            while((read = iStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//                totalRead += read;
                remaining -= read;
//                System.out.println("read " + totalRead + " bytes.");
                fos.write(buffer, 0, read);
            }

            while (iStream.available() != 0)
                iStream.read();

            fos.close();
            System.out.println("end recieving file");
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void handlePathFinder(String post) {
        server.getLogTA().appendText("\n" + user.getName() + "wants all path to " + post );
        boolean visited[] = new boolean[server.getUsers().size()];
        ArrayListI paths = new ArrayListI();
        User dist = server.getUser(post);
        if(dist != null) {
            paths = showAllPath(user, dist, visited, paths, user.getName());
            if(!paths.isEmpty()){
                sendMessage("pathFinder " + gson.toJson(paths));
            } else {
                sendMessage("pathFinder notFound");
            }
        } else  {
            sendMessage("pathFinder distNotExist");
        }

    }


    public ArrayListI showAllPath(User src, User dist, boolean visited[], ArrayListI paths, String s){
        visited[src.getId()] = true;
        if(src.getId() == dist.getId()) {
            paths.add(s);
            return paths;
        }
        User friend ;
        for (int i = 0; i < src.getFriends().size(); i++) {
            friend = server.getUser((String)src.getFriends().get(i));
            if (!visited[friend.getId()])
                showAllPath(friend,dist, Arrays.copyOf(visited,server.getUsers().size()),paths,s + " -> " + friend.getName());

        }

        return paths;
    }

    private void handleMessage(String post) {
        System.out.println(post);
        Type messageType = new TypeToken<Message>(){}.getType();
        Message message = gson.fromJson(post,messageType);
        //updating numberList
        Number tempNumI = server.getNumber(message.getDist());
        Number tempNumII = server.getNumber(message.getSrc());
        messageModifier(tempNumI,message.getSrc(),message);
        messageModifier(tempNumII,message.getDist(),message);
        //updating number of userList
        userListMessageModifier(server.getUser(tempNumI.getUser()),message,message.getDist());
        userListMessageModifier(server.getUser(tempNumII.getUser()),message,message.getSrc());
        sendMessagePv(message);
        server.updateData();

    }

    private void userListMessageModifier(User user,Message message,long number){
        Number tempNum = findUserNumber(user,number);
        if(message.getDist() == number){
            messageModifier(tempNum,message.getSrc(),message);
        } else {
            messageModifier(tempNum,message.getDist(),message);
        }


    }

    private Number findUserNumber(User user,long number){
        for (int i = 0; i < user.getNumbers().size() ; i++) {
            if(user.getNumbers().get(i).getNumber() == number){
                return user.getNumbers().get(i);
            }
        }
        return null;
    }

    private void messageModifier(Number dist,long src,Message message) {
        for (int i = 0; i < dist.getMessages().size() ; i++) {
            if(dist.getMessages().get(i).getSrc() == src || dist.getMessages().get(i).getDist() == src){
                dist.getMessages().set(i,message);
                return;
            }
        }
        dist.getMessages().add(message);
    }

    private void sendMessagePv(Message message) {
        long distination;
        if(message.getDist() == number.getNumber()) {
            distination = message.getSrc();
        } else {
            distination = message.getDist();
        }
        server.getLogTA().appendText("\n" +"message by " +
                user.getName() + "(" + number.getNumber() + ") to " +
                server.getNumber(distination).getUser() + "(" + distination + ")" );
        for (int i = 0; i < server.getServerConnections().size() ; i++) {
            if (((ServerConnection)server.getServerConnections().get(i)).getNumber() != null)
            if(((ServerConnection)server.getServerConnections().get(i)).getNumber().getNumber() == distination){
                Type messageType = new TypeToken<Message>(){}.getType();
                ((ServerConnection)server.getServerConnections().get(i)).sendMessage("message " + gson.toJson(message,messageType));
                return;
            }
        }
    }


    private void handleLevelSearch(int level) {
        server.getLogTA().appendText("\n" + user.getName() + " wants to see his level " + level + "users in graph of his firendships");
        Queue q = new Queue();
        boolean isVisiteds[] = new boolean[server.getUsers().size()];
        q.enqueue(user);
        System.out.println("here");
        isVisiteds[user.getId()] = true;
        int levelOrder = 0;
        int levelCounter = 0;
        boolean levelExist = false;
        while(!q.isEmpty()){
            System.out.println("here");
            levelOrder = q.size();
            User tmpUser;
            while (levelOrder > 0) {
                User tmp = (User)q.dequeue();
                levelOrder--;
                for(int i = 0 ; i < tmp.getFriends().size() ;i++){
                    tmpUser = server.getUser((String)tmp.getFriends().get(i));
                    if(!isVisiteds[tmpUser.getId()]) {
                        isVisiteds[tmpUser.getId()] = true;
                        q.enqueue(tmpUser);
                    }
                }
            }
            levelCounter++;
            System.out.println(levelCounter);
            if(levelCounter == level && !q.isEmpty()){
                levelExist = true;
                break;
            }

        }
        if(levelExist) {
            ArrayList<String> levelUsers = new ArrayList();
            while (!q.isEmpty()){
                levelUsers.add(((User) q.dequeue()).getName());
            }
            Type userType = new TypeToken<List<String>>(){}.getType();
            sendMessage("levelSearch " + gson.toJson(levelUsers,userType));
        } else {
            sendMessage("levelSearch " + "notExist");
        }
    }

    private void handleRequestRejection(String post) {
        server.getLogTA().appendText("\n" + user.getName() + " reject request from " + post);
        requestRemover(post);
        server.updateData();
    }

    private void handleRequestAcception(String post) {
        //remove request from database
        requestRemover(post);
        User distUser = server.getUser(post);
        Type userType = new TypeToken<User>(){}.getType();
        server.getLogTA().appendText("\n" + user.getName() + " accept request from " + post );
        sendMessage("acceptRequest " + gson.toJson(distUser,userType));
        //check if another user is online ,if he was online he will be notified and his friendslist will be update
        for (int i = 0; i < server.getServerConnections().size(); i++) {
            if (((ServerConnection)server.getServerConnections().get(i)).getUser() != null)
            if(((ServerConnection)server.getServerConnections().get(i)).getUser().getName().equals(post)){
                ((ServerConnection)server.getServerConnections().get(i)).sendMessage("acceptRequest " + gson.toJson(user,userType));
                break;
            }
        }
        user.getFriends().add(post);
        distUser.getFriends().add(user.getName());
        server.updateData();
    }

    private void requestRemover(String post){
        User distUser = server.getUser(post);
        for (int i = 0; i < user.getRequests().size(); i++) {
            if(user.getRequests().get(i).getDist().equals(user.getName()) && user.getRequests().get(i).getSrc().equals(post))
                user.getRequests().remove(i);
        }
        for (int i = 0; i < distUser.getRequests().size(); i++) {
            if(distUser.getRequests().get(i).getDist().equals(user.getName()) && distUser.getRequests().get(i).getSrc().equals(post))
                distUser.getRequests().remove(i);
        }
    }

    private void handleSendRequest(String post) {
        server.getLogTA().appendText("\n" + user.getName() + " requests to " + post);
        User distUser = server.getUser(post);
        if(distUser == null){
            sendMessage("sendRequest notExist");
        } else {
            for (int i = 0; i < user.getFriends().size(); i++) {
                if(((String)user.getFriends().get(i)).equals(post)){
                    sendMessage("sendRequest yourFriend");
                    return;
                }
            }
            sendMessage("sendRequest successfull");
            Request request = new Request(user.getName(),post);
            user.addRequest(request);
            distUser.addRequest(request);
            server.updateData();
        }

    }

    //when client want to see who send it request before
    public void handleGetRequest(){
        Type requestType = new TypeToken<List<Request>>(){}.getType();
        server.getLogTA().appendText("\n" + user.getName() + " wants his requests");
        sendMessage("getRequest " + gson.toJson(user.getRequests(),requestType));

    }

    private void handleNumberLogin(String post) {
        number = server.getNumber(Long.parseLong(post));
        ArrayList<User> friends = new ArrayList<>();
        for (int i = 0; i < user.getFriends().size(); i++) {
            friends.add(server.getUser((String)user.getFriends().get(i)));
        }
        server.getLogTA().appendText("\n" + user.getName() + " login with number " + number.getNumber());
        Type userType = new TypeToken<List<User>>(){}.getType();
        sendMessage("numberLogin " + gson.toJson(friends,userType));
    }

    private void handleNumberAdding(String post) {
        //checking if this number is already exist or not
        for (int i = 0; i < server.getNumbers().size(); i++) {
            if((Long.parseLong(post) == server.getNumbers().get(i).getNumber())){
                server.getLogTA().appendText("\n" + user.getName() + " requests for adding number " + post + " but this number is duplicated");
                sendMessage("addNumber duplicatedNumber");
                return;
            }
        }
        //if this number is not exist create a number
        server.getLogTA().appendText("\n" + user.getName() + " add number " + post);
        sendMessage("addNumber " + post);
        Number tmpNumber = new Number(Long.parseLong(post),user.getName());
        server.getNumbers().add(tmpNumber);
        user.addNumber(tmpNumber);
        server.updateData();
    }

    //when client send his name for registering
    private void handleRegistering(String post) {
        //search users to findout this username name is already exist or not
        for (int i = 0; i < server.getUsers().size() ; i++) {
            if(server.getUsers().get(i).getName().equals(post)) {
                server.getLogTA().appendText("\n" + "client request for registering with "+ post + " name but this user is already exist.");
                sendMessage("register exist");//sending user detail to client
                return;
            }
        }
        //if this username not exist create a user with this name and notify client
        user = new User(post);
        server.writingToFile(user , "users.json");
        server.getLogTA().appendText("\n" + post + " registered");
        sendMessage("register successfull");
    }

    //when client click login button we recieve the username then reply to him
    private void handleLogin(String post){
        //search users to find the matched username and send it to client
        for (int i = 0; i < server.getUsers().size() ; i++) {
           if(server.getUsers().get(i).getName().equals(post)) {
               user = server.getUsers().get(i);
               Type userType = new TypeToken<User>(){}.getType();
               server.getLogTA().appendText("\n" + post + " login");
               sendMessage("login " + gson.toJson(user,userType));//sending user detail to client
               return;
           }
        }
        //if no username with this name not found
        server.getLogTA().appendText("\n" + post + " requests for login but this username not registerd yet");
        sendMessage("login notFound");
    }

    public void sendMessage(String post) {
        try {
            oStream.writeUTF(post);
            oStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }
}
