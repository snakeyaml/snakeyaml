/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Human {
    private String name;
    private Date birthday;
    private String birthPlace;
    private Human father;
    private Human mother;
    private Human parner;
    private Human bankAccountOwner;
    private List<Human> children;

    public Human() {
        children = new LinkedList<Human>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public Human getFather() {
        return father;
    }

    public void setFather(Human father) {
        this.father = father;
    }

    public Human getMother() {
        return mother;
    }

    public void setMother(Human mother) {
        this.mother = mother;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public List<Human> getChildren() {
        return children;
    }

    public void setChildren(List<Human> children) {
        this.children = children;
    }

    public Human getParner() {
        return parner;
    }

    public void setParner(Human parner) {
        this.parner = parner;
    }

    public Human getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(Human bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }
}
