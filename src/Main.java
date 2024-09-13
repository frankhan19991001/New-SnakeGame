import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Main extends JPanel implements KeyListener {
    public static final int CELL_SIZE = 20;
    public static final int width = 400;
    public static final int height = 400;
    public static int row = height / CELL_SIZE;
    public static int column = width / CELL_SIZE;
    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private final Integer speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    private int highestScore;

    public Main() {
        read_highest_score();
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
        if (gameOverLogic()) {
            return;
        }

        draw_basic_object_and_environment(g);

        // remove snake tail and put at head
        var newHead = getNewHead();
        drawSnakeAndFruit(g, newHead);

        allowKeyPress = true;
        requestFocusInWindow();
    }

    private Node getNewHead() {
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        switch (direction) {
            case "Left":
                snakeX -= CELL_SIZE;
                break;
            case "Up":
                snakeY -= CELL_SIZE;
                break;
            case "Right":
                snakeX += CELL_SIZE;
                break;
            case "Down":
                snakeY += CELL_SIZE;
                break;
            default:
                break;
        }

        Node newHead = new Node(snakeX, snakeY);
        return newHead;
    }

    private void drawSnakeAndFruit(Graphics g, Node newHead) {
        if (snake.getSnakeBody().get(0).x == fruit.getX()
                && snake.getSnakeBody().get(0).y == fruit.getY()) {
            fruit.setNewLocation(snake);
            fruit.drawFruit(g);
            score++;
        } else {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
        }
        
        snake.getSnakeBody().add(0, newHead);
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

    private void read_highest_score() {
        try {
            File myObj = new File("filename.txt");
            try (Scanner myReader = new Scanner(myObj)) {
                highestScore = myReader.nextInt();
            }
        } catch (FileNotFoundException e) {
            highestScore = 0;
            try {
                File myObj = new File("filename.txt");
                if (myObj.createNewFile()) {
                    System.out.println("File created " + myObj.getName());
                }
                try (FileWriter myWriter = new FileWriter(myObj.getName())) {
                    myWriter.write("" + 0);
                }
            } catch(IOException err) {
                System.out.println("An error occured");
                err.printStackTrace();
            }
        }
    }

    public void write_files(int score) {
        try {
            try (FileWriter myWriter = new FileWriter("filename.txt")) {
                if (score > highestScore) {
                    myWriter.write("" + score);
                    highestScore = score;
                } else {
                    myWriter.write(highestScore);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean gameOverLogic() throws HeadlessException {
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
                write_files(score);
                switch (response) {
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return true;
                }
            }
        }
        return false;
    }

    private void draw_basic_object_and_environment(Graphics g) {
        // black background
        g.fillRect(0, 0, width, height);
        fruit.drawFruit(g);
        snake.drawSnake(g);
        
        // draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Score: " + score, 5, 15);
        g.setColor(Color.YELLOW);
        g.drawString("High Score: " + highestScore, 5, 30);
    }
}