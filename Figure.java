package coloris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.input.KeyCode;

public class Figure implements Sprite
{
    
    public static final List<KeyCode> MOVES_LEFT          = Arrays.asList(new KeyCode[] {KeyCode.A,     KeyCode.LEFT,  KeyCode.KP_LEFT});
    public static final List<KeyCode> MOVES_RIGHT         = Arrays.asList(new KeyCode[] {KeyCode.D,     KeyCode.RIGHT, KeyCode.KP_RIGHT}); 
    public static final List<KeyCode> INCREMENTS_VELOCITY = Arrays.asList(new KeyCode[] {KeyCode.S,     KeyCode.DOWN,  KeyCode.KP_DOWN});
    public static final List<KeyCode> DECREMENTS_VELOCITY = Arrays.asList(new KeyCode[] {KeyCode.W,     KeyCode.UP,    KeyCode.KP_UP});
    public static final List<KeyCode> SLIDES_UP           = Arrays.asList(new KeyCode[] {KeyCode.SPACE, KeyCode.ENTER});
    
    private static final int NO_TILES = 3;
    private static final int INITIAL_COLUMN = 3;
    private static final int NO_COLUMNS = 7;
    
    private final double  width;
    private final double  height;
    private int column;
    private boolean awake;
    private double velocity;
    private double minVelocity;
    private double maxVelocity;
    private double velocityIncrement;
    private final ArrayList<Tile> tiles;
    
    public Figure(double dimension, double minVelocity, double maxVelocity, double velocityIncrement) //namesti u konstruktoru adekvatne y koordinate za tile-ove
    {
        width = dimension;
        height = dimension * NO_TILES;
        column = INITIAL_COLUMN;
        awake = true;
        velocity = minVelocity;
        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
        this.velocityIncrement = velocityIncrement;
        
        tiles  = new ArrayList<>();
        for(int i = 0; i < NO_TILES; i++)
        {
            Tile tile = new Tile(dimension);
            tile.setTranslateY(i * width);
            tiles.add(tile);
        }
    }
    
    public void slideUp()
    {
        double temp  = tiles.get(0).getTranslateY();
        double temp1 = tiles.get(1).getTranslateY();
        tiles.get(1).setTranslateY(temp);
        tiles.get(0).setTranslateY(tiles.get(2).getTranslateY());
        tiles.get(2).setTranslateY(temp1);
        
        Tile bottomNext = tiles.get(0);
        tiles.remove(0);
        tiles.add(bottomNext);
    }
    
    public void moveLeft()
    {
        Board board = (Board)tiles.get(0).getParent();
        
        if(column > 0 && getTranslateY() + getHeight() < board.getFreeSpaceInColumn(column - 1))
        {
            for(Tile tile : tiles)
                tile.setTranslateX(tile.getTranslateX() - getWidth());
            column--;
        }
    }
    
    public void moveRight()
    {
        Board board = (Board)tiles.get(0).getParent();
        
        if (column < NO_COLUMNS - 1 && getTranslateY() + getHeight() < board.getFreeSpaceInColumn(column + 1))
        {
            for(Tile tile : tiles)
                tile.setTranslateX(tile.getTranslateX() + getWidth());
            column++;
        }
    }

    public void incrementVelocity() {if (velocity < maxVelocity) velocity += velocityIncrement;}
    
    public void decrementVelocity() {if (velocity > minVelocity) velocity -= velocityIncrement;}
    
    @Override
    public void update()
    {
        Board board = (Board)tiles.get(0).getParent();
       
        if (getTranslateY() + getHeight() + velocity < board.getFreeSpaceInColumn(column))
        {
            setTranslateY(getTranslateY() + velocity);
        }
        else
        {
            setTranslateY(board.getFreeSpaceInColumn(column) - getHeight());
            awake = false;
        }
    }

    public int getColumn() {return column;}

    public ArrayList<Tile> getTiles() {return tiles;}

    public double getWidth() {return width;}
    public double getHeight() {return height;}
    
    public void setTranslateX(double x)
    {
        for(Tile tile : tiles)
        {
            tile.setTranslateX(x);
        }
    }
    public double getTranslateX() {return tiles.get(0).getTranslateX();}
    
    public void setTranslateY(double y)
    {
        for(int i = 0; i < NO_TILES; i++)
        {
            Tile tile = tiles.get(i);
            tile.setTranslateY(y + i * width);
        }
    }
    public double getTranslateY() {return tiles.get(0).getTranslateY();}

    public boolean isAwake() {return awake;}
}