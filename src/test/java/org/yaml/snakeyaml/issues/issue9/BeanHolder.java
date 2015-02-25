/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
