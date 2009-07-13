/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.util.Date;

public abstract class AbstractHumanGen<T, K extends AbstractHumanGen<T, ?>> {
    private String name;
    private Date birthday;
    private String birthPlace;
    private K father;
    private K mother;
    private K partner;
    private K bankAccountOwner;
    protected T children;

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

    public K getFather() {
        return father;
    }

    public void setFather(K father) {
        this.father = father;
    }

    public K getMother() {
        return mother;
    }

    public void setMother(K mother) {
        this.mother = mother;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public T getChildren() {
        return children;
    }

    public void setChildren(T children) {
        this.children = children;
    }

    public K getPartner() {
        return partner;
    }

    public void setPartner(K partner) {
        this.partner = partner;
    }

    public K getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(K bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }
    
}
