private static void executarAlgoritmoNRU(int[][] matrizRam, int[][] matrizSwap) {
        List<Integer> listaNRU = new ArrayList<>();

        while (instrucaoAtual <= 1000) {
            int instrucao = new Random().nextInt(100) + 1;

            // Verifica se a instrução está na memória RAM
            int linhaRam = encontrarLinhaNaMatriz(matrizRam, instrucao);
            if (linhaRam == -1) {
                // A instrução não está na memória RAM, então é necessário realizar uma substituição
                int linhaSwap = encontrarLinhaMaisAntiga(listaNRU);
                substituirPagina(matrizRam, matrizSwap, linhaSwap);
            } else {
                // A instrução está na memória RAM, então é necessário atualizar o tempo de acesso
                atualizarTempoAcesso(listaNRU, linhaRam);
            }

            atualizarBitAcesso(matrizRam, instrucao);

            atualizarConjuntoTrabalho(matrizRam, listaNRU);

            instrucaoAtual++;
        }
    }

    private static int encontrarLinhaMaisAntiga(List<Integer> listaNRU) {
        return listaNRU.get(listaNRU.size() - 1);
    }

    private static void substituirPagina(int[][] matrizRam, int[][] matrizSwap, int linhaSwap) {
        // Salva a página que está sendo substituída em SWAP
        int numeroPagina = matrizSwap[linhaSwap][0];
        int instrucao = matrizSwap[linhaSwap][1];
        int dado = matrizSwap[linhaSwap][2];
        int bitAcesso = matrizSwap[linhaSwap][3];
        int bitModificado = matrizSwap[linhaSwap][4];
        int tempoEnvelhecimento = matrizSwap[linhaSwap][5];

        // Atualiza a página em SWAP com o bit modificado como 0
        matrizSwap[linhaSwap][4] = 0;

        // Copia a página do conjunto de trabalho para a memória RAM
        int linhaRam = encontrarLinhaNoConjuntoTrabalho(matrizRam, numeroPagina);
        matrizRam[linhaRam] = matrizSwap[linhaSwap];

        // Atualiza o tempo de acesso da página em SWAP
        matrizSwap[linhaSwap][5] = instrucaoAtual;
    }

    private static void atualizarTempoAcesso(List<Integer> listaNRU, int linhaRam) {
        int numeroPagina = matrizRam[linhaRam][0];
        listaNRU.remove(numeroPagina);
        listaNRU.add(numeroPagina);
    }

    private static void atualizarBitAcesso(int[][] matrizRam, int instrucao) {
        int linhaRam = encontrarLinhaNaMatriz(matrizRam, instrucao);
        matrizRam[linhaRam][3] = 1;
    }

    private static void atualizarConjuntoTrabalho(int[][] matrizRam, List<Integer> listaNRU) {
        // Atualiza o conjunto de trabalho com todas as páginas que estão na memória RAM
        for (int i = 0; i < matrizRam.length; i++) {
            int numeroPagina = matrizRam[i][0];
            if (numeroPagina != -1) {
                listaNRU.add(numeroPagina);
            }
        }
    }
