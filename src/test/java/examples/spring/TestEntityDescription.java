package examples.spring;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.nodes.Node;

/**
 * This description is supposed to work like this:
 * 
 * 1) Counter is retrieved from dataRegistry and is set before deserializing data from YAML;
 * 2) Id is deserialized from YAML, using standard SnakeYAML logic for Strings and is impacted by Counter that was set before;
 * 3) Data is being retrieved from injected DataRegistry bean using the id;
 * 
 * @since 2016-06-06
 * @author kibertoad
 *
 */
public class TestEntityDescription extends TypeDescription{

    private DataRegistry dataRegistry;
    
    public TestEntityDescription() {
        super(TestEntity.class, TestEntity.class);
    }
    
    @Override
    public Object newInstance(Node node) {
        TestEntity entity = (TestEntity) super.newInstance(node);
        entity.setCounter(dataRegistry.getNextCounterValue());
        return entity;
    }
    
    @Override
    public Object finalizeConstruction(Object obj) {
        TestEntity entity = (TestEntity) super.finalizeConstruction(obj);
        entity.setData(dataRegistry.getDataForId(entity.getId()));
        return entity;
    }
    
    public void setDataRegistry(DataRegistry dataRegistry) {
        this.dataRegistry = dataRegistry;
    }
}
