/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.issues.issue9;

import org.springframework.core.style.ToStringCreator;

public class BeanHolder {

    private IBean bean = new Bean1();

    public BeanHolder() {
        super();
    }

    public BeanHolder(IBean bean) {
        super();
        this.bean = bean;
    }

    public IBean getBean() {
        return bean;
    }

    public void setBean(IBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append(this.bean);
        return builder.toString();
    }
}
