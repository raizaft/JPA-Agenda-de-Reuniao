package appswing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Reuniao;
import regras_de_negocio.Fachada;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaPrincipal {

	private JFrame frame;
	private JTable table;
	private JButton button;
	private JButton button_3;
	private JScrollPane scrollPane;
	private JTextField textField_1;
	private JLabel label_2;
	private JTextField textField_2;
	private JLabel label_3;
	private JLabel label_4;
	private JTextField textField_3;
	private JTextArea textArea;
	private JLabel label;
	private JButton button_1;
	private ArrayList<String> nomesPessoas;
	private JScrollPane scrollPane_1;
	private JButton button_2;
	private JButton button_4;
	private JButton button_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal window = new TelaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaPrincipal() {
		initialize();
		listagemReunioes();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Fachada.inicializar();
		frame = new JFrame();
		frame.setTitle("Agenda de Reuniões");
		frame.setBounds(100, 100, 502, 384);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Fachada.finalizar();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        frame.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 275, 257);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
        scrollPane.setViewportView(table);
        table.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"ID", "Assunto", "Data"
        	}
        ));
        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(185);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
		
		button = new JButton("Cadastrar Reunião");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        String data = textField_1.getText();
		        String assunto = textField_2.getText();

		        if (data.isEmpty() || assunto.isEmpty() || nomesPessoas.isEmpty()) {
		            label.setText("Preencha os campos obrigatórios");
		            return;
		        }
				try {
					Fachada.criarReuniao(data, assunto, nomesPessoas);
					listagemReunioes();
					nomesPessoas.clear();
				} catch (Exception ex) {
                	label.setText("Erro: " + ex.getMessage());
                }
			}
		});
		button.setBounds(295, 211, 182, 23);
		frame.getContentPane().add(button);
		
		button_3 = new JButton("Limpar");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nomesPessoas.clear();
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textArea.setText("");
				label.setText("");
			}
		});
		button_3.setBounds(295, 245, 182, 23);
		frame.getContentPane().add(button_3);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(295, 26, 181, 20);
		frame.getContentPane().add(textField_1);
		
		label_2 = new JLabel("Data");
		label_2.setBounds(295, 11, 46, 14);
		frame.getContentPane().add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(295, 72, 181, 20);
		frame.getContentPane().add(textField_2);
		
		label_3 = new JLabel("Assunto");
		label_3.setBounds(295, 57, 82, 14);
		frame.getContentPane().add(label_3);
		
		label_4 = new JLabel("Participante");
		label_4.setBounds(295, 103, 86, 14);
		frame.getContentPane().add(label_4);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(295, 118, 131, 20);
		frame.getContentPane().add(textField_3);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(295, 149, 182, 51);
		frame.getContentPane().add(scrollPane_1);
		
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		label = new JLabel("");
		label.setBounds(10, 279, 275, 23);
		frame.getContentPane().add(label);
	
		nomesPessoas = new ArrayList<>();
		
		button_1 = new JButton("+");
		button_1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String nomePessoa = textField_3.getText();
		        try {
		            if (!nomesPessoas.contains(nomePessoa)) {
		                try {
		                	if (Fachada.buscarPessoa(nomePessoa)==null) {
			                    Fachada.criarPessoa(nomePessoa);
		                	}
		                    textArea.append(nomePessoa + "\n");
		                    nomesPessoas.add(nomePessoa);
		                } catch (Exception ex) {
		                    if (ex.getMessage().equals("Pessoa já existe")) {
		                        textArea.append(nomePessoa + "\n");
		                        nomesPessoas.add(nomePessoa);
		                    } else {
		                        throw ex;
		                    }
		                }
		            } else {
		                label.setText("Pessoa já adicionada à reunião.");
		            }
		            textField_3.setText("");
		        } catch (Exception ex) {
		            label.setText("Erro: " + ex.getMessage());
		        } 
		        
		    }
		});
		button_1.setFont(new Font("Arial", Font.BOLD, 11));
		button_1.setBounds(436, 117, 41, 23);
		frame.getContentPane().add(button_1);
		
		button_2 = new JButton("Excluir Reunião");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					int id = (int) table.getValueAt(selectedRow, 0);
					try {
						Fachada.deletarReuniao(id);
						listagemReunioes();
						label.setText("Reunião excluída com sucesso.");
					} catch (Exception ex) {
						label.setText("Erro ao excluir reunião: " + ex.getMessage());
					}
				} else {
					label.setText("Selecione uma reunião para excluir.");
				}
			}
		});
		button_2.setBounds(296, 313, 181, 23);
		frame.getContentPane().add(button_2);
		
		button_4 = new JButton("Ver Reunião");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					int id = (int) table.getValueAt(selectedRow, 0);
					abrirReuniao(id);
				} else {
					label.setText("Selecione uma reunião para ver.");
				}
			}
		});
		button_4.setBounds(296, 279, 181, 23);
		frame.getContentPane().add(button_4);
		
		button_5 = new JButton("Ver Pessoas");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaPessoa telaPessoa = new TelaPessoa();
		        telaPessoa.setVisible(true);
			}
		});
		button_5.setBounds(10, 313, 275, 23);
		frame.getContentPane().add(button_5);
	}
	
	private void abrirReuniao(int id) {
        try {
            Reuniao reuniao = Fachada.buscarReuniao(id);
            if (reuniao != null) {
            	TelaReuniao telareuniao = new TelaReuniao(reuniao, this);
                telareuniao.setVisible(true);
            } else {
                label.setText("Reunião não encontrada.");
            }
        } catch (Exception e) {
            label.setText("Erro ao buscar detalhes da reunião: " + e.getMessage());
        }
    }
	
	public void listagemReunioes() {
		try {
			List<Reuniao> lista = Fachada.listarReunioes();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);
			
			for (Reuniao reu : lista) {
				model.addRow(new Object[]{
					reu.getId(),
					reu.getAssunto(),
					reu.getData()});
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
}