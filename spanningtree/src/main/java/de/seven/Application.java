package de.seven;

import de.seven.utils.Data;

import java.io.File;
import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Data data;
        data = new Data(
                new File(
                        Application.class
                                .getClassLoader()
                                .getResource("data.json")
                                .getFile()));
        try {
        } catch(Exception e){
            data = null;
            System.out.println(e);
            System.out.println("Something went terribly wrong!!!");
        }
        data.calculatePath();
    }
}