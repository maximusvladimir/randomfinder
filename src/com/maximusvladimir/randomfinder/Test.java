package com.maximusvladimir.randomfinder;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.JTextPane;
import java.awt.Font;

public class Test extends JFrame {
	private JTextField textField;
	private Searcher scanner;
	private JTextPane txtpncodeWillGenerate;
	public static void main(String[] args) { new Test(); }
	
	public Test() {
		setVisible(true);
		setSize(324,270);
		setTitle("Test");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		scanner = new Searcher();
		
		JLabel lblEnterStringTo = new JLabel("Enter string to find:");
		lblEnterStringTo.setBounds(10, 11, 136, 14);
		getContentPane().add(lblEnterStringTo);
		
		textField = new JTextField();
		textField.setBounds(10, 30, 189, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		final JButton btnGo = new JButton("Go!");
		btnGo.setBounds(209, 29, 89, 23);
		getContentPane().add(btnGo);
		
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 90, 288, 20);
		getContentPane().add(progressBar);
		
		JLabel lblMaximumTimeRemaining = new JLabel("Maximum time remaining:");
		lblMaximumTimeRemaining.setBounds(10, 61, 178, 14);
		getContentPane().add(lblMaximumTimeRemaining);
		
		final JLabel lblResult = new JLabel("Result:");
		lblResult.setBounds(10, 121, 189, 14);
		getContentPane().add(lblResult);
		
		txtpncodeWillGenerate = new JTextPane();
		txtpncodeWillGenerate.setFont(new Font("Courier New", Font.PLAIN, 10));
		txtpncodeWillGenerate.setText("(Code will generate here)");
		txtpncodeWillGenerate.setBounds(10, 141, 298, 90);
		getContentPane().add(txtpncodeWillGenerate);
		
		btnGo.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (btnGo.getText().equals("Go!")) {
					btnGo.setText("Halt");
					lblResult.setText("Result:");
					try {
						scanner.setString(textField.getText());
					}
					catch (Throwable t) {
						JOptionPane.showMessageDialog(Test.this, t.getMessage());
					}
					scanner.startAsync();
					Test.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				}
				else {
					Test.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					btnGo.setText("Go!");
					scanner.halt();
				}
			}
		});
		
		new Timer(250,new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				progressBar.setValue((int)(100 * scanner.getProgress()));
				
				if (scanner.isFinished()) {
					Test.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					if (scanner.getResultSeed() == Integer.MIN_VALUE)
						lblResult.setText("Result: Not found.");
					else {
						lblResult.setText("Result: " + scanner.getResultSeed());
						genCode();
					}
					btnGo.setText("Go!");
				}
			}
		}).start();
		
		repaint();
	}
	
	private void genCode() {
		String pieces = "";
		for (int i = 0; i < scanner.getString().length(); i++) {
			pieces += "((char)(rand.nextInt(96)+32)) + \"\"";
			pieces += " +";
		}
		if (scanner.getString().length() >= 1)
			pieces = pieces.substring(0,pieces.length()-2);
		txtpncodeWillGenerate.setText("Random rand = new Random("+scanner.getResultSeed()+");\n"+
									  "System.out.println(" + pieces + ");\n"+
									  "// Outputs " + scanner.getString());
	}
}
