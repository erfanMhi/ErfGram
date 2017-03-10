package com.ErMedia.user;

import com.ErMedia.LogicModules.LogicComponents.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ErMedia.LogicModules.LogicComponents.Number;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/11/17.
 */
public class TestInput {
    public static void main(String[] args) {
        User user = new User("FirstMan");
        Gson gson = new Gson();
        ArrayList<User> users = new ArrayList();
        user.addNumber(new Number(3342882,"FirstMan"));
        users.add(user);
        Type listOfTestObject = new TypeToken<List<User>>(){}.getType();

        String json = gson.toJson(users,listOfTestObject);
        System.out.println(((User)users.get(0)).getName());
        try {
            FileWriter writer = new FileWriter("src/com/ErMedia/Data/users.json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
