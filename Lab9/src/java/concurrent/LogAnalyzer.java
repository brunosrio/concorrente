import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class Task implements Runnable {

    String filename;
    int[] contador;
    Semaphore mutex200;
    Semaphore mutex500;

    Task(String filename, Semaphore mutex200, Semaphore mutex500, int[] contador) {
        this.filename = filename;
        this.mutex200 = mutex200;    
        this.mutex500 = mutex500;    
        this.contador = contador;
    }

    @Override
    public void run() {
            try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length == 3) {
                        String code = parts[2];
                        if (code.equals("200")) {
                            this.mutex200.acquire();
                            this.contador[0]++;
                            this.mutex200.release();
                        } else if (code.equals("500")) {
                            this.mutex500.acquire();
                            this.contador[1]++;
                            this.mutex500.release();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler arquivo: " + this.filename);
                e.printStackTrace();
            } catch (InterruptedException e) {
                 e.printStackTrace();
            }
    }

}

public class LogAnalyzer {

    static Semaphore mutex200 = new Semaphore(1);
    static Semaphore mutex500 = new Semaphore(1);
    static int[] contador = {0, 0};

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java LogAnalyzer <arquivos_de_log>");
            System.exit(1);
        }

        List<Thread> threads = new ArrayList<>();

        for (String fileName : args) {
            System.out.println("Processando arquivo: " + fileName);
            Thread t = new Thread(new Task(fileName, mutex200, mutex500, contador));
            threads.add(t);
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("===== RESULTADO FINAL =====");
        System.out.println("Total 200: " + contador[0]);
        System.out.println("Total 500: " + contador[1]);
    }
}
