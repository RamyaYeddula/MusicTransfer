package com.example.Ramya.MusicTransfer;

public class Music {
    //private variables
    int _id;
    String _name;
    String _owner;

    // Empty constructor
    public Music(){

    }
    // constructor
    public Music(int id, String name, String _owner){
        this._id = id;
        this._name = name;
        this._owner = _owner;
    }

    // constructor
    public Music(String name, String _owner){
        this._name = name;
        this._owner = _owner;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting owner
    public String getOwner(){
        return this._owner;
    }

    // setting owner
    public void setOwner(String owner){
        this._owner = owner;
    }

    @Override
    public String toString(){
        return this._name + "  " + this._owner;
    }
}

