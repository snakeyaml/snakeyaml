/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

public class Dice {
    private Integer a;
    private Integer b;

    public Dice(Integer a, Integer b) {
        super();
        this.a = a;
        this.b = b;
    }

    public Integer getA() {
        return a;
    }

    public Integer getB() {
        return b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dice) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "Dice " + a + "d" + b;
    }
}
