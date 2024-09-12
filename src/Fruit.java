import java.awt.*;

public class Fruit {
    private int x;
    private int y;

    public Fruit(){
        this.x = (int) Math.floor(Math.random() * Main.column) * Main.CELL_SIZE;
        this.y = (int) Math.floor(Math.random() * Main.row) * Main.CELL_SIZE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void drawFruit(Graphics g) {
        g.setColor(Color.pink);
        g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE);
    }

    public void setNewLocation(Snake s) {
        int new_x;
        int new_y;
        boolean overlapping;

        do{
            new_x = (int) Math.floor(Math.random() * Main.column) * Main.CELL_SIZE;
            new_y = (int) Math.floor(Math.random() * Main.row) * Main.CELL_SIZE;
            overlapping = check_overlap(new_x, new_y, s);
        } while (overlapping);

        this.x = new_x;
        this.y = new_y;
    }

    private boolean check_overlap(int x, int y, Snake s) {
        for (int i = 0; i < s.getSnakeBody().size(); i++) {
            var snake_body_x = s.getSnakeBody().get(i).x;
            var snake_body_y = s.getSnakeBody().get(i).y;

            if (x == snake_body_x && y == snake_body_y){
                return true;
            }
        }
        return false;
    }
}
