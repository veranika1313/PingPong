import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PingPongGame extends JPanel implements Runnable, KeyListener, MouseMotionListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 60;
    private static final int BALL_DIAMETER = 10;
    private static final int MAX_SCORE = 10;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 2;

    private int paddle1Y = 250;
    private int paddle2Y = 250;
    private int ballX = 400;
    private int ballY = 300;
    private int ballXSpeed = BALL_SPEED;
    private int ballYSpeed = BALL_SPEED;
    private int player1Score = 0;
    private int player2Score = 0;

    private boolean ballMoving = false;

    public PingPongGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);

        g.fillRect(WIDTH / 2, 0, 2, HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics fontMetrics = g.getFontMetrics();
        g.drawString(Integer.toString(player1Score), (WIDTH / 2) - (fontMetrics.stringWidth(Integer.toString(player1Score)) + 40),
                50);
        g.drawString(Integer.toString(player2Score), (WIDTH / 2) + 20, 50);

        g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
        g.fillRect(10, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - PADDLE_WIDTH - 10, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (ballMoving) {
            ballX += ballXSpeed;
            ballY += ballYSpeed;

            if (ballY <= 0 || ballY >= HEIGHT - BALL_DIAMETER) {
                ballYSpeed *= -1;
            }

            if (ballX <= BALL_DIAMETER || ballX >= WIDTH - BALL_DIAMETER) {
                ballXSpeed *= -1;

                if (ballX <= BALL_DIAMETER) {
                    player2Score++;
                } else {
                    player1Score++;
                }

                if (player1Score == MAX_SCORE || player2Score == MAX_SCORE) {
                    ballMoving = false;
                    saveScoreToFile();
                }
            }

            if (ballX <= PADDLE_WIDTH + 20 && ballY + BALL_DIAMETER >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
                ballXSpeed *= -1;
            }

            if (ballX >= WIDTH - PADDLE_WIDTH - BALL_DIAMETER - 20 && ballY + BALL_DIAMETER >= paddle2Y
                    && ballY <= paddle2Y + PADDLE_HEIGHT) {
                ballXSpeed *= -1;
            }
        }
    }

    public void saveScoreToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
            writer.write("Player 1: " + player1Score + " | Player 2: " + player2Score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_SPACE) {
            if (!ballMoving) {
                ballMoving = true;
                ballX = 400;
                ballY = 300;
                ballXSpeed = BALL_SPEED;
                ballYSpeed = BALL_SPEED;
                player1Score = 0;
                player2Score = 0;
            }
        }

        if (keyCode == KeyEvent.VK_UP && paddle2Y > 0) {
            paddle2Y -= PADDLE_SPEED;
        }

        if (keyCode == KeyEvent.VK_DOWN && paddle2Y + PADDLE_HEIGHT < HEIGHT) {
            paddle2Y += PADDLE_SPEED;
        }
    }

    public void mouseDragged(MouseEvent e) {
        paddle1Y = e.getY() - PADDLE_HEIGHT / 2;
        if (paddle1Y < 0) {
            paddle1Y = 0;
        }
        if (paddle1Y + PADDLE_HEIGHT > HEIGHT) {
            paddle1Y = HEIGHT - PADDLE_HEIGHT;
        }
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ping Pong Game");
        PingPongGame game = new PingPongGame();
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        new Thread(game).start();
    }
}