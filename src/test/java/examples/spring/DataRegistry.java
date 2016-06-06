package examples.spring;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a test bean that simulates storing external data that will be used for instantiating beans
 * 
 * @since 2016-06-06
 * @author kibertoad
 *
 */
public class DataRegistry {

    private AtomicInteger counter = new AtomicInteger(1);
    
    /**
     * Generates pseudodata of format "<id>]-<id>"
     * @param id
     * @return
     */
    public String getDataForId (String id) {
        return id +"-"+id;
    }

    /**
     *  Returns next unassigned counter value
     * @return
     */
    public int getNextCounterValue () {
        return counter.getAndIncrement();
    }
}
