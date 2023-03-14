package de.seven;

import de.seven.utils.Data;

import java.io.File;

public class Application {
    public static void main(String[] args) {
        Data data;
        try {
            data = new Data(
                    new File(
                            Application.class
                                    .getClassLoader()
                                    .getResource("data.json")
                                    .getFile()));
        } catch(Exception e){
            data = null;
            System.out.println("Something went terribly wrong!!!");
        }
        data.calculatePath();
        System.out.println("test");
    }
}