package appswing;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.util.List;

import modelo.Reuniao;
import regras_de_negocio.Fachada;
import modelo.Pessoa;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class TelaReuniao extends JFrame {
    private Reuniao reuniao;
    private JLabel lblId;
    private JLabel lblData;
    private JLabel lblAssunto;
    private JTextArea textAreaParticipantes;
    private JLabel label;
    private JButton button;
    private JButton button_2;
    private JTextField textField;
    private TelaPrincipal telaPrincipal;
    private JButton button_1;

    public TelaReuniao(Reuniao reuniao, TelaPrincipal telaPrincipal) {
        this.reuniao = reuniao;
        this.telaPrincipal = telaPrincipal;
        initialize();
    }

    private void initialize() {
        setTitle("Detalhes da Reunião");
        setBounds(100, 100, 297, 448);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        lblId = new JLabel("ID: " + reuniao.getId());
        lblId.setBounds(10, 20, 64, 14);
        getContentPane().add(lblId);

        lblData = new JLabel("Data: " + reuniao.getData());
        lblData.setBounds(84, 20, 186, 14);
        getContentPane().add(lblData);

        lblAssunto = new JLabel("Assunto: " + reuniao.getAssunto());
        lblAssunto.setBounds(10, 45, 200, 14);
        getContentPane().add(lblAssunto);

        textAreaParticipantes = new JTextArea();
        textAreaParticipantes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaParticipantes);
        scrollPane.setBounds(10, 129, 260, 171);
        getContentPane().add(scrollPane);

        label = new JLabel("Participantes:");
        label.setBounds(10, 104, 97, 14);
        getContentPane().add(label);

        listagemPessoas();

        button = new JButton("Adicionar Participante");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = textField.getText();
                if (nome != null && !nome.trim().isEmpty()) {
                    try {
                        Fachada.addPessoaReuniao(nome, reuniao);
                    } catch (Exception ex) {
                        Component frame = null;
                        JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "O nome não pode ser vazio ou apenas espaços!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
                listagemPessoas();
                textField.setText("");
            }
        });
        button.setBounds(10, 342, 260, 23);
        getContentPane().add(button);

        button_2 = new JButton("Alterar Assunto");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String assunto = JOptionPane.showInputDialog(null, "Novo assunto:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
                if (assunto != null && !assunto.trim().isEmpty()) {
                    try {
                        Fachada.alterarAssuntoReuniao(reuniao.getId(), assunto);
                        lblAssunto.setText("Assunto: " + assunto);
                        telaPrincipal.listagemReunioes();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        button_2.setBounds(10, 70, 260, 23);
        getContentPane().add(button_2);

        textField = new JTextField();
        textField.setBounds(10, 311, 260, 20);
        getContentPane().add(textField);
        textField.setColumns(10);

        button_1 = new JButton("Remover Participante");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = JOptionPane.showInputDialog(null, "Nome:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
                if (nome != null && !nome.trim().isEmpty()) {
                    try {
                        boolean existe = reuniao.getPessoas().stream()
                                .anyMatch(p -> p.getNome().equalsIgnoreCase(nome));

                        if (!existe) {
                            JOptionPane.showMessageDialog(null, "Nome não encontrado entre os participantes.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if (reuniao.getPessoas().size() <= 2) {
                            int confirm = JOptionPane.showConfirmDialog(null,
                                    "Essa remoção deixará a reunião sem participantes suficientes. A reunião será excluída. Você deseja continuar?",
                                    "Confirmação de Exclusão",
                                    JOptionPane.YES_NO_OPTION);

                            if (confirm == JOptionPane.YES_OPTION) {
                                Fachada.removerPessoaReuniao(nome, reuniao.getId());
                                Fachada.deletarReuniao(reuniao.getId());
                                JOptionPane.showMessageDialog(null, "Reunião excluída com sucesso.");
                                telaPrincipal.listagemReunioes();
                                dispose();
                            }
                        } else {
                            Fachada.removerPessoaReuniao(nome, reuniao.getId());
                            JOptionPane.showMessageDialog(null, "Participante removido.");
                            listagemPessoas();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        button_1.setBounds(10, 376, 260, 23);
        getContentPane().add(button_1);
    }

    public void listagemPessoas() {
        try {
            List<Pessoa> participantes = reuniao.getPessoas();
            StringBuilder sb = new StringBuilder();
            for (Pessoa pessoa : participantes) {
                sb.append(pessoa.getNome()).append("\n");
            }
            textAreaParticipantes.setText(sb.toString());
        } catch (Exception ex) {
            Component frame = null;
            JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}