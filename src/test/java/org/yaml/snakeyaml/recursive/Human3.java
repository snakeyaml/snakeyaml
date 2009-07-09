/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive;

import java.util.LinkedList;
import java.util.List;

public class Human3 extends AbstractHuman {

    private Human3 father;
    private Human3 mother;
    private Human3 partner;
    private Human3 bankAccountOwner;
    protected List<Human3> children;
    
    public Human3() {
        children = new LinkedList<Human3>();
    }

    public Human3 getFather() {
        return father;
    }

    public void setFather(Human3 father) {
        this.father = father;
    }

    public Human3 getMother() {
        return mother;
    }

    public void setMother(Human3 mother) {
        this.mother = mother;
    }

    public Human3 getPartner() {
        return partner;
    }

    public void setPartner(Human3 partner) {
        this.partner = partner;
    }

    public Human3 getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(Human3 bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public List<Human3> getChildren() {
        return children;
    }

    public void setChildren(List<Human3> children) {
        this.children = children;
    }
    
}
