package br.com.fiap;

import br.com.fiap.domain.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracle");
        EntityManager manager = factory.createEntityManager();

        //TipoDeAcao tipoDeAcao = addTipoAcao(manager);
        //System.out.println(tipoDeAcao);

        //Estado estado = addEstado(manager);
        //System.out.println(estado);

        //Advogado advogado = addAdvogado(manager);
        //System.out.println(advogado);

        //Processo processo = addProcesso(manager);
        //System.out.println(processo);

        //Processo processoEncontrado = buscarProcessoPorId(manager);
        //System.out.println(processoEncontrado);

        listarTodosProcessos(manager);

        manager.close();
        factory.close();
    }

    private static void listarTodosProcessos(EntityManager manager) {
        List<Processo> processos = manager.createQuery("FROM Processo").getResultList();
        for (Processo processo: processos) {
            System.out.println(processo);
        }
    }

    private static Processo buscarProcessoPorId(EntityManager manager) {
        String id = JOptionPane.showInputDialog("Digite o id do processo que deseja buscar: ");
        return manager.find(Processo.class, id);
    }

    private static Processo addProcesso(EntityManager manager) {
        boolean salvou = false;

        do {
            String numero = JOptionPane.showInputDialog("Número: ");

            List<TipoDeAcao> tipoDeAcoes = manager.createQuery("FROM TipoDeAcao").getResultList();
            TipoDeAcao tipoAcaoSelecionada = (TipoDeAcao) JOptionPane.showInputDialog(null, "Selecione o tipo de ação", "Selecione uma opção", JOptionPane.QUESTION_MESSAGE, null, tipoDeAcoes.toArray(), tipoDeAcoes.get(0));

            String[] options = {"Sim", "Não"};
            int proBonoResposta = JOptionPane.showOptionDialog(null, "Pró-bono?", "Selecione uma opção", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Boolean proBono = (proBonoResposta == 0);

            List<Advogado> advogados = manager.createQuery("FROM Advogado").getResultList();
            Advogado advogadoSelecionado = (Advogado) JOptionPane.showInputDialog(null, "Selecione o advogado", "Selecione uma opção", JOptionPane.QUESTION_MESSAGE, null, advogados.toArray(), advogados.get(0));

            Processo processo = new Processo().setNumero(numero).setProBono(proBono).setAdvogado(advogadoSelecionado).setTipoDeAcao(tipoAcaoSelecionada);

            try {
                manager.getTransaction().begin();
                manager.persist(processo);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o Advogado. " + e.getMessage());
            }
            return processo;
        } while (salvou == false);
    }

    private static Advogado addAdvogado(EntityManager manager) {
        boolean salvou = false;

        do {
            String nome = JOptionPane.showInputDialog("Nome do Advogado: ");
            String numeroOAB = JOptionPane.showInputDialog("Número OAB: ");
            List<Estado> estados = manager.createQuery("FROM Estado").getResultList();
            Estado estadoSelecionado = (Estado) JOptionPane.showInputDialog(null, "Selecione o Estado correspondente", "Seleção o Estado", JOptionPane.QUESTION_MESSAGE, null, estados.toArray(), estados.get(0));

            Advogado advogado = new Advogado().setNome(nome).setNumeroOAB(numeroOAB).setEstado(estadoSelecionado);

            try {
                manager.getTransaction().begin();
                manager.persist(advogado);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o Advogado. " + e.getMessage());
            }
            return advogado;
        } while (salvou == false);
    }

    private static Estado addEstado(EntityManager manager) {
        boolean salvou = false;

        do {
            String nome = JOptionPane.showInputDialog("Nome do Estado: ");
            String sigla = JOptionPane.showInputDialog("Sigla deste Estado: ");

            Estado estado = new Estado().setNome(nome).setSigla(sigla);

            try {
                manager.getTransaction().begin();
                manager.persist(estado);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o estado. " + e.getMessage());
            }
            return estado;
        } while (salvou == false);
    }

    private static TipoDeAcao addTipoAcao(EntityManager manager) {
        boolean salvou = false;

        do {
            String nome = JOptionPane.showInputDialog("Tipo de Ação: ");

            TipoDeAcao tipo = new TipoDeAcao().setNome(nome);

            try {
                manager.getTransaction().begin();
                manager.persist(tipo);
                manager.getTransaction().commit();
                salvou = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: \nNão foi possível salvar o tipo de ação. " + e.getMessage());
            }
            return tipo;
        } while (salvou == false);
    }
}