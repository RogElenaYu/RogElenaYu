package RogSimpsonTetrisData;

import javax.swing.JPanel;

import RogSimpsonTetrisData.Figure.SquareForms;

import java.awt.Color;
import java.awt.Graphics;

public class GameFieldPreview extends JPanel {

	private final int BOARD_WIDTH = 5;
	private final int BOARD_HEIGHT = 5;
	private int curX = 1;
	private int curY = 1;
	public Color nextColorPreview; //rog
	public SquareForms nextSquareFormsPreview; //rog
	private Figure curPiecePreview;
	private SquareForms[] boardPreview;

	public GameFieldPreview(Tetris parent) {
	}


	private int squareWidth() {

		return (int) getSize().getWidth() / BOARD_WIDTH;
	}

	private int squareHeight() {

		return (int) getSize().getHeight() / BOARD_HEIGHT;
	}

	void start() {

		boardPreview = new SquareForms[BOARD_WIDTH * BOARD_HEIGHT];
		newPiece();
		repaint();
	}


	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		doDrawing(g);
		repaint();
	}

	private void doDrawing(Graphics g) {

		if (nextSquareFormsPreview != SquareForms.CleanFigure) {

			for (int i = 0; i < 6; i++) {

				int x = curX + curPiecePreview.x(i);
				int y = curY - curPiecePreview.y(i);

				drawSquare(g, x * squareWidth(),
						(BOARD_HEIGHT - y - 1) * squareHeight(),
						curPiecePreview.getFigure(), nextColorPreview);
			}
		}
	}

	public void newPiece() {

		curPiecePreview = new Figure();
		curPiecePreview.setFigure(nextSquareFormsPreview); //rog
	}


	private void drawSquare(Graphics g, int x, int y, SquareForms shape, Color color) {

		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1,
				x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
				x + squareWidth() - 1, y + 1);
	}

	}

