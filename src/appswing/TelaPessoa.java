package appswing;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Pessoa;
import modelo.Reuniao;
import regras_de_negocio.Fachada;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class TelaPessoa {

    private JFrame frame;
    private JTable table;
    private JButton button;
    private JButton button_1;
    private JScrollPane scrollPane;
    private JLabel label;
    private JButton button_2;

    /**
     * Create the application.
     */
    public TelaPessoa() {
        initialize();
        listagemPessoas();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Pessoas");
        frame.setBounds(100, 100, 561, 286);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Change to DISPOSE_ON_CLOSE
        frame.getContentPane().setLayout(null);
        
        scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 525, 165);
		frame.getContentPane().add(scrollPane);
        
        table = new JTable();
		scrollPane.setViewportView(table);
        scrollPane.setViewportView(table);
        table.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Nome", "Reuniões"
        	}
        ));
        table.getColumnModel().getColumn(0).setPreferredWidth(25);
        table.getColumnModel().getColumn(1).setPreferredWidth(185);
        
        button = new JButton("Excluir");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String nome = (String) table.getValueAt(selectedRow, 0);
					try {
						Fachada.deletarPessoa(nome);
						listagemPessoas();
					} catch (Exception e1) {
						label.setText(e1.getMessage());
					}
				} else {
					label.setText("Selecione uma reunião para ver.");
				}
        	}
        });
        button.setBounds(10, 187, 140, 23);
        frame.getContentPane().add(button);
        
        button_1 = new JButton("Alterar");
        button_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String nome = (String) table.getValueAt(selectedRow, 0);
					String nome2 = JOptionPane.showInputDialog(null, "Novo nome:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
					if (nome2 != null && !nome2.trim().isEmpty()) {
        	            try {
        	                Fachada.alterarNomePessoa(nome, nome2);
        	                listagemPessoas();
        	            } catch (Exception ex) {
        	                label.setText(ex.getMessage());
        	            }
        	        }
				} else {
					label.setText("Selecione uma pessoa para alterar.");
				}
        	}
        });
        button_1.setBounds(160, 187, 140, 23);
        frame.getContentPane().add(button_1);
        
        label = new JLabel("");
        label.setBounds(10, 221, 525, 15);
        frame.getContentPane().add(label);
        
        button_2 = new JButton("+ Nova Pessoa");
        button_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String nome = JOptionPane.showInputDialog(null, "Nome:", "Entrada de Dados", JOptionPane.QUESTION_MESSAGE);
        		try {
					Fachada.criarPessoa(nome);
					listagemPessoas();
				} catch (Exception e1) {
					label.setText(e1.getMessage());
				}
        	}
        });
        button_2.setBounds(310, 187, 225, 23);
        frame.getContentPane().add(button_2);
    }
    
    public void listagemPessoas() {
		try {
			List<Pessoa> lista = Fachada.listarPessoas();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);
			
			for (Pessoa p : lista) {
				model.addRow(new Object[]{
					p.getNome(),
					p.getAssuntosReunioes()});
			}
			
	        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

	        for (int i = 0; i < table.getColumnCount(); i++) {
	            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	        }
		} catch(Exception erro){
			label.setText("Erro: " + erro.getMessage());
		}
	}

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}