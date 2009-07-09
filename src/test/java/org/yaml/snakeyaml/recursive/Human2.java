/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive;

import java.util.HashMap;
import java.util.Map;

public class Human2 extends AbstractHuman {

    private Human2 father;
    private Human2 mother;
    private Human2 parner;
    private Human2 bankAccountOwner;
    protected Map<Human2, String> children;
    
    public Human2() {
        children = new HashMap<Human2, String>();
    }

    public Human2 getFather() {
        return father;
    }

    public void setFather(Human2 father) {
        this.father = father;
    }

    public Human2 getMother() {
        return mother;
    }

    public void setMother(Human2 mother) {
        this.mother = mother;
    }

    public Human2 getParner() {
        return parner;
    }

    public void setParner(Human2 parner) {
        this.parner = parner;
    }

    public Human2 getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(Human2 bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public Map<Human2, String> getChildren() {
        return children;
    }

    public void setChildren(Map<Human2, String> children) {
        this.children = children;
    }
    
}
