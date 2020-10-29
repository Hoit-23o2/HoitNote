package com.example.hoitnote.utils.commuications;

import com.example.hoitnote.models.Tally;

import java.io.Serializable;
import java.util.ArrayList;
/*
* 定义了整体数据：
* 1. 配置相关数据 config
* 2. 所有账单数据 allTallies
* */
public class DataPackage implements Serializable {
    private ArrayList<Config> configs;

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<Config> configs) {
        this.configs = configs;
    }

    private ArrayList<Tally> allTallies;

    public ArrayList<Tally> getAllTallies() {
        return allTallies;
    }

    public void setAllTallies(ArrayList<Tally> allTallies) {
        this.allTallies = allTallies;
    }

    public DataPackage(){

    }

    public DataPackage(ArrayList<Config> configs, ArrayList<Tally> allTallies) {
        this.configs = configs;
        this.allTallies = allTallies;
    }
}
