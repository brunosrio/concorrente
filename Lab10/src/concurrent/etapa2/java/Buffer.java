import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class Buffer {
    private final BlockingQueue<Integer> data = new ArrayBlockingQueue<>(100000);
    
    public void put(int value) throws InterruptedException {
        data.put(value);
        System.out.println("Inserted: " + value + " | Buffer size: " + data.size());
    }
    
    public int remove() throws InterruptedException {
        Integer value = data.poll(600, TimeUnit.MILLISECONDS);
        
        if (value != null) {
            System.out.println("Removed: " + value + " | Buffer size: " + data.size());
        } else {
            value = -1;
        }

        return value;
    }
}
