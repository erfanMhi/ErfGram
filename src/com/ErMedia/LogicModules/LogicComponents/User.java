package com.ErMedia.LogicModules.LogicComponents;

import com.ErMedia.LogicModules.DataStructure.ArrayListI;
import com.ErMedia.LogicModules.DataStructure.Queue;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 1/5/17.
 */
public class User implements Serializable{
    private int id ;
    private static int idCounter = 0;
    private String name;
    private ArrayList<Number> numbers;//LogicComponents.Number
    private ArrayListI friends ;//LogicComponents.User
    private ArrayList<Request> requests ;//LogicComponents.Request

    public User(String name){
        this.id = getIdCounter();
        setIdCounter(getIdCounter() + 1);
        this.name = name;
        friends = new ArrayListI();
        numbers = new ArrayList();
        requests = new ArrayList();
    }



    public User[] getByLevel(int level){
        Queue q = new Queue();
        boolean isVisiteds[] = new boolean[getIdCounter()];

        q.enqueue(this);
        isVisiteds[this.id] = true;

        int levelOrder = 0;
        int levelCounter = 0;
        boolean levelExist = false;
        while(q.isEmpty()){
            levelOrder = q.size();
            while (levelOrder > 0) {
                User tmp = (User)q.dequeue();
                levelOrder--;
                for(int i = 0 ; i < tmp.getFriends().size() ;i++){
                    if(!isVisiteds[((User)tmp.getFriends().get(i)).getId()]) {
                        isVisiteds[((User)tmp.getFriends().get(i)).getId()] = true;
                        q.enqueue(tmp.getFriends().get(i));
                    }
                }
            }
            levelCounter++;
            if(levelCounter == level){
                levelExist = true;
                break;
            }
        }
        if(levelExist) {
            User levelFriends[] = new User[q.size()];
            for (int i = 0; i < q.size(); i++) {
                levelFriends[i] = (User) q.dequeue();
            }
            return levelFriends;
        }
        return null;
    }

    public ArrayList<Number>  getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<Number>  numbers) {
        this.numbers = numbers;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    public void addRequest(Request request){
        requests.add(request);
    }

    public ArrayListI getFriends() {
        return friends;
    }

    public void setFriends(ArrayListI friends) {
        this.friends = friends;
    }

    public boolean addNumber(Number number){
        if(numbers.contains(number))
            return false;
        numbers.add(number);
        return  true;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        User.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
