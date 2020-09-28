package com.example.hoitnote.utils.commuications;

import com.example.hoitnote.models.Tally;

import java.util.ArrayList;
/*
* 定义了整体数据：
* 1. 配置相关数据 config
* 2. 所有账单数据 allTallies
* */
public class DataPackage {
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
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

    public DataPackage(Config config, ArrayList<Tally> allTallies) {
        this.config = config;
        this.allTallies = allTallies;
    }
}
