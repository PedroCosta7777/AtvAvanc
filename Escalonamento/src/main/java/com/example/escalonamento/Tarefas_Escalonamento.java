package com.example.escalonamento;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tarefas_Escalonamento {
    public static List<ArrayList<Object>> tarefas = Collections.synchronizedList(new ArrayList<>());

    public static void adicionarTarefa(int numeroDaTarefa, long tempoDeComputacao, int quantidadeDeItens) {
        // Cria um ArrayList para armazenar a tarefa
        ArrayList<Object> tarefa = new ArrayList<>();
        // Adiciona os parâmetros na tarefa
        tarefa.add(numeroDaTarefa); // Número da tarefa
        tarefa.add(tempoDeComputacao); // Tempo de computação da tarefa
        tarefa.add(quantidadeDeItens); // Quantidade de itens
        // Adiciona a tarefa ao ArrayList principal
        tarefas.add(tarefa);
        Log.d("TAREFAS", "Tarefas: " + tarefas.toString() + "\n");
    }

    public static void verificarEscalonabilidade() {
        // Definindo os deadlines de acordo com o número da tarefa
        Map<Integer, Integer> deadlines = new HashMap<>();
        deadlines.put(1, 40);
        deadlines.put(2, 40);
        deadlines.put(3, 100);
        deadlines.put(4, 40);
        deadlines.put(5, 100);

        // Período fixo para todas as tarefas
        final int periodo = 100;

        // Percorrendo as tarefas para verificar escalonabilidade
        for (int i = 0; i < tarefas.size(); i++) {
            ArrayList<Object> tarefa = tarefas.get(i);

            // Extraindo os dados da tarefa
            int numeroDaTarefa = (int) tarefa.get(0);  // Número da tarefa
            long tempoDeComputacao = (long) tarefa.get(1);  // Tempo de computação da tarefa

            // Verificando o deadline da tarefa
            Integer deadline = deadlines.get(numeroDaTarefa);
            if (deadline == null) {
                Log.d("ESCALONAMENTO", "Deadline não definido para a tarefa " + numeroDaTarefa);
                continue;
            }

            // Inicializando W_i
            long W_i = tempoDeComputacao;
            boolean escalonavel = true;

            while (true) {
                long interferencia = 0;

                // Calculando a interferência de todas as tarefas anteriores
                for (int j = 0; j < i; j++) {
                    ArrayList<Object> tarefaAnterior = tarefas.get(j);
                    long tempoDeComputacaoAnterior = (long) tarefaAnterior.get(1);

                    // Soma da interferência conforme a fórmula: ceil(W_i / P_j) * C_j
                    interferencia += Math.ceil((double) W_i / periodo) * tempoDeComputacaoAnterior;
                }

                long novo_W_i = tempoDeComputacao + interferencia;

                // Se W_i não mudou, atingimos a convergência
                if (novo_W_i == W_i) {
                    break;
                }

                // Se W_i ultrapassou o deadline, a tarefa não é escalonável
                if (novo_W_i > deadline) {
                    escalonavel = false;
                    Log.d("ESCALONAMENTO", "Novo Wi:  " + novo_W_i);
                    break;
                }

                W_i = novo_W_i;

            }

            if (!escalonavel) {
                Log.d("ESCALONAMENTO", "O sistema deixou de ser escalonável na tarefa " + numeroDaTarefa + " após " + (i + 1) + " itens. Tempo de computação dela: " + tempoDeComputacao);
                break; // Se uma tarefa não for escalonável, interrompa a verificação
            }
        }
    }
}
