package Breakbrick;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class Gameplay extends JPanel implements ActionListener, MouseListener, KeyListener {
    private boolean play = false;
    private boolean showStart = true;
    private Timer timer;

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirX = -1;
    private int ballDirY = -2;

    private int score = 0;
    private int totalBricks = 21;

    private MapGenerator map;

    public Gameplay() {
        addMouseListener(this);
        addKeyListener(this); // Listen to key events
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        map = new MapGenerator(3, 7);
        int delay = 8;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Draw map
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(691, 0, 3, 592);

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 540, 30);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // START button
        if (showStart && !play) {
            g.setColor(Color.ORANGE);
            g.setFont(new Font("serif", Font.BOLD, 40));
            g.drawString("START", 270, 350);
        }

        // Win
        if (totalBricks <= 0) {
            play = false;
            ballDirX = ballDirY = 0;

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!", 260, 300);
            drawRestartExit(g);
        }

        // Game Over
        if (ballPosY > 570) {
            play = false;
            ballDirX = ballDirY = 0;

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);
            drawRestartExit(g);
        }

        g.dispose();
    }

    private void drawRestartExit(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("RESTART", 250, 350);
        g.drawString("EXIT", 280, 400);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            // Ball - paddle interaction
            Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
            Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);

            if (ballRect.intersects(paddleRect)) {
                ballDirY = -ballDirY;
            }

            // Ball - brick interaction
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }

                            break A;
                        }
                    }
                }
            }

            // Ball movement
            ballPosX += ballDirX;
            ballPosY += ballDirY;

            // Wall collisions
            if (ballPosX < 0) ballDirX = -ballDirX;
            if (ballPosY < 0) ballDirY = -ballDirY;
            if (ballPosX > 670) ballDirX = -ballDirX;
        }

        repaint();
    }

    // Mouse click handler
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // START click
        if (showStart && x >= 270 && x <= 370 && y >= 310 && y <= 360) {
            play = true;
            showStart = false;
            repaint();
        }

        // RESTART click
        if (!play && x >= 250 && x <= 370 && y >= 320 && y <= 360) {
            restartGame();
        }

        // EXIT click
        if (!play && x >= 280 && x <= 350 && y >= 370 && y <= 410) {
            System.exit(0);
        }
    }

    private void restartGame() {
        play = true;
        ballPosX = 120;
        ballPosY = 350;
        ballDirX = -1;
        ballDirY = -2;
        playerX = 310;
        score = 0;
        totalBricks = 21;
        map = new MapGenerator(3, 7);
        repaint();
    }

    // Keyboard control for paddle
    @Override
    public void keyPressed(KeyEvent e) {
        if (play) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (playerX < 600) {
                    playerX += 20;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (playerX > 10) {
                    playerX -= 20;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    // Unused mouse methods
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
