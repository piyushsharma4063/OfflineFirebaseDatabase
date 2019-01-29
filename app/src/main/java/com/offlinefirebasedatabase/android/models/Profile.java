package com.offlinefirebasedatabase.android.models;

public class Profile {

    String id;
    String name;
    String email;
    String number;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

    public Profile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Profile(String name, String email, String number) {
        this.name = name;
        this.email = email;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
