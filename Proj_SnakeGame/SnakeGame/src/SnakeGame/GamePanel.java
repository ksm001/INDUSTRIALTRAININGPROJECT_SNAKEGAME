package SnakeGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	private int[] snake_xlength = new int[750];
    private int[] snake_ylength = new int[750];
    private int lengthofSnake = 3;  //Length of the snake at the start.
    // Arrays for position of the enemy
    private int[] xPos = {25,50,75,100,125,150,175,200,225,250,275,300,
                          325,350,375,400,425,450,475,500,525,550,575,
                          600,625,650,675,700,725,750,775,800,825,850};
    private int[] yPos = {75,100,125,150,175,200,225,250,275,300,325,350,
                           375,400,425,450,475,500,525,550,575,600,625};
    
    private Random random = new Random();
    //stores current position of enemy
    private int enemyX,enemyY;
    //Direction Values
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    private int moves = 0;
    private int score = 0;
    private boolean gameOver = false;
    private boolean gamePause = false;
    //Assets
    private ImageIcon snaketitle = new ImageIcon( getClass().getResource("snaketitle.jpg"));
    private ImageIcon leftmouth = new ImageIcon( getClass().getResource("leftmouth.png"));
    private ImageIcon rightmouth = new ImageIcon( getClass().getResource("rightmouth.png"));
    private ImageIcon upmouth = new ImageIcon( getClass().getResource("upmouth.png"));
    private ImageIcon downmouth = new ImageIcon( getClass().getResource("downmouth.png"));
    private ImageIcon snakeimage = new ImageIcon( getClass().getResource("snakeimage.png"));
    private ImageIcon enemy = new ImageIcon( getClass().getResource("enemy.png"));

    private Timer timer;
    private int delay=150;
    //Constructor
    GamePanel(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        timer = new Timer(delay,this);
        timer.start();
        newEnemy();
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        //Title Image Border
        g.setColor(Color.WHITE);
        g.drawRect(24, 10, 851, 55);
        //Draw the title Image
        snaketitle.paintIcon(this, g, 25, 11); 
        //Border for gameplay
        g.drawRect(24, 74, 851, 576);
        //Draw Background for the gameplay
        g.setColor(Color.BLACK);
        g.fillRect(25, 75, 850, 575);
        //Drawing the initial position of the snake.
        if(moves==0){
            snake_xlength[0] = 100;
            snake_xlength[1] = 75;
            snake_xlength[2] = 50;

            snake_ylength[0] = 100;
            snake_ylength[1] = 100;
            snake_ylength[2] = 100;
        }
        //Determining which mouth to paint for the corresponding directions.
        if(left){
            leftmouth.paintIcon(this, g, snake_xlength[0], snake_ylength[0]);
        }
        if(right){
            rightmouth.paintIcon(this, g, snake_xlength[0], snake_ylength[0]);
        }
        if(up){
            upmouth.paintIcon(this, g, snake_xlength[0], snake_ylength[0]);
        }
        if(down){
            downmouth.paintIcon(this, g, snake_xlength[0], snake_ylength[0]);
        }
        //painting the body of the snake.
        for(int i=1;i<lengthofSnake;i++){
            snakeimage.paintIcon(this, g, snake_xlength[i], snake_ylength[i]);
        }
        //painting the enemy.
        enemy.paintIcon(this, g, enemyX, enemyY);
        
        //END SCREEN
        if(gamePause == true){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arail",Font.BOLD,50));
            g.drawString("GAME PAUSED", 280, 300);

            g.setFont(new Font("Arail",Font.PLAIN,20));
            g.drawString("PRESS P TO PLAY", 360, 350);
        }
        if(gameOver){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arail",Font.BOLD,50));
            g.drawString("GAME OVER", 300, 300);

            g.setFont(new Font("Arail",Font.PLAIN,20));
            g.drawString("PRESS SPACE TO RESTART", 320, 350);
        }
        
        //To paint Score and Length
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial",Font.PLAIN,14));
        g.drawString("SCORE : "+score, 750, 30);
        g.drawString("LENGTH : "+lengthofSnake, 750, 50);

        g.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        //To determine the position of snake on traversing to different directions.
    	for(int i=lengthofSnake-1;i>0;i--) {
    		snake_xlength[i] = snake_xlength[i-1];
    		snake_ylength[i] = snake_ylength[i-1];
    	}
        if(left){
            snake_xlength[0]=snake_xlength[0]-25;
        }
        if(right){
            snake_xlength[0]=snake_xlength[0]+25;
        }
        if(up){
            snake_ylength[0]=snake_ylength[0]-25;
        }
        if(down){
            snake_ylength[0]=snake_ylength[0]+25;
        }

        //If Snake goes to either ends of the screen It reappears on the opposite side. 
        if(snake_xlength[0]>850) {
        	snake_xlength[0]=25;		
        }
        if(snake_xlength[0]<25) {
        	snake_xlength[0]=850;		
        }
        if(snake_ylength[0]>625) {
        	snake_ylength[0]=75;		
        }
        if(snake_ylength[0]<75) {
        	snake_ylength[0]=625;		
        }
        
        collidesWithEnemy();
        collidesWithBody();
        repaint();
    }

    //Function to determine what happens if any Key is Pressed.
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE && gameOver == false){
            pause();
        }
        if(e.getKeyCode()==KeyEvent.VK_P && gamePause==true) {
        	play();
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE && gameOver == true){
            restart();
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT && (!right)){
            left=true;
            right=false;
            up=false;
            down=false;
            moves++;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT && (!left)){
            left=false;
            right=true;
            up=false;
            down=false;
            moves++;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP && (!down)){
            left=false;
            right=false;
            up=true;
            down=false;
            moves++;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN && (!up)){
            left=false;
            right=false;
            up=false;
            down=true;
            moves++;
        }
    }

	@Override
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) { 
    }
    
    //Method to determine the random position of the enemy.
    private void newEnemy() {
        enemyX = xPos[random.nextInt(34)];
        enemyY = yPos[random.nextInt(23)];
        
        //Loop is used so that the enemy does not spawn on the snake.
        for(int i=lengthofSnake-1;i>=0;i--){
            if(snake_xlength[i]==enemyX && snake_ylength[i]==enemyY){
                newEnemy();
            }
        }
    }
    //Method to determine what happens when Snake collides with the enemy.
    private void collidesWithEnemy() {
        if(snake_xlength[0]==enemyX && snake_ylength[0]==enemyY){
            newEnemy();
            lengthofSnake++;
            score++;
        }
    }
    //Method to determine what happens when Snake collides with itself.
    private void collidesWithBody() {
        for(int i=lengthofSnake-1;i>0;i--){
            if(snake_xlength[i]==snake_xlength[0] && snake_ylength[i]==snake_ylength[0]){
                timer.stop();
                gameOver=true;
            }
        }
    }
    //Method for restarting the game.
    private void restart() {
        gameOver = false;
        moves = 0;
        score = 0;
        lengthofSnake =3;
        left = false;
        right = true;
        up = false;
        down = false;
        timer.start();
        newEnemy();
        repaint();
    }
    private void pause() {
        gamePause = true;
    	timer.stop();
        repaint();
	}
	private void play() {
		gamePause = false;
		timer.start();
	}
}