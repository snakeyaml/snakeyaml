package examples.spring;

/**
 * @since 2016-06-06
 * @author kibertoad
 *
 */
public class TestEntity {

    //This field is supposed to be set by the means of calling {@link DataRegistry} and affects ID setting
    private transient int counter;
    
    //This field is supposed to be serialized and deserialized into YAML
    private String id;
    
    //This field is supposed to be set by the means of calling {@link DataRegistry} and is affected by ID
    private transient String data;
    
    
    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public void setId(String id) {
        this.id = counter+":"+id;
    }
    
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    public int getCounter() {
        return counter;
    }
    
}
