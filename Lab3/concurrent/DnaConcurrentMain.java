import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class DnaConcurrentMain {

    public static long countInFile(File file, String pattern) throws IOException {
        long total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    total += countInSequence(line, pattern);
                }
            }
        }
        return total;    
    }

    public static long countInSequence(String sequence, String pattern) {
        if (sequence == null || pattern == null) {
            return 0;
        }
        int n = sequence.length();
        int m = pattern.length();
        if (m == 0 || n < m) {
            return 0;
        }
        long count = 0;
        for (int i = 0; i <= n - m; i++) {
            if (sequence.regionMatches(false, i, pattern, 0, m)) {
                count++;
            }
        }
        return count;
    }

    static class Work implements Runnable {
        int id;
        File file;
        String pattern;
        Long[] sizes;

        Work(int id, File file, String pattern, Long[] sizes) {
            this.id = id;
            this.file = file;
            this.pattern = pattern;
            this.sizes = sizes;
        }

        @Override
        public void run() {
            try {
                long total = countInFile(this.file, this.pattern);
                sizes[this.id] = total;
            } catch (IOException e) {
                System.err.println("Erro ao ler arquivos: " + e.getMessage());
                System.exit(4);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java DnaSerialMain DIRETORIO_ARQUIVOS PADRAO");
            System.err.println("Exemplo: java DnaSerialMain dna_inputs CGTAA");
            System.exit(1);
        }

        String dirName = args[0];
        String pattern = args[1];

        File dir = new File(dirName);
        if (!dir.isDirectory()) {
            System.err.println("Caminho não é um diretório: " + dirName);
            System.exit(2);
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.err.println("Nenhum arquivo .txt encontrado em: " + dirName);
            System.exit(3);
        }

        Long[] sizes = new Long[files.length];
        Thread[] threads = new Thread[files.length];

        try {
            long total = 0;
            
	        for (int i = 0; i < files.length; i++) {
                Thread t = new Thread(new Work(i, files[i], pattern, sizes));
                threads[i] = t;
                t.start();
            }

            for (int i = 0; i < files.length; i++) {
                threads[i].join();
            }

            for (int i = 0; i < files.length; i++) {
                total += sizes[i];
            }

	        System.out.println("Sequência " + pattern + " foi encontrada " + total + " vezes.");
        } catch (Exception e) {
            System.err.println("Erro ao ler arquivos: " + e.getMessage());
            System.exit(4);
        }
    }
}
