/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.math.BigInteger;
import java.util.Date;

public class TestBean1 {
    private String text;
    private String id;
    private Byte byteClass;
    private byte bytePrimitive;
    private Short shortClass;
    private short shortPrimitive;
    private Integer integer;
    private int intPrimitive;
    private Long longClass;
    private long longPrimitive;
    private Boolean booleanClass;
    private boolean booleanPrimitive;
    private Character charClass;
    private char charPrimitive;
    private BigInteger bigInteger;
    private Float floatClass;
    private float floatPrimitive;
    private Double doubleClass;
    private double doublePrimitive;
    private Date date;
    public String publicField;
    static public Integer staticInteger;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public int getIntPrimitive() {
        return intPrimitive;
    }

    public void setIntPrimitive(int intPrimitive) {
        this.intPrimitive = intPrimitive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Byte getByteClass() {
        return byteClass;
    }

    public void setByteClass(Byte byteClass) {
        this.byteClass = byteClass;
    }

    public byte getBytePrimitive() {
        return bytePrimitive;
    }

    public void setBytePrimitive(byte bytePrimitive) {
        this.bytePrimitive = bytePrimitive;
    }

    public Short getShortClass() {
        return shortClass;
    }

    public void setShortClass(Short shortClass) {
        this.shortClass = shortClass;
    }

    public short getShortPrimitive() {
        return shortPrimitive;
    }

    public void setShortPrimitive(short shortPrimitive) {
        this.shortPrimitive = shortPrimitive;
    }

    public Long getLongClass() {
        return longClass;
    }

    public void setLongClass(Long longClass) {
        this.longClass = longClass;
    }

    public long getLongPrimitive() {
        return longPrimitive;
    }

    public void setLongPrimitive(long longPrimitive) {
        this.longPrimitive = longPrimitive;
    }

    public Boolean getBooleanClass() {
        return booleanClass;
    }

    public void setBooleanClass(Boolean booleanClass) {
        this.booleanClass = booleanClass;
    }

    public boolean isBooleanPrimitive() {
        return booleanPrimitive;
    }

    public void setBooleanPrimitive(boolean booleanPrimitive) {
        this.booleanPrimitive = booleanPrimitive;
    }

    public Character getCharClass() {
        return charClass;
    }

    public void setCharClass(Character charClass) {
        this.charClass = charClass;
    }

    public char getCharPrimitive() {
        return charPrimitive;
    }

    public void setCharPrimitive(char charPrimitive) {
        this.charPrimitive = charPrimitive;
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public Float getFloatClass() {
        return floatClass;
    }

    public void setFloatClass(Float floatClass) {
        this.floatClass = floatClass;
    }

    public float getFloatPrimitive() {
        return floatPrimitive;
    }

    public void setFloatPrimitive(float floatPrimitive) {
        this.floatPrimitive = floatPrimitive;
    }

    public Double getDoubleClass() {
        return doubleClass;
    }

    public void setDoubleClass(Double doubleClass) {
        this.doubleClass = doubleClass;
    }

    public double getDoublePrimitive() {
        return doublePrimitive;
    }

    public void setDoublePrimitive(double doublePrimitive) {
        this.doublePrimitive = doublePrimitive;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}