package coloris;

import java.util.HashMap;
import java.util.Random;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Tile extends Rectangle
{
    
    private static final Color[] COLORS = new Color[] {Color.RED, Color.BLUE, Color.PURPLE, Color.YELLOW, Color.WHITE};
    private static final HashMap<Color, String> SPRITE_MAPPINGS = new HashMap<Color, String>() {{
                                                                                put(Color.RED,    "/coloris/sprites/red.png");
                                                                                put(Color.BLUE,   "/coloris/sprites/blue.png");
                                                                                put(Color.PURPLE, "/coloris/sprites/purple.png");
                                                                                put(Color.YELLOW, "/coloris/sprites/yellow.png");
                                                                                put(Color.WHITE,  "/coloris/sprites/white.png");
                                                                           }};
    
    private static final HashMap<Color, String> DESTROY_MAPPINGS = new HashMap<Color,String>() {{
                                                                                put(Color.RED,    "/coloris/sprites/destroy_red.png");
                                                                                put(Color.BLUE,   "/coloris/sprites/destroy_blue.png");
                                                                                put(Color.PURPLE, "/coloris/sprites/destroy_purple.png");
                                                                                put(Color.YELLOW, "/coloris/sprites/destroy_yellow.png");
                                                                                put(Color.WHITE,  "/coloris/sprites/destroy_white.png");
                                                                           }};

    private static final double FALL_ONE_ROW_IN_MILLIS = 100.0;
            
    private final Color color;
    
    public Tile(double dimension) //treba da dodam i offset, boardWidth i boardHeight
    {
        super(dimension, dimension);
        
        Random random = new Random();
        int index = (int)(random.nextDouble() * COLORS.length);
        color = COLORS[index];
        String path = SPRITE_MAPPINGS.get(color);
        Image sprite = new Image(getClass().getResource(path).toString());
        setFill(new ImagePattern(sprite, 0, 0, 1, 1, true));
    }
    
    public TranslateTransition createTranslation(int noPlaces) 
    {
        TranslateTransition tt = new TranslateTransition();
        tt.setNode(this);
        tt.setDuration(Duration.millis(FALL_ONE_ROW_IN_MILLIS * noPlaces));
        tt.setByY(getWidth() * noPlaces);
        return tt;
    }
    
    public Color getColor() {return color;}
    public boolean sameColor(Tile tile) {return color.equals(tile.getColor());}
    
    public void setDestroySprite()
    {
        String path = DESTROY_MAPPINGS.get(color);
        Image sprite = new Image(getClass().getResource(path).toString());
        setFill(new ImagePattern(sprite, 0, 0, 1, 1, true));
    }
    
    @Override
    public String toString()
    {
        String path = SPRITE_MAPPINGS.get(getColor());
        int start =  path.lastIndexOf('/');
        char c = path.charAt(start + 1);
        return String.valueOf(c).toUpperCase();
    }
}
