import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// Classe que mantem as estatisticas
class WebStats2 {
    private AtomicLong totalAccess = new AtomicLong(0);
    private AtomicInteger totalPurchases = new AtomicInteger(0);
    private AtomicInteger totalFailures = new AtomicInteger(0);
    private AtomicInteger totalNothing = new AtomicInteger(0);
    private AtomicInteger onlineUsers = new AtomicInteger(0);

    // Usuario acessa o sistema
    public void access() {
        totalAccess.incrementAndGet();
        onlineUsers.incrementAndGet();
    }

    // Usuario realiza uma compra
    public void purchase() {
        totalPurchases.incrementAndGet();
    }

    // Ocorreu uma falha
    public void failure() {
        totalFailures.incrementAndGet();
    }

    // Usuario nem compra nem falha
    public void nothing() {
        totalNothing.incrementAndGet();
    }

    // Usuario faz logout
    public void logout() {
        onlineUsers.decrementAndGet();
    }

    // Impressao das estatisticas atuais
    public void printStats() {
        System.out.println("========= Estatisticas do Sistema =========");
        System.out.println("Total de Acessos: " + totalAccess.get());
        System.out.println("Total de Compras: " + totalPurchases.get());
        System.out.println("Total de Falhas: " + totalFailures.get());
        System.out.println("Total de acessos sem compras ou falhas: " + totalNothing.get());
        System.out.println("Usuarios Online: " + onlineUsers.get());
        System.out.println("=======================================================");
    }
}

// Classe que simula acoes de um usuario no sistema
class UserSimulation2 implements Runnable {
    private WebStats2 stats;
    private Random random;

    public UserSimulation2(WebStats2 stats) {
        this.stats = stats;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            // Usuario acessa o sistema
            stats.access();

            // Simula tempo navegando
            Thread.sleep(random.nextInt(300));

            // Decide se faz compra, falha ou apenas navega
            int action = random.nextInt(3); // 0 = compra, 1 = falha, 2 = nada
            if (action == 0) {
                stats.purchase();
            } else if (action == 1) {
                stats.failure();
            } else {
                stats.nothing();
            }

            // Simula tempo antes de logout
            Thread.sleep(random.nextInt(200));

            // Usuario sai do sistema
            stats.logout();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Classe principal que executa a simulacao concorrente
public class WebStatsAtmVarMain {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Usage: java WebStatsMain number_users");
            System.exit(1);
        }

        int numUsers = Integer.valueOf(args[0]); // quantidade de threads (usuarios simultaneos)

        WebStats2 stats = new WebStats2();
        Thread[] users = new Thread[numUsers];

        // Criacao e inicializacao das threads
        for (int i = 0; i < numUsers; i++) {
            users[i] = new Thread(new UserSimulation2(stats));
            users[i].start();
        }

        // Aguarda todas as threads terminarem
        for (int i = 0; i < numUsers; i++) {
            try {
                users[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stats.printStats();
    }
}