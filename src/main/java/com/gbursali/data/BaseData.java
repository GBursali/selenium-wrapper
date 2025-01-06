package com.gbursali.data;

public class BaseData {

    public String identifier;

    public BaseData(String identifier){
        this.identifier = identifier;
    }

    public void save(){
        DataManager.save(this);
    }
}
