import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel implements KeyListener {
    public static final int CELL_SIZE = 20;
    public static int width = 400;
    public static int height = 400;
    public static int row = height / CELL_SIZE;
    public static int column = width / CELL_SIZE;
    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private Integer speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    public Main() {
        reset();
        addKeyListener(this);
    }

    private void reset() {
        score = 0;
        if (snake != null) {
            snake.getSnakeBody().clear();
        }

        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }

    private void setTimer() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, speed);
    }

    @Override
    public void paintComponent(Graphics g) {
        // check game over
        var snake_body = snake.getSnakeBody();
        var head = snake_body.get(0);
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            if (snake_body.get(i).x == head.x && snake_body.get(i).y == head.y) {
                allowKeyPress = false;
                t.cancel();
                t.purge();
                int response = JOptionPane.showOptionDialog(
                        this,
                        "Restart?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        null, // Options to display
                        JOptionPane.YES_OPTION // Default option
                );
                switch (response) {
                    case JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }
        // black background
        g.fillRect(0, 0, width, height);
        fruit.drawFruit(g);
        snake.drawSnake(g);

        // remove snake tail and put at head
        var newHead = getNewHead();

        if (snake.getSnakeBody().get(0).x == fruit.getX()
                && snake.getSnakeBody().get(0).y == fruit.getY()) {
            fruit.setNewLocation(snake);
            fruit.drawFruit(g);
            // score++
        } else {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
        }

        snake.getSnakeBody().add(0, newHead);

        allowKeyPress = true;
        requestFocusInWindow();
    }

    private Node getNewHead() {
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        if(direction.equals("Left")) {
            snakeX -= CELL_SIZE;
        } else if(direction.equals("Up")) {
            snakeY -= CELL_SIZE;
        } else if(direction.equals("Right")) {
            snakeX += CELL_SIZE;
        } else if(direction.equals("Down")) {
            snakeY += CELL_SIZE;
        }

        Node newHead = new Node(snakeX, snakeY);
        return newHead;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (allowKeyPress) {
            if (e.getKeyCode() == 37 && !direction.equals("Right")){
                direction = "Left";
            } else if (e.getKeyCode() == 38 && !direction.equals("Down")){
                direction = "Up";
            } else if (e.getKeyCode() == 39 && !direction.equals("Left")){
                direction = "Right";
            } else if (e.getKeyCode() == 40 && !direction.equals("Up")){
                direction = "Down";
            }
            allowKeyPress = false;
        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}