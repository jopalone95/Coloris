package coloris;

import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class NumberDisplayer extends HBox implements Sprite
{
    
    private static final Image[] digitsImages = new Image[] {new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/0.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/1.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/2.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/3.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/4.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/5.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/6.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/7.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/8.png").toString()),
                                                             new Image(NumberDisplayer.class.getClassLoader().getClass().getResource("/coloris/sprites/digits/9.png").toString())};
                                                             
    private int number;
    private final Rectangle[] digits;
    
    public NumberDisplayer(int noDigits, double digitWidth, double digitHeight, double spacing) //treba u konstruktoru da ubacim dimenzije
    {
        super(spacing);
        
        this.number = 0;
        this.digits = new Rectangle[noDigits];
        
        for(int i = 0; i < digits.length; i++)
        {
            digits[i] = new Rectangle(digitWidth, digitHeight); //postavi dimenzije nubaro 
            digits[i].setFill(new ImagePattern(digitsImages[0], 0, 0, 1, 1, true));
        }
        
        getChildren().addAll(digits);
    }
    
    @Override
    public void update() 
    {
        StringBuilder number = new StringBuilder(Integer.toString(this.number));
        
        while(number.length() < digits.length)
            number.insert(0, "0");
        
        for(int i = 0; i < digits.length; i++)
        {
            int index = number.charAt(i) - '0';
            digits[i].setFill(new ImagePattern(digitsImages[index], 0, 0, 1, 1, true));
        }
    }

    public int getNumber() {return number;}
    public void setNumber(int number) {this.number = number;}
    
    public int getNoDigits() {return digits.length;}
    public int getMaxNumber() {return (int)Math.pow(10, digits.length) - 1;}
}
