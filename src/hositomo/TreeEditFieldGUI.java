package hositomo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class TreeEditFieldGUI extends JFrame {

	private JPanel contentPane;
	private TextTreeControler ctrl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TreeEditFieldGUI frame = new TreeEditFieldGUI();
					frame.setVisible(true);
					TreeEditFieldGUISub frame2 = new TreeEditFieldGUISub();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TreeEditFieldGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		
		
		
		JTextPane textOut = new JTextPane();
		scrollPane_1.setViewportView(textOut);
		textOut.setEditable(false);
		
		ctrl = new TextTreeControler();
		ctrl.init();
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		JTextArea txtrIn = new TreeField(ctrl);
		UndoHelper helper = new UndoHelper(txtrIn,textOut);
		scrollPane.setViewportView(txtrIn);
		txtrIn.setLineWrap(true);
		txtrIn.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e) {
//				System.out.println(txtrIn.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {//文字が削除されるときの挙動
//				System.err.println(txtrIn.getText());
				ctrl.remove(txtrIn.getText(),e.getOffset(),e.getLength());
//				System.out.println("r Len = "+e.getLength()+"　off = "+e.getOffset());
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
//				System.out.println("test");
			}
			
		});
		


		
		JButton btnReset = new JButton("リセット");
		btnReset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.init();
				txtrIn.requestFocus();
			}
			
		});
		
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.gridx = 0;
		gbc_btnReset.gridy = 2;
		contentPane.add(btnReset, gbc_btnReset);
		
	}

}
