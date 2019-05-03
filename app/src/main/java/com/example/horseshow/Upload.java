package com.example.horseshow;

public class Upload {
    private String mName;
    public String mUrl;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url) {
        if(name.trim().equals("")){
            name = "No Name";
        }
        mName = name;
        mUrl= url;
    }

    public String getName() {
        return mName;
    }
    public void setName (String name){
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}

