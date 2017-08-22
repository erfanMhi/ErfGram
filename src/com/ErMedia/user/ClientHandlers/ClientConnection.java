package com.ErMedia.user.ClientHandlers;

import com.ErMedia.user.resources.controllers.ChatRoomController;
import com.ErMedia.LogicModules.DataStructure.ArrayListI;
import com.ErMedia.LogicModules.LogicComponents.Message;
import com.ErMedia.LogicModules.LogicComponents.Request;
import com.ErMedia.LogicModules.LogicComponents.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/10/17.
 */
public class ClientConnection extends Thread{

    private final Socket socket;
    private final Client client;

    private DataInputStream iStream;
    private DataOutputStream oStream;

    private User user;

    boolean isStart = true;

    private Gson gson ;

    public ClientConnection(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    public void sendMessage(String post) {
        try {
            oStream.writeUTF(post);
            oStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendFile(File file,String dist,long distance) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        sendMessage("file "+file.length() + " " + file.getName() + " " + dist + " " + distance);
        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            oStream.write(buffer);
        }
//        oStream.flush();
        System.out.println("end sending file");
        fis.close();

    }

    private void saveFile(String fileName,int fileSize) {
        File file = new File("src/com/ErMedia/user/recievedFiles/" +fileName);
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
            System.out.println("end saving file");
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            gson = new Gson();
            iStream = new DataInputStream(socket.getInputStream());
            oStream = new DataOutputStream(socket.getOutputStream());
            while (isStart){
                System.out.println("hereee");
                while (iStream.available() == 0 && isStart ){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(!isStart){
                    break;
                }
                System.out.println("avaible");
                String get = iStream.readUTF();
                System.out.println(get);
                checkOrder(get);
            }
            iStream.close();
            oStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkOrder(String cOrder) {
        String command = cOrder.split(" ")[0];
        String post ;
        if(command.equals("message")){
            System.out.println("message");
            post = cOrder.substring(8);
            Type messageType = new TypeToken<Message>(){}.getType();
            client.getChatRoomController().messageRecieverHandler(gson.fromJson(post,messageType));
        } else if(command.equals("file")) {
            System.out.println(cOrder);
            post = cOrder.substring(5).split(" ")[0];
            String fileName = cOrder.substring(5).split(" ")[1];
            int fileSize = Integer.parseInt(post);
            String src = cOrder.substring(5).split(" ")[2];
            saveFile(fileName,fileSize);
            client.getChatRoomController().showNotification("File Recieved","file " + fileName + " has sent from " + src);
        } else if(command.equals("login")){
            System.out.println("login");
            post = cOrder.substring(6);
            if(post.equals("notFound"))
                client.getMainController().usernameNotExist();
            else {
                System.out.println(post);
                Type userType = new TypeToken<User>(){}.getType();
                user = gson.fromJson(post, userType);
                System.out.println(user.getNumbers());
                client.getMainController().goToNumbersPage(user);
            }
        } else if(command.equals("register")){
            post = cOrder.substring(9);
            System.out.println("register");
            if(post.equals("exist")){
                client.getMainController().getRegisterController().usernameExist();
            } else {
                client.getMainController().getRegisterController().registrationIsDone();
            }
        } else if(command.equals("addNumber")){
            post = cOrder.substring(10);
            if(post.equals("duplicatedNumber")){
                client.getUserNumberListController().duplicateNumberWarner();
            } else {
                client.getUserNumberListController().updateListview(post);
            }

        } else if(command.equals("numberLogin")){
            post = cOrder.substring(12);
            System.out.println("numberLogin");
            handleNumberLogin(post);
        } else if(command.equals("sendRequest")){
            post = cOrder.substring(12);
            System.out.println("sendRequest");
            if(post.equals("notExist")){
                client.getChatRoomController().showNotification("Request","this username not exist.");
            } else if(post.equals("yourFriend")){
                client.getChatRoomController().showNotification("Request","he/she is already your friend.");
            } else {
                client.getChatRoomController().showNotification("Request" , "Request sent successfully.");
            }
        } else if(command.equals("getRequest")){
            post = cOrder.substring(11);
            System.out.println("getRequest");
            Type requestType = new TypeToken<List<Request>>(){}.getType();
            client.getChatRoomController().getUser().setRequests(gson.fromJson(post, requestType));
            client.getSettingPageController().createRequestPage();
        } else if(command.equals("acceptRequest")){
            post = cOrder.substring(14);
            System.out.println("getRequest");
            Type userType = new TypeToken<User>(){}.getType();
            client.getChatRoomController().requestAcceptHandler(gson.fromJson(post,userType));
        } else if(command.equals("levelSearch")){
            post = cOrder.substring(12);
            System.out.println("levelSearch");
            if(post.equals("notExist")){
                client.getChatRoomController().showNotification("level searching","this level not Exist.");
                client.getLevelSearchPageController().clearRequestObservable();
            } else {
                Type stringType = new TypeToken<List<String>>(){}.getType();
                ArrayList<String> usernames =gson.fromJson(post, stringType);
                client.getLevelSearchPageController().setRequestLvObservable(ArrayListI.toStringArray(usernames));
            }
        } else if(command.equals("pathFinder")){
            post = cOrder.substring(11);
            System.out.println("search");
            if(post.equals("notFound")){
                client.getChatRoomController().showNotification("Path Finder","There is no path to this user.");
            } else if(post.equals("distNotExist")) {
                client.getChatRoomController().showNotification("Path Finder","This user not exist");
            } else {
                ArrayListI paths = gson.fromJson(post,ArrayListI.class);
                client.getShowPathPageController().showPath(paths);
            }
        }
    }

    private void handleNumberLogin(String post) {
        Type userType = new TypeToken<List<User>>(){}.getType();
        ChatRoomController.setFriends(gson.fromJson(post, userType));
        client.getUserNumberListController().chatRoomCreator();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void terminate(){
        this.isStart = false;
    }
}
