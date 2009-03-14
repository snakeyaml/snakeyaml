/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

public class Hero {
    private String name;
    private Integer sp;
    private Integer hp;

    public Hero(String name, Integer hp, Integer sp) {
        super();
        this.name = name;
        this.sp = sp;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public Integer getSp() {
        return sp;
    }

    public Integer getHp() {
        return hp;
    }

}
