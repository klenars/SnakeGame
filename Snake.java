package com.javarush.games.snake;

import java.util.ArrayList;
import java.util.List;
import com.javarush.engine.cell.*;

public class Snake {

    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    private List<GameObject> snakeParts = new ArrayList<>();
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y){
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }

    public void draw(Game game){
        Color color;
        if (isAlive) {
            color = Color.BLACK;
        }
        else color = Color.RED;

        game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, color, 75);
        for (int i = 1; i < snakeParts.size(); i++){
            game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, color, 75);
        }
    }

    public void setDirection(Direction direction){
        if ((direction.equals(Direction.LEFT) && this.direction.equals(Direction.RIGHT))
            || (direction.equals(Direction.RIGHT) && this.direction.equals(Direction.LEFT))
            || (direction.equals(Direction.UP) && this.direction.equals(Direction.DOWN))
            || (direction.equals(Direction.DOWN) && this.direction.equals(Direction.UP))){
            return;
        }
        else if (((this.direction.equals(Direction.LEFT) || this.direction.equals(Direction.RIGHT)) && snakeParts.get(0).x == snakeParts.get(1).x)
            || ((this.direction.equals(Direction.UP) || this.direction.equals(Direction.DOWN)) && snakeParts.get(0).y == snakeParts.get(1).y)){
            return;
        }
        else this.direction = direction;
    }

    public void move(Apple apple){
        GameObject gameObject = createNewHead();

        if (gameObject.y < 0 || gameObject.y > SnakeGame.HEIGHT - 1 || gameObject.x < 0 || gameObject.x > SnakeGame.WIDTH - 1){
            isAlive = false;
            return;
        }

        if (checkCollision(gameObject)){
            isAlive = false;
            return;
        }
        else snakeParts.add(0, gameObject);

        if (gameObject.x == apple.x && gameObject.y == apple.y){
            apple.isAlive = false;
        }
        else removeTail();

    }

    public GameObject createNewHead(){
        int x = snakeParts.get(0).x;
        int y = snakeParts.get(0).y;

        switch (direction){
            case LEFT:
                return new GameObject(x - 1, y);
            case RIGHT:
                return new GameObject(x + 1, y);
            case DOWN:
                return new GameObject(x, y + 1);
            case UP:
                return new GameObject(x, y - 1);
        }
        return null;
    }

    public void removeTail(){
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject){
        for (GameObject part : snakeParts){
            if (gameObject.x == part.x && gameObject.y == part.y){
                return true;
            }
        }
        return false;
    }

    public int getLength(){
        return snakeParts.size();
    }
}
