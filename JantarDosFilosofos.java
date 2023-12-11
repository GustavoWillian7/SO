import java.util.concurrent.Semaphore;

public class JantarDosFilosofos {
    private static final int N = 5;
    private static final int PENSANDO = 0;
    private static final int FAMINTO = 1;
    private static final int COMENDO = 2;

    private static int[] state = new int[N]; // Array para acompanhar o estado de cada filósofo
    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore[] s = new Semaphore[N]; // Um semáforo para cada filósofo
    private static int[] refeicoes = new int[N]; // Número de refeições de cada filósofo

    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            s[i] = new Semaphore(0);
        }

        Thread[] filosofos = new Thread[N];

        for (int i = 0; i < N; i++) {
            final int index = i;
            filosofos[i] = new Thread(() -> filosofo(index));
            filosofos[i].start();
        }
    }

    private static void filosofo(int i) {
        int refeicoesRealizadas = 0;

        while (refeicoesRealizadas < 3) {
            pense(i);
            pegueGarfo(i);
            coma(i);
            largarGarfo(i);
            refeicoesRealizadas++;
        }

        System.out.println("Filósofo " + i + " comeu 3 vezes e está satisfeito.");
    }

    private static void pegueGarfo(int i) {
        try {
            mutex.acquire();
            state[i] = FAMINTO;
            testa(i);
            mutex.release();
            s[i].acquire(); // Bloqueia se os garfos não foram adquiridos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void largarGarfo(int i) {
        try {
            mutex.acquire();
            state[i] = PENSANDO;
            testa(vizinhoEsquerda(i));
            testa(vizinhoDireita(i));
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testa(int i) {
        if (state[i] == FAMINTO && state[vizinhoEsquerda(i)] != COMENDO && state[vizinhoDireita(i)] != COMENDO) {
            state[i] = COMENDO;
            s[i].release(); // Libera se os garfos foram adquiridos
        }
    }

    private static int vizinhoEsquerda(int i) {
        return (i - 1 + N) % N; // Fórmula para encontrar o vizinho à esquerda, garantindo que seja um índice válido no array
    }

    private static int vizinhoDireita(int i) {
        return (i + 1) % N; // Fórmula para encontrar o vizinho à direita, garantindo que seja um índice válido no array
    }

    private static void pense(int i) {
        System.out.println("Filósofo " + i + " está pensando.");
        try {
            Thread.sleep((long) (2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void coma(int i) {
        System.out.println("Filósofo " + i + " está comendo.");
        try {
            Thread.sleep((long) (2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
