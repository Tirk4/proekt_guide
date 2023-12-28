package com.example.proekt;

import java.io.Serializable;
import java.util.Random;

public class Note implements Serializable {
    private static int getRandomId() {
        Random random = new Random();


        int id = random.nextInt();
        int finalId = id;
        while (DataBase.getInstance().getAll().stream().anyMatch(note -> note.getId() == finalId)) {
            id = random.nextInt();
        }
        return id;
    }

    public float rate;
    public String title;
    public String theme;
    public String text;

    public double coordX;
    public double coordY;
    public String date;
    private int i;


    public Note(int id, float rate, String title, String theme, String text, String date, double coordX, double coordY) {
        this.i = id;
        this.rate = rate;
        this.title = title;
        this.theme = theme;
        this.text = text;
        this.date = date;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Note(float rate, String title, String theme, String text, String date, double coordX, double coordY) {
        this.i = getRandomId();
        this.rate = rate;
        this.title = title;
        this.theme = theme;
        this.text = text;
        this.date = date;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public int getId() {
        return i;
    }

    public float getRate() {
        return rate;
    }

    public String getTitle() {
        return title;
    }

    public String getTheme() {
        return theme;
    }

    public String getText() {
        return text;
    }

    public double getCoordX() {
        return coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public String getDate() {
        return date;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

}
