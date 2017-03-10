package com.ErMedia.user;

import com.ErMedia.LogicModules.LogicComponents.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by root on 1/11/17.
 */
public class TestOutput {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader( new FileReader("src/com/ErMedia/Data/users.json"));
            Type listOfTestObject = new TypeToken<List<User>>(){}.getType();
            List<User> users = gson.fromJson(br, listOfTestObject);
            System.out.println(users.get(0).getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
