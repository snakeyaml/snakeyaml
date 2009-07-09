/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive;

import java.util.LinkedHashSet;
import java.util.Set;

public class Human extends AbstractHuman {
    
    private Human father;
    private Human mother;
    private Human partner;
    private Human bankAccountOwner;
    protected Set<Human> children;
    
    public Human() {
        children = new LinkedHashSet<Human>();
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

    public Human getPartner() {
        return partner;
    }

    public void setPartner(Human partner) {
        this.partner = partner;
    }

    public Human getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(Human bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public Set<Human> getChildren() {
        return children;
    }

    public void setChildren(Set<Human> children) {
        this.children = children;
    }

}
