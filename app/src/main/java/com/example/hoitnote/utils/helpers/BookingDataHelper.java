package com.example.hoitnote.utils.helpers;

import com.example.hoitnote.models.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingDataHelper {
    public static List<String> getClassifications1() {
        return classifications1;
    }

    public static List<List<String>> getClassifications2() {
        return classifications2;
    }

    public static List<String> getAccounts() {
        return accounts;
    }

    public static List<String> getPersons() {
        return persons;
    }

    public static List<String> getStores() {
        return stores;
    }

    public static List<String> getProjects() {
        return projects;
    }

    private static List<String> classifications1 = new ArrayList<>();
    private static List<List<String>> classifications2 = new ArrayList<>();
    private static List<String> accounts = new ArrayList<>();
    private static List<String> persons = new ArrayList<>();
    private static List<String> stores = new ArrayList<>();
    private static List<String> projects = new ArrayList<>();
    static {
        classifications1.add("饮食");
        classifications1.add("娱乐");
        classifications2.add(Arrays.asList("早餐","午餐","晚餐"));
        classifications2.add(Arrays.asList("唱歌","游戏","购物"));
        accounts.add("现金");
        accounts.add("工商银行 1234");
        persons.add("我");
        persons.add("庄智俊");
        stores.add("超市");
        stores.add("公交");
        projects.add("国庆旅游");
        projects.add("回家过年");
    }

    public static void addClassification1(String classification1){
        classifications1.add(classification1);
        classifications2.add(new ArrayList<String>());
    }
    public static void addClassification2(String classfication1, String classfication2){
        classifications2.get(classifications1.indexOf(classfication1)).add(classfication2);
    }
    public static void addAccount(String account){
        accounts.add(account);
    }
    public static void addPerson(String person){
        persons.add(person);
    }
    public static void addStore(String store){
        stores.add(store);
    }
    public static void addProject(String project){
        projects.add(project);
    }

    //彳亍
}
