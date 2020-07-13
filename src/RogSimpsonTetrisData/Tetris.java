package RogSimpsonTetrisData;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tetris extends JFrame {
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {

			var game = new Tetris();
			game.setVisible(true);
		});
	}
	private JLabel scoreField;
	private JButton btnRotation;
	private JButton btnReflect;
	private JButton btnSound;
	private GameFieldPreview gameFieldPreview;

	public Tetris() {

		initUI();
	}

	private void initUI() {

		JPanel panel = new JPanel();
		scoreField = new JLabel(" 0");
		panel.add(scoreField);

		add(panel, BorderLayout.NORTH);
		panel.setPreferredSize(new Dimension(288, 55));

		Image imageSound = new ImageIcon("Sound.png").getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		ImageIcon iconSound = new ImageIcon(imageSound);
		btnSound = new JButton(iconSound);
		btnSound.setPreferredSize(new Dimension(25, 25));
		btnSound.setToolTipText("Turn off the sound");
		panel.add(btnSound);
		btnSound.setFocusable(false);
		
		Image imageRotation = new ImageIcon("RotationRight.png").getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		ImageIcon iconRotation = new ImageIcon(imageRotation);
		btnRotation = new JButton(iconRotation);
		btnRotation.setPreferredSize(new Dimension(25, 25));
		btnRotation.setToolTipText("To change rotation direction press E.");
		panel.add(btnRotation);
		btnRotation.setFocusable(false);

		Image imageReflection = new ImageIcon("Reflection.png").getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		ImageIcon iconReflection = new ImageIcon(imageReflection);
		btnReflect = new JButton(iconReflection);
		btnReflect.setPreferredSize(new Dimension(25, 25));
		btnReflect.setToolTipText("To reflect the figure press R.");
		panel.add(btnReflect);
		btnReflect.setFocusable(false);
		
		gameFieldPreview = new GameFieldPreview(this);
		gameFieldPreview.setPreferredSize(new Dimension(50, 50));
		gameFieldPreview.setToolTipText("Next figure");
		panel.add(gameFieldPreview);

		var gameField = new GameField(this);
		add(gameField);
		gameField.start();

		gameFieldPreview.start();
		gameFieldPreview.setVisible(false);

		setTitle("Rog Simpson Tetris");
		setSize(288, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	JLabel getScoreField() {

		return scoreField;
	}

	JButton getbtnSound() {

		return btnSound;
	}

	JButton getbtnRotation() {

		return btnRotation;
	}

	JButton getbtnReflect() {

		return btnReflect;
	}

	public GameFieldPreview getGameFieldPreview() {

		return gameFieldPreview;
	}
}
