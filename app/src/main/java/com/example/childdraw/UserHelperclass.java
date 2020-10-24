package com.example.childdraw;

public class UserHelperclass {

    String name, username, emil, phoneNo, password;

    public UserHelperclass() {
    }

    public UserHelperclass(String name, String username, String emil, String phoneNo, String password) {
        this.name = name;
        this.username = username;
        this.emil = emil;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmil() {
        return emil;
    }

    public void setEmil(String emil) {
        this.emil = emil;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
