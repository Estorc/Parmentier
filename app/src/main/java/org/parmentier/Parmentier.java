package org.parmentier;

public class Parmentier {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new Parmentier().getGreeting());
    }
}
