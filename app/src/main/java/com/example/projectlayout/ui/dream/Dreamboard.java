package com.example.projectlayout.ui.dream;

public class Dreamboard {

    String name;
    byte[] image;





    public Dreamboard(String name, byte[] image){
        this.name = name;
        this.image = image;
    }

    public Dreamboard() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
