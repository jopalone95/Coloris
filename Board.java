package coloris;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Board extends Group implements Sprite
{
    
    private final GameInfo gameInfo;
    private final NumberDisplayer scoreDisplayer;
    private final NumberDisplayer lastDisplayer;
    private final NumberDisplayer breakDisplayer;
    private final TileMatrix tileMatrix;
    private Figure moving; 
    private Figure nextMoving;
    private boolean gameover;
    
    public Board(GameInfo gameInfo)
    {
        this.gameInfo = gameInfo;
        gameover = false;
        
        scoreDisplayer = new NumberDisplayer(gameInfo.getScoreNoDigits(), gameInfo.getDigitWidth(), gameInfo.getDigitHeight(), gameInfo.getDigitSpacing());
        scoreDisplayer.setTranslateX(gameInfo.getScoreXCoord());
        scoreDisplayer.setTranslateY(gameInfo.getScoreYCoord());
        
        lastDisplayer = new NumberDisplayer(gameInfo.getLastNoDigits(), gameInfo.getDigitWidth(), gameInfo.getDigitHeight(), gameInfo.getDigitSpacing());
        lastDisplayer.setTranslateX(gameInfo.getLastXCoord());
        lastDisplayer.setTranslateY(gameInfo.getLastYCoord());
        
        breakDisplayer = new NumberDisplayer(gameInfo.getBreakNoDigits(), gameInfo.getDigitWidth(), gameInfo.getDigitHeight(), gameInfo.getDigitSpacing());
        breakDisplayer.setTranslateX(gameInfo.getBreakXCoord());
        breakDisplayer.setTranslateY(gameInfo.getBreakYCoord());
        
        tileMatrix = new TileMatrix(gameInfo, scoreDisplayer);
        
        moving = new Figure(gameInfo.getTileDimension(), gameInfo.getMinVelocity(), gameInfo.getMaxVelocity(), gameInfo.getVelocityIncrement());
        moving.setTranslateX(gameInfo.getMovingXCoord());
        moving.setTranslateY(0);//- moving.getHeight());
        
        nextMoving = new Figure(gameInfo.getTileDimension(), gameInfo.getMinVelocity(), gameInfo.getMaxVelocity(), gameInfo.getVelocityIncrement());
        nextMoving.setTranslateX(gameInfo.getNextXCoord());
        nextMoving.setTranslateY(gameInfo.getNextYCoord());
        
        getChildren().add(scoreDisplayer);
        getChildren().add(lastDisplayer);
        getChildren().add(breakDisplayer);
        getChildren().addAll(moving.getTiles());
        getChildren().addAll(nextMoving.getTiles());
    }
  
    public void keyPressed(KeyCode code)
    {
        if (moving != null && moving.isAwake() == true)
        {
            if (Figure.MOVES_LEFT.contains(code))
                moving.moveLeft();
            else if (Figure.MOVES_RIGHT.contains(code))
                moving.moveRight();
            else if (Figure.SLIDES_UP.contains(code))
                moving.slideUp();
            else if (Figure.INCREMENTS_VELOCITY.contains(code))
                moving.incrementVelocity();
            else if (Figure.DECREMENTS_VELOCITY.contains(code))
                moving.decrementVelocity();
        }
    }
    
    @Override
    public void update()
    {
        if (moving != null && moving.isAwake() == true) 
        {
            moving.update();
        }
        else if (moving != null)
        {
            tileMatrix.addToColumn(moving.getColumn(), moving.getTiles());
            moving = null;
            tileMatrix.update();
        }
        else if (tileMatrix.isUpdateInProgress() == false)
        {   
            if (tileMatrix.isGameover() == true)
            {
                setGameover();
            }
            else
            {
                moving = nextMoving;
                moving.setTranslateX(gameInfo.getMovingXCoord());
                moving.setTranslateY(0);//- moving.getHeight());

                nextMoving = new Figure(gameInfo.getTileDimension(), gameInfo.getMinVelocity(), gameInfo.getMaxVelocity(), gameInfo.getVelocityIncrement());
                nextMoving.setTranslateX(gameInfo.getNextXCoord());
                nextMoving.setTranslateY(gameInfo.getNextYCoord());

                getChildren().addAll(nextMoving.getTiles());
            }
        }
    }
    
    public double getFreeSpaceInColumn(int col)
    {
        return gameInfo.getBoardHeight() - tileMatrix.getNoTiles(col) * moving.getWidth();
    }

    public boolean isGameover() {return gameover;}
    private void setGameover()
    {
        gameover = true;
        
        Rectangle gameoverText = new Rectangle(gameInfo.getGameoverXCoord(), gameInfo.getGameoverYCoord(), gameInfo.getGameoverWidth(), gameInfo.getGameoverHeight());
        Image image = new Image(getClass().getResource("/coloris/sprites/gameover.png").toString());
        gameoverText.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
        gameoverText.setOpacity(0.0);
        gameoverText.setStroke(gameInfo.getBoardBackgroundColor());
        
        FadeTransition ft = new FadeTransition(Duration.millis(2000), gameoverText);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setOnFinished((e) -> {
            //stvori prozor sa buttonom koji omogucava
        });
        ft.play();
        
        getChildren().add(gameoverText);
    }
}
