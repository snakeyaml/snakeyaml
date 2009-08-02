/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicJavaBean {
    private float amount;
    private AtomicLong atomic;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public AtomicLong getAtomic() {
        return atomic;
    }

    public void setAtomic(AtomicLong atomic) {
        this.atomic = atomic;
    }

    @Override
    public String toString() {
        return "AtomicJavaBean";
    }
}