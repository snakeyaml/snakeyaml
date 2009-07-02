package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;

public abstract class AbstractConstruct implements Construct {

    public void construct2ndStep(Node node, Object data) {
        assert node.isTwodStepsConstruction();
        throw new IllegalStateException("Not Implemented in " + getClass().getName());
    }

}
