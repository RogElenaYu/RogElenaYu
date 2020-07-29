package RogSimpsonTetrisData;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import RogSimpsonTetrisData.Figure.SquareForms;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

public class GameField extends JPanel {

	private final int FIELD_WIDTH = 15;
	private final int FIELD_HEIGHT = 30;
	private final int PERIOD_INTERVAL = 1000;

	private Timer timer;
	private boolean isFallingFinished = false;
	private boolean isPaused = false;
	private int removedLinesScore = 0;
	private int currentFigureX = 0;
	private int currentFigureY = 0;
	private JLabel scoreField;
	private JButton btnSound;
	private boolean noSound = false;
	private float currentVolume = 0.5F;
	private JButton btnRotation;
	private boolean rightRotation = true;
	private JButton btnReflect;
	private Figure currentFigure;
	private SquareForms[] field;
	private Color[] gameFieldColor;
	private Color color;
	private Color nextColor;
	private SquareForms nextFigure;
	private GameFieldPreview gameFieldPreview;
	private int figureQuantity = 7;
	private Figure currentMan;
	private int currentManX = 0;
	private int currentManY = 2;
	private int step = 1;
	private boolean manDied = false;
	private Sound manDyingSound;
	private Sound manGreetingSound;
	private Sound manBurpingSound;
	private Sound manLaughingSound;
	private Sound manSingingSound;
	private boolean neededManDyingSound = true;
	private boolean neededManSingingSound = true;
	private Timer timerManBurping;
	private final int PERIOD_INTERVAL_ManBurping = 30000;
	private ImageIcon iconSound = new ImageIcon(new ImageIcon("Sound.png").getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH));
	private ImageIcon iconNoSound = new ImageIcon(new ImageIcon("NoSound.png").getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH));
	private ImageIcon iconRotationRight = new ImageIcon(new ImageIcon("RotationRight.png").getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH));
	private ImageIcon iconRotationLeft = new ImageIcon(new ImageIcon("RotationLeft.png").getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH));
	private Image imageBlot = new ImageIcon("Blot.png").getImage();
	private Image imageManJumping = new ImageIcon("ManJumping.png").getImage();
	private Image imageManGoingRight = new ImageIcon("ManGoingRight.gif").getImage();
	private Image imageManGoingLeft = new ImageIcon("ManGoingLeft.gif").getImage();
	private Image imageManSpinning = new ImageIcon("ManSpinning.gif").getImage();
	private Image imageManDisappearing = new ImageIcon("ManDisappearing.gif").getImage();
	private Image imageManDisappearingReturn = new ImageIcon("ManDisappearingReturn.gif").getImage();
	private Image imageManSinging = new ImageIcon("ManSinging.gif").getImage();
	private Image image;

	public GameField(Tetris parent) {

		initGameField(parent);
	}

	private void initGameField(Tetris parent) {

		setFocusable(true);
		scoreField = parent.getScoreField();
		addKeyListener(new TAdapter());
		gameFieldPreview = parent.getGameFieldPreview();

		File soundFile = new File("ManDying.wav");
		manDyingSound = new Sound(soundFile);

		soundFile = new File("ManGreeting.wav");
		manGreetingSound = new Sound(soundFile);

		soundFile = new File("ManBurping.wav");
		manBurpingSound = new Sound(soundFile);

		soundFile = new File("ManLaughing.wav");
		manLaughingSound = new Sound(soundFile);

		soundFile = new File("ManSinging.wav");
		manSingingSound = new Sound(soundFile);

		
		btnSound = parent.getbtnSound();
		
		btnSound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				noSound = !noSound;
				turnSoundOnOff(noSound, true);
				btnSound.setFocusable(false);
			}
		});

		btnRotation = parent.getbtnRotation();
		btnRotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightRotation = !rightRotation;
				if (rightRotation == true) {
					btnRotation.setIcon(iconRotationRight);
					
				} else {
					btnRotation.setIcon(iconRotationLeft);
				}
				btnRotation.setFocusable(false);
			}

		});

		btnReflect = parent.getbtnReflect();
		btnReflect.setVisible(false);
		btnReflect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveFigure(currentFigure.reflect(), currentFigureX, currentFigureY);
				btnReflect.setFocusable(false);
			}
		});
	}

	void start() {
		String message = "THANKS TO:\n- the \"Tetriller\" game for the idea;"
				+ "\n- open source (http://zetcode.com/tutorials/javagamestutorial/tetris/) for the basis code of the game;"
				+ "\n- open source (https://habr.com/ru/post/191422/) for the Sound class code."
				+ "\nCONTROL KEYS:\n- Right;\n- Left;\n- Up (turn);\n- Space (drop);\n- D (accelerate the fall);"
				+ "\n- E (change the direction of rotation);\n- R (flip horizontally, appears after 20 removed lines);\n- P (pause).";
		
		JOptionPane.showMessageDialog(null, message, "Rog Simpson Tetris", JOptionPane.INFORMATION_MESSAGE);

		figureQuantity = 7;
		removedLinesScore = 0;
		image = imageManJumping;
		manDied = false;
		neededManDyingSound = true;
		neededManSingingSound = true;

		color = getContrastColor();
		nextColor = getContrastColor();
		gameFieldPreview.nextColorPreview = nextColor;
		gameFieldPreview.repaint();

		currentFigure = new Figure();
		currentFigure.setFigure(SquareForms.CleanFigure);

		nextFigure = currentFigure.getRandomFigure(figureQuantity);

		currentMan = new Figure();
		currentMan.setFigure(SquareForms.ManFigure);

		field = new SquareForms[FIELD_WIDTH * FIELD_HEIGHT];
		gameFieldColor = new Color[FIELD_WIDTH * FIELD_HEIGHT];

		clearGameField();

		field[2 * FIELD_WIDTH] = SquareForms.ManFigure;
		currentManX = 0;
		currentManY = 2;
		step = 1;

		btnRotation.setVisible(true);
		scoreField.setText(String.valueOf(removedLinesScore));

		playSound(manGreetingSound, false, noSound);

		newFigure();

		timer = new Timer(PERIOD_INTERVAL, new GameCycle());
		timer.start();

		timerManBurping = new Timer(PERIOD_INTERVAL_ManBurping, new ManBurpCycle());
		timerManBurping.start();
	}

	private void turnSoundOnOff(boolean off, boolean changeSoundIcon) {
		if (!(!changeSoundIcon && btnSound.getIcon() == iconNoSound))
			noSound = off;
		if (noSound) {
			if (changeSoundIcon) {
				btnSound.setIcon(iconNoSound);
				btnSound.setToolTipText("Turn on the sound");
			}
			if (manSingingSound.isPlaying()) {
				currentVolume = manSingingSound.getVolume();
				manSingingSound.setVolume(0);
			}
		} else {
			if (changeSoundIcon) {
				btnSound.setIcon(iconSound);
				btnSound.setToolTipText("Turn off the sound");
			}
			manSingingSound.setVolume(currentVolume);
		}
	}
	
	private int squareWidth() {

		return (int) getSize().getWidth() / FIELD_WIDTH;
	}

	private int squareHeight() {

		return (int) getSize().getHeight() / FIELD_HEIGHT;
	}

	private SquareForms figureAt(int x, int y) {

		return field[(y * FIELD_WIDTH) + x];

	}

	private Color colorAt(int x, int y) {

		return gameFieldColor[(y * FIELD_WIDTH) + x];

	}

	private void playSound(Sound sound, boolean breakOld, boolean noSound) {
		if (noSound && sound.getVolume() > 0) {
			currentVolume = sound.getVolume();
			sound.setVolume(0);
		}
		else if (!noSound && sound.getVolume() == 0) {
			sound.setVolume(currentVolume);
		}
		sound.play(breakOld, noSound);
		
	}

	private Color getContrastColor() {
		boolean flag = false;
		int rand1 = 0;
		int rand2 = 0;
		int rand3 = 0;
		while (!flag) {
			Random rand = new Random();
			rand1 = rand.nextInt(256);
			rand2 = rand.nextInt(256);
			rand3 = rand.nextInt(256);
			if (rand1 + rand2 + rand3 < 510 && rand1 + rand2 + rand3 > 210) {
				flag = true;
			}
		}
		return new Color(rand1, rand2, rand3);
	}

	private void pause() {

		isPaused = !isPaused;

		turnSoundOnOff(isPaused, false);
		if (isPaused) {
			JOptionPane.showMessageDialog(null, "Paused");
			isPaused = false;
			turnSoundOnOff(isPaused, false);
		}
		repaint();
	}

	private void announceAddingNewFigures(Boolean reflectbtn, Boolean gameFieldPreview) {
		isPaused = true;
		String message = "";
		if (gameFieldPreview) {
			message = "You've got new figures and Preview field!";
		} else if (reflectbtn) {
			message = "You've got new figures and Reflect button!";
		} else {
			message = "You've got new figures!";
		}
		JOptionPane.showMessageDialog(null, message);
		isPaused = false;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		doDrawing(g);
	}

	private void doDrawing(Graphics g) {

		var size = getSize();
		int gameFieldTop = (int) size.getHeight() - FIELD_HEIGHT * squareHeight();

		for (int i = 0; i < FIELD_HEIGHT; i++) {

			for (int j = 0; j < FIELD_WIDTH; j++) {

				SquareForms figure = figureAt(j, FIELD_HEIGHT - i - 1);
				Color col = colorAt(j, FIELD_HEIGHT - i - 1);

				if (figure == SquareForms.ManFigure) {
					if (manDied) {

						g.drawImage(image, j * squareWidth(), gameFieldTop + i * squareHeight(), squareWidth(),
								squareHeight(), this);

					} else if (image == imageManSpinning) {
						g.drawImage(image, j * squareWidth(), gameFieldTop + i * squareHeight() - squareHeight(),
								squareWidth(), squareHeight() * 2, this);
					} else {
						if (step == 1)
							g.drawImage(image, j * squareWidth(), gameFieldTop + i * squareHeight() - squareHeight(),
									squareWidth(), squareHeight() * 2, this);
						else
							g.drawImage(image, j * squareWidth(), gameFieldTop + i * squareHeight() - squareHeight(),
									squareWidth(), squareHeight() * 2, this);
					}

				} else if (figure != SquareForms.CleanFigure) {

					drawSquare(g, j * squareWidth(), gameFieldTop + i * squareHeight(), figure, col);
				}

				else {
					drawSquareClearGameField(g, j * squareWidth(), gameFieldTop + i * squareHeight());

				}
			}
		}

		if (currentFigure.getFigure() != SquareForms.CleanFigure) {

			for (int i = 0; i < 6; i++) {

				int x = currentFigureX + currentFigure.x(i);
				int y = currentFigureY - currentFigure.y(i);

				drawSquare(g, x * squareWidth(), gameFieldTop + (FIELD_HEIGHT - y - 1) * squareHeight(),
						currentFigure.getFigure(), color);
			}
		}

	}

	private void dropDown() {

		int newY = currentFigureY;

		while (newY > 0) {

			if (!moveFigure(currentFigure, currentFigureX, newY - 1)) {

				break;
			}

			newY--;
		}

		figureDropped();
	}

	private void oneLineDown() {

		if ((!moveFigure(currentFigure, currentFigureX, currentFigureY - 1)) || manDied) {

			figureDropped();
		}

	}

	private void manGoDown() {
		
		if (image != imageManJumping
				&& image != imageManSpinning) {

			while (currentManY - 1 >= 0) {
				int x = currentManX;
				int y = currentManY;
				if (figureAt(x, y - 1) == SquareForms.CleanFigure) {
					currentManY = y - 1;
					repaint();
					manStepped();
					field[(y * FIELD_WIDTH) + x] = SquareForms.CleanFigure;
					gameFieldColor[(y * FIELD_WIDTH) + x] = Color.GRAY.brighter();
					repaint();
				}
				else {
					break;
				}
			}
		}	
	}
	
	private void oneMansStep() {
		int x = currentManX;
		int y = currentManY;

		if (x + step >= 0 && x + step < FIELD_WIDTH 			// stepping down
				&& y - 1 >= 0 && figureAt(x + step, y - 1) == SquareForms.CleanFigure
				&& figureAt(x + step, y) == SquareForms.CleanFigure
				&& (image == imageManJumping
						|| (y - 1 == 0 || (y - 2 >= 0 && figureAt(x + step, y - 2) != SquareForms.CleanFigure)))
				&& placeFreeOfCurpiece(x + step, y - 1)) {

			if (image != imageManJumping) {
				if (step == 1)
					image = imageManGoingRight;
				else
					image = imageManGoingLeft;
			}
			currentManX = x + step;
			currentManY = y - 1;
		}

		else if (x + step >= 0 && x + step < FIELD_WIDTH 		// stepping forward
				&& figureAt(x + step, y) == SquareForms.CleanFigure
				&& figureAt(x + step, y + 1) == SquareForms.CleanFigure
				&& (y == 0 || figureAt(x + step, y - 1) != SquareForms.CleanFigure)
				&& placeFreeOfCurpiece(x + step, y)) {

			if (step == 1)
				image = imageManGoingRight;
			else
				image = imageManGoingLeft;
			currentManX = x + step;
		}

		else if (x + step >= 0 && x + step < FIELD_WIDTH 		// stepping up
				&& y + 1 <= FIELD_HEIGHT && y + 2 <= FIELD_HEIGHT
				&& figureAt(x + step, y + 1) == SquareForms.CleanFigure && y + 2 < FIELD_HEIGHT
				&& figureAt(x + step, y + 2) == SquareForms.CleanFigure
				&& figureAt(x + step, y) != SquareForms.CleanFigure && placeFreeOfCurpiece(x + step, y + 1)) {

			if (image == imageManDisappearingReturn) {
				if (step == 1)
					image = imageManGoingRight;
				else
					image = imageManGoingLeft;
			}

			if (y - 1 >= 0) {
				if (image == imageManSpinning && figureAt(x, y - 1) != SquareForms.CleanFigure) {
					if (step == 1)
						image = imageManGoingRight;
					else
						image = imageManGoingLeft;
				}
			}

			currentManX = x + step;
			currentManY = y + 1;
		}

		else if ( 												// flying
				(x + step < 0 || x + step >= FIELD_WIDTH || figureAt(x + step, y + 1) != SquareForms.CleanFigure)
				&& (x - step < 0 || x - step >= FIELD_WIDTH || figureAt(x - step, y + 1) != SquareForms.CleanFigure)
				&& placeFreeOfCurpiece(x, y + 1)
				&& (y + 2 <= FIELD_HEIGHT && figureAt(x, y + 2) == SquareForms.CleanFigure)) {

			image = imageManSpinning;
			manSingingSound.stop();
			playSound(manLaughingSound, false, noSound);
			currentManX = x;
			currentManY = y + 1;

		}

		else if ( 												// disappearing and appearing
				(x + step < 0 || x + step >= FIELD_WIDTH || figureAt(x + step, y + 1) != SquareForms.CleanFigure)
				&& (x - step < 0 || x - step >= FIELD_WIDTH || figureAt(x - step, y + 1) != SquareForms.CleanFigure)
				&& placeFreeOfCurpiece(x, y + 1)
				&& !(y + 2 <= FIELD_HEIGHT && figureAt(x, y + 2) == SquareForms.CleanFigure)) {

			if (image == imageManDisappearing) {
				int newY = FIELD_HEIGHT - 2;
				int newX;
				if (currentFigureX >= FIELD_WIDTH / 2) {
					newX = 0;
				} else {
					newX = FIELD_WIDTH - 1;
				}
				if (figureAt(newX, newY) == SquareForms.CleanFigure 	// disappearing and appearing in far side
						&& figureAt(newX, newY + 1) == SquareForms.CleanFigure && newY - 1 >= 0) {
					while (newY - 1 >= 0 && figureAt(newX, newY - 1) == SquareForms.CleanFigure) {
						newY--;
					}
				} else {												// disappearing and appearing in other side
					if (newX == 0) {
						newX = FIELD_WIDTH - 1;
					} else {
						newX = 0;
					}
					if (figureAt(newX, newY) == SquareForms.CleanFigure // disappearing and appearing in other side
							&& figureAt(newX, newY + 1) == SquareForms.CleanFigure && newY - 1 >= 0) {
						while (newY - 1 >= 0 && figureAt(newX, newY - 1) == SquareForms.CleanFigure) {
							newY--;
						}
					} else { 											// both sides are filled, game is over
						manDied = true;
						field[(y * FIELD_WIDTH) + x] = SquareForms.CleanFigure;
						gameFieldColor[(y * FIELD_WIDTH) + x] = Color.GRAY.brighter();
						repaint();
						timerManBurping.stop();
						timer.stop();
						currentFigure.setFigure(SquareForms.CleanFigure);
						var message = String.format("Game over. Score: %d", removedLinesScore);
						restart(message);
						return;
					}
				}

				currentManX = newX;
				currentManY = newY;
				field[(y * FIELD_WIDTH) + x] = SquareForms.CleanFigure;
				gameFieldColor[(y * FIELD_WIDTH) + x] = Color.GRAY.brighter();
				repaint();
				neededManSingingSound = true;
				image = imageManDisappearingReturn;
			} else {

				if (manSingingSound.isPlaying()) {
					image = imageManSinging;
					neededManSingingSound = false;
				}

				else if (neededManSingingSound) {
					playSound(manSingingSound, false, noSound);
					image = imageManSinging;
					neededManSingingSound = false;
				}

				if (!manSingingSound.isPlaying()) {
					image = imageManDisappearing;
					repaint();
					neededManSingingSound = true;
				}
			}

		}

		else {
			step = step * -1;
			if (image != imageManSpinning || y - 1 == 0 || figureAt(x, y - 1) != SquareForms.CleanFigure) {
				if (step == 1)
					image = imageManGoingRight;
				else
					image = imageManGoingLeft;
			}
		}

		repaint();
		if (x != currentManX || y != currentManY) {
			manStepped();
			field[(y * FIELD_WIDTH) + x] = SquareForms.CleanFigure;
			gameFieldColor[(y * FIELD_WIDTH) + x] = Color.GRAY.brighter();
			repaint();
		}
	}

	private boolean placeFreeOfCurpiece(int manX, int manY) {
		for (int i = 0; i < 6; i++) {

			int newX = currentFigureX + currentFigure.x(i);
			int newY = currentFigureY + currentFigure.y(i);

			if (newY - 1 >= 0) {
				if (newX == manX && (newY - 1 == manY || newY - 1 == manY + 1 || newY == manY + 1 || newY == manY)) {
					return false;
				}
			}
		}
		return true;
	}

	private void clearGameField() {

		for (int i = 0; i < FIELD_HEIGHT * FIELD_WIDTH; i++) {

			field[i] = SquareForms.CleanFigure;
			gameFieldColor[i] = Color.GRAY.brighter();
		}
	}

	private void figureDropped() {

		for (int i = 0; i < 6; i++) {

			int x = currentFigureX + currentFigure.x(i);
			int y = currentFigureY - currentFigure.y(i);
			field[(y * FIELD_WIDTH) + x] = currentFigure.getFigure();
			gameFieldColor[(y * FIELD_WIDTH) + x] = color;
		}

		removeLines();

		color = nextColor;
		nextColor = getContrastColor();
		gameFieldPreview.nextColorPreview = nextColor;
		gameFieldPreview.repaint();

		if (!isFallingFinished) {
			newFigure();
		}
	}

	private void manStepped() {

		int x = currentManX;
		int y = currentManY;
		field[(y * FIELD_WIDTH) + x] = SquareForms.ManFigure;
	}

	private void newFigure() {

		if (nextFigure == null) {
			nextFigure = currentFigure.getRandomFigure(figureQuantity);
			gameFieldPreview.nextSquareFormsPreview = nextFigure;
		}
		currentFigure.setFigure(nextFigure);
		nextFigure = currentFigure.getRandomFigure(figureQuantity);
		gameFieldPreview.nextSquareFormsPreview = nextFigure;
		gameFieldPreview.newPiece();

		currentFigureX = FIELD_WIDTH / 2 + 1;
		currentFigureY = FIELD_HEIGHT - 1 + currentFigure.minY();

		if (!moveFigure(currentFigure, currentFigureX, currentFigureY)) {
			timerManBurping.stop();
			timer.stop();

			currentFigure.setFigure(SquareForms.CleanFigure);

			var message = String.format("Game over. Score: %d", removedLinesScore);
			restart(message);
		}
	}

	private void restart(String message) {

		scoreField.setText(message);
		btnReflect.setVisible(false);
		btnRotation.setVisible(false);
		gameFieldPreview.setVisible(false);

		int dialogButton = JOptionPane.YES_NO_OPTION;
		int reply = JOptionPane.showConfirmDialog(null, "Play again?", "WARNING", dialogButton);

		if (reply == 0) {
			clearGameField();
			start();
		} else {
			System.exit(0);
		}
	}

	private boolean moveFigure(Figure newPiece, int newX, int newY) {

		for (int i = 0; i < 6; i++) {

			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);

			if (x < 0 || x >= FIELD_WIDTH || y < 0 || y >= FIELD_HEIGHT) {

				return false;
			}

			if (figureAt(x, y) != SquareForms.CleanFigure) {

				return false;
			}
		}

		for (int i = 0; i < 6; i++) {

			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);

			if (y - 1 >= 0) {
				if (figureAt(x, y - 1) == SquareForms.ManFigure) {
					manDied = true;
					image = imageBlot;
				}
			}
		}

		currentFigure = newPiece;
		currentFigureX = newX;
		currentFigureY = newY;
		if (manDied) {
			timerManBurping.stop();
			timer.stop();
			if (neededManDyingSound) {
				playSound(manDyingSound, false, noSound);
				neededManDyingSound = false;
			}
			timer.start();
		}
		repaint();

		return true;

	}

	private void removeLines() {

		int numFullLines = 0;

		for (int i = FIELD_HEIGHT - 1; i >= 0; i--) {

			boolean lineIsFull = true;

			for (int j = 0; j < FIELD_WIDTH; j++) {

				if (figureAt(j, i) == SquareForms.CleanFigure || figureAt(j, i) == SquareForms.ManFigure) {

					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {

				numFullLines++;

				for (int k = i; k < FIELD_HEIGHT - 1; k++) {
					for (int j = 0; j < FIELD_WIDTH; j++) {
						if (field[(k * FIELD_WIDTH) + j] == SquareForms.ManFigure) {
							currentManY = currentManY - 1;
						}
						field[(k * FIELD_WIDTH) + j] = figureAt(j, k + 1);
						gameFieldColor[(k * FIELD_WIDTH) + j] = colorAt(j, k + 1);
					}
				}
				
				manGoDown();
				
				oneMansStep();
			}
		}

		if (numFullLines > 0) {

			removedLinesScore += numFullLines;

			scoreField.setText(String.valueOf(removedLinesScore));
			isFallingFinished = true;
			currentFigure.setFigure(SquareForms.CleanFigure);
			if (removedLinesScore >= 10 && removedLinesScore < 20 && figureQuantity < 11) {
				figureQuantity = 11;
				announceAddingNewFigures(false, false);
			} else if (removedLinesScore >= 20 && figureQuantity < 16) {
				figureQuantity = 16;
				announceAddingNewFigures(true, false);
				btnReflect.setVisible(true);
			} else if (removedLinesScore >= 30 && figureQuantity < 19) {
				figureQuantity = 19;
				announceAddingNewFigures(true, true);
				gameFieldPreview.setVisible(true);
			}
		}
	}

	private void drawSquare(Graphics g, int x, int y, SquareForms shape, Color color) {

		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}

	private void drawSquareClearGameField(Graphics g, int x, int y) {

		Color color = Color.GRAY.brighter();
		g.setColor(color);
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

	}

	private class GameCycle implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			doGameCycle();
		}
	}

	private class ManBurpCycle implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (!(image == imageManSpinning || manSingingSound.isPlaying() || isPaused))
				playSound(manBurpingSound, false, noSound);
		}
	}

	private void doGameCycle() {

		update();
		repaint();

	}

	private void update() {

		if (isPaused) {

			return;
		}

		if (isFallingFinished) {

			isFallingFinished = false;
			newFigure();

		} else {

			if (!manDied) {
				oneMansStep();
			}

			oneLineDown();
		}
	}

	class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			if (currentFigure.getFigure() == SquareForms.CleanFigure) {

				return;
			}

			int keycode = e.getKeyCode();

			switch (keycode) {

			case KeyEvent.VK_P:
				pause();
				break;
			case KeyEvent.VK_LEFT:
				moveFigure(currentFigure, currentFigureX - 1, currentFigureY);
				break;
			case KeyEvent.VK_RIGHT:
				moveFigure(currentFigure, currentFigureX + 1, currentFigureY);
				break;
			case KeyEvent.VK_DOWN:
				moveFigure(currentFigure.rotateRight(), currentFigureX, currentFigureY);
				break;
			case KeyEvent.VK_UP:
				if (rightRotation) {
					moveFigure(currentFigure.rotateRight(), currentFigureX, currentFigureY);
				} else {
					moveFigure(currentFigure.rotateLeft(), currentFigureX, currentFigureY);
				}
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			case KeyEvent.VK_D:
				oneLineDown();
				break;
			case KeyEvent.VK_E:
				rightRotation = !rightRotation;

				if (rightRotation == true) {
					btnRotation.setIcon(iconRotationRight);
				} else {
					btnRotation.setIcon(iconRotationLeft);
				}

				break;
			case KeyEvent.VK_R:
				if (btnReflect.isVisible()) {
					moveFigure(currentFigure.reflect(), currentFigureX, currentFigureY);
				}
				break;

			}
		}
	}
}
