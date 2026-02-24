import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class Task implements Callable<Resposta> {

    String filename;
    int total200;
    int total500;

    Task(String filename) {
        this.filename = filename;
    }

    public void run() {
            try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length == 3) {
                        String code = parts[2];
                        if (code.equals("200")) {
                            total200++;
                        } else if (code.equals("500")) {
                            total500++;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler arquivo: " + this.filename);
                e.printStackTrace();
            }
    }

    @Override
    public Resposta call() throws Exception {
        this.run();
        return new Resposta(this.total200, this.total500);   
    }


}

class Resposta {
    int total200;
    int total500;

    Resposta(int total200, int total500) {
        this.total200 = total200;
        this.total500 = total500;
    }
}

public class LogAnalyzer4 {

    static int total200;
    static int total500;
    
    static List<Future<Resposta>> retorno = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        if (args.length == 0) {
            System.out.println("Uso: java LogAnalyzer <arquivos_de_log>");
            System.exit(1);
        }
        
        ExecutorService executor = Executors.newCachedThreadPool();

        for (String fileName : args) {
            System.out.println("Processando arquivo: " + fileName);

            Future<Resposta> tarefa = executor.submit(new Task(fileName));
            retorno.add(tarefa);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(10000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Future<Resposta> r : retorno) {
            total200 += r.get().total200;
            total500 += r.get().total500;
        }

        System.out.println("===== RESULTADO FINAL =====");
        System.out.println("Total 200: " + total200);
        System.out.println("Total 500: " + total500);
    }
}
