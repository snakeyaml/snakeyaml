/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.util.LinkedHashMap;

public class EnumBean {
    private int id;
    private Suit suit;
    private LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();

    public LinkedHashMap<Suit, Integer> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<Suit, Integer> map) {
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }
}
