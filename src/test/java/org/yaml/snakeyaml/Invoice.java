/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.util.List;

public class Invoice {
    public Integer invoice; // invoice
    public String date; // date
    public Person billTo;// bill-to
    public Person shipTo;// ship-to
    public List<Product> product;
    public Float tax;
    public Float total;
    public String comments;

}