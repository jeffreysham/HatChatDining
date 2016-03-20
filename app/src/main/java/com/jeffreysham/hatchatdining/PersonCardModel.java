package com.jeffreysham.hatchatdining;

import com.jeffreysham.hatchatdining.tindercards.CardModel;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class PersonCardModel extends CardModel {
    private String title;
    private String description;
    private int age;
    private String number;

    public PersonCardModel(String title, String description, String number, int age) {
        this.title = title;
        this.description = description;
        this.age = age;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
