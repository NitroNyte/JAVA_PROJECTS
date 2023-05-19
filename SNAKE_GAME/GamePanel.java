import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.util.Vector;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
public class GamePanel extends JPanel implements ActionListener{
	private Image background;
	private Image apple;
	private Image head_down;
	private Image head_up;
	private Image head_left;
	private Image head_right;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 30;
	static final int body = 35;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 120;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	private int bodyParts = 3;
	int applesEaten = 0;
	private int HighScore = 0;
	private int appleX;
	private int appleY;
	char direction = 'R';
	private boolean running = false;
	Timer timer;
	Random random;
	private Image head;
	private Image tail_down;
	private Image tail_up;
	private Image tail_left;
	private Image tail_right;
	private JButton resetButton;
	private JButton stopButton;
	static Clip clip;
	//Use the png's to rezise the images for them to be used in the program
	GamePanel(){
	
		apple = (new ImageIcon("apple.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		head_down = (new ImageIcon("head_down.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		head_up = (new ImageIcon("head_up.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		head_left = (new ImageIcon("head_left.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		head_right = (new ImageIcon("head_right.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		background = new ImageIcon("grass03.png").getImage();
		tail_down = (new ImageIcon("tail_down.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		tail_up = (new ImageIcon("tail_up.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		tail_left = (new ImageIcon("tail_left.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		tail_right = (new ImageIcon("tail_right.png").getImage()).getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_DEFAULT);
		
		try {
			// Replace "audioFile.wav" with the path to your audio file
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
					getClass().getResourceAsStream("munch.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

		resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
		resetButton.setVisible(false); // Initially hidden
        add(resetButton);

		
		stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Terminate the program
            }
        });
        stopButton.setVisible(false); // Initially hidden
        add(stopButton);
        
		background = background.getScaledInstance(SCREEN_WIDTH, SCREEN_HEIGHT, Image.SCALE_DEFAULT);
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
		
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		//Create 2D graphics constructor and add costum background
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(background, 0, 0, null);
		//Draw lines
		if(running) {
			//This part is optional, if the viewer is interested in seeing the lines then delete the comment tags
			//We didn't add them cause of personal preference, but you can do as you wish
		for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
			//  g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
			//  g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
		}
		g.setColor(Color.RED);
		
		g2D.drawImage(apple, appleX, appleY, null);

		//Draw head of snake
		for(int i=0; i<bodyParts;i++) {
			if(i==0) {
				switch(direction) {
					case 'U':
						head = head_up; 
						//tail = tail_down; 
						break;
					case 'D':
						head = head_down;
						//tail = tail_up;  
						
						break;
					case 'L':
						head = head_left;
						//tail = tail_right;
						break;
					case 'R':
						head = head_right;
						//tail = tail_left;
						break;
					}
					//Head of snake
				g2D.drawImage(head, x[i], y[i],UNIT_SIZE, UNIT_SIZE, null);
				
				// g.setColor(Color.GREEN);
				
			}
			//Checks for the direction of the tail to draw it using the real life logic
			else if(i == bodyParts - 1) {

				char tailDirection = getTailDirection();
				switch (tailDirection) {
					case 'U':
						g2D.drawImage(tail_up, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
						
						break;
					case 'D':
						g2D.drawImage(tail_down, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
						break;
					case 'L':
						g2D.drawImage(tail_left, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
						
						break;
					case 'R':
						g2D.drawImage(tail_right, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
						break;
				}
				
			}
			//Draws the rest of the boddy
			else {
				 g.setColor(new Color(45,180,0));
				 g.setColor(new Color(91, 123, 249));
				 g.drawRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 27, 10);
				 g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 27, 10);
				 g.setColor(new Color(81, 146, 169));
				 g.drawRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 27, 10);
				 //g.fillRect(x[i],y[i], UNIT_SIZE ,UNIT_SIZE);
			}
		}



		//Text that display's how many apple's the snake has eaten
		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Calibri Body", Font.BOLD, 20));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g2D.drawImage(apple, 500, 550, null);
		
		g.drawString("" + applesEaten, 540, 573);
		
		Graphics2D g3 = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(3);
		g3.setStroke(stroke);
		g3.drawRect(500, 545, 68, 45);
		
		}
		else {
			gameOver(g);
		}
	}
	//Give random coordinates so that the apple doesn't spawn in the same place
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	//Move method
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE; 
			break;
		case 'D':
			
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

		
	}
	//If snake has eaten the apple increment a boddy part and create a new apple
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			playEatSound();
			applesEaten = applesEaten + 1;
			newApple();
		}

		
	}
	public void checkCollisions() {
		//Checks if head collides with body
		for(int i=bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}

		
		//Checks if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//Checks if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//Checks if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//Checks if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void showGameOverWindow() {
        resetButton.setVisible(true);
        stopButton.setVisible(true);
        repaint();
    }

	public void gameOver(Graphics g) {
		//SCORE
		HighScore = applesEaten > HighScore ? applesEaten : HighScore;
		g.setColor(new Color(3, 101, 100));
		g.setFont(new Font("Sans Serif", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Apple's eaten: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Apple's eaten: "))/2 - 13, 350);
		//GameOver Text
		g.setFont(new Font("Sans Serif", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.setColor(new Color(204, 51, 53));
		g.drawString("GAME OVER!", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER!"))/2, SCREEN_HEIGHT/2);
		g.setColor(new Color(119, 204, 164));
		g.setFont(new Font("Sans Serif", Font.BOLD, 50));
		g.drawString("Highest Score: " + HighScore, (SCREEN_WIDTH - metrics1.stringWidth("High Score: " + HighScore)) - 235, (SCREEN_HEIGHT) - 450 );
		showGameOverWindow();
		resetButton.setVisible(true);
		//repaint();
		
	}
	

	//Checks if game is running then proceeds to the next methods, then at last repaint so that the design is accurate
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	//For the tail to move logically so that the design isn't off we created this method
	private char getTailDirection() {
		int dx = x[bodyParts - 1] - x[bodyParts - 2];
		int dy = y[bodyParts - 1] - y[bodyParts - 2];
	
		if (dx == -UNIT_SIZE) {
			return 'L';
		} else if (dx == UNIT_SIZE) {
			return 'R';
		} else if (dy == -UNIT_SIZE) {
			return 'U';
		} else if (dy == UNIT_SIZE) {
			return 'D';
		}
	
		return 'R'; // Default direction if no valid direction is found
	}
	public int highScore() {
		if(applesEaten > HighScore) {
			HighScore = applesEaten;
			return HighScore;
		}
		return HighScore;
	}

	private static void playEatSound() {
        if (clip.isRunning()) {
            // Stop the sound if it's already playing
            clip.stop();
        }
        // Rewind the sound to the beginning
        clip.setFramePosition(0);
        // Play the sound
        clip.start();
    }
	public void resetGame() {
        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';
        running = false;
        timer.stop();

        // Reset snake position
        x[0] = UNIT_SIZE * 3;
        y[0] = UNIT_SIZE * 3;

        // Generate new apple
        newApple();

        // Start the game again
        running = true;
        timer.start();
		resetButton.setVisible(false); 
		stopButton.setVisible(false);
        revalidate();
        repaint();

        // Request focus on the game panel
        this.requestFocus();

        // Update the UI
        
		
		repaint();
    }
	

	//Reacts based on the key's we pressed
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
	
}



