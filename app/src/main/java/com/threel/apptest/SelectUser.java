package com.threel.apptest;

public class SelectUser {
    String id,name,surname,selected;

    public SelectUser() {
    }

    public SelectUser(String id, String name, String surname, String selected) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
