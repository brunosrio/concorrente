import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Buffer {
    private final BlockingQueue<Integer> data = new ArrayBlockingQueue<>(100);
    
    public void put(int value) throws InterruptedException {
        data.put(value);
        System.out.println("Inserted: " + value + " | Buffer size: " + data.size());
    }
    
    public int remove() throws InterruptedException {
        int value = data.take();
        System.out.println("Removed: " + value + " | Buffer size: " + data.size());

        return value;
    }
}
