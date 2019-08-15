package coloris;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class GameInfo //da li da napravim ovo kao singleton???
{
    
    private static final int ROW_COUNT      = 17;
    private static final int COL_COUNT      = 7;
    private static final int MAX_COL_HEIGHT = 14;
    
    private static final double PADDING = 2.5; //px
    
    private static final double BOARD_WIDTH_SCENE_WIDTH_RATIO        = 275.0 / 1050.0; 
    private static final double BOARD_HEIGHT_SCENE_HEIGHT_RATIO      = 775.0 / 824.0;
    private static final double BOARD_WIDTH_MIDDLE_SCENE_WIDTH_RATIO = (200.0 + 475.0) / (2.0 * 1050.0);
    
    private static final double NEXT_FIGURE_X_COORD_SCENE_WIDTH_RATIO  = 62.0  / 1050.0;
    private static final double NEXT_FIGURE_Y_COORD_SCENE_HEIGHT_RATIO = 103.0 / 824.0;
    
    private static final double MIN_VELOCITY_SCENE_HEIGHT_RATIO       = 3.0  / 824.0;
    private static final double MAX_VELOCITY_SCENE_HEIGHT_RATIO       = 15.0 / 824.0;
    private static final double VELOCITY_INCREMENT_SCENE_HEIGHT_RATIO = 2.0  / 824.0;
    
    private static final double GAMEOVER_WIDTH_HEIGHT_RATIO         = 1123.0 / 204.0;
    private static final double GAMEOVER_X_COORD_SCENE_WIDTH_RATIO  = 212.0  / 1050.0;
    private static final double GAMEOVER_Y_COORD_SCENE_HEIGHT_RATIO = 50.0   / 824.0;
    private static final double GAMEOVER_WIDTH_BOARD_WIDTH_RATIO    = 251.0  / 275.0;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private static final double DIGIT_WIDTH_HEIGHT_RATIO         = 352.0 / 489.0;
    private static final double DIGIT_WIDTH_SCENE_WIDTH_RATIO    = 42.0  / 1050.0;
    private static final double DIGIT_WIDTH_SPACING_RATIO        = 42.0  / 12.0;
    
    private static final int    SCORE_NO_DIGITS                  = 6;
    private static final double SCORE_X_COORD_SCENE_WIDTH_RATIO  = 629.0 / 1050.0;
    private static final double SCORE_Y_COORD_SCENE_HEIGHT_RATIO = 58.0  / 824.0;
    
    private static final int    LAST_NO_DIGITS                  = 6;
    private static final double LAST_X_COORD_SCENE_WIDTH_RATIO  = 629.0 / 1050.0;
    private static final double LAST_Y_COORD_SCENE_HEIGHT_RATIO = 357.0 / 824.0;
    
    private static final int    BREAK_NO_DIGITS                  = 1;
    private static final double BREAK_X_COORD_SCENE_WIDTH_RATIO  = 697.0 / 1050.0;
    private static final double BREAK_Y_COORD_SCENE_HEIGHT_RATIO = 662.0 / 824.0;
    
    private final Scene scene;
    
    public GameInfo(Scene scene)
    {
        this.scene = scene;
    }
    
    public double getBoardWidth()  {return scene.getWidth() * BOARD_WIDTH_SCENE_WIDTH_RATIO - PADDING * 2.0;} //mnozi se sa dva da bi padding levo i desno bio isti kao s donje strane 
    public double getBoardHeight() {return scene.getHeight() * BOARD_HEIGHT_SCENE_HEIGHT_RATIO - PADDING;}
    
    public int getRowCount()     {return ROW_COUNT;}
    public int getColCount()     {return COL_COUNT;}
    public int getMaxColHeight() {return MAX_COL_HEIGHT;}
    
    public double getTileDimension() {return getBoardWidth() / COL_COUNT;}
    
    public double getMovingXCoord() {return scene.getWidth() * BOARD_WIDTH_MIDDLE_SCENE_WIDTH_RATIO - getTileDimension() / 2.0;} //deli se sa dva da bi se nacentriralo
    
    public double getNextXCoord() {return scene.getWidth()  * NEXT_FIGURE_X_COORD_SCENE_WIDTH_RATIO;}
    public double getNextYCoord() {return scene.getHeight() * NEXT_FIGURE_Y_COORD_SCENE_HEIGHT_RATIO;}
    
    public double getMinVelocity()       {return scene.getHeight() * MIN_VELOCITY_SCENE_HEIGHT_RATIO;}
    public double getMaxVelocity()       {return scene.getHeight() * MAX_VELOCITY_SCENE_HEIGHT_RATIO;}
    public double getVelocityIncrement() {return scene.getHeight() * VELOCITY_INCREMENT_SCENE_HEIGHT_RATIO;}
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public double getGameoverXCoord() {return scene.getWidth()   * GAMEOVER_X_COORD_SCENE_WIDTH_RATIO;}
    public double getGameoverYCoord() {return scene.getHeight()  * GAMEOVER_Y_COORD_SCENE_HEIGHT_RATIO;}
    public double getGameoverWidth()  {return getBoardWidth()    * GAMEOVER_WIDTH_BOARD_WIDTH_RATIO;}
    public double getGameoverHeight() {return getGameoverWidth() / GAMEOVER_WIDTH_HEIGHT_RATIO;}
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public double getDigitWidth()   {return scene.getWidth()  * DIGIT_WIDTH_SCENE_WIDTH_RATIO;}
    public double getDigitHeight()  {return getDigitWidth()   / DIGIT_WIDTH_HEIGHT_RATIO;}
    public double getDigitSpacing() {return getDigitWidth()   / DIGIT_WIDTH_SPACING_RATIO;}
    
    public int    getScoreNoDigits() {return SCORE_NO_DIGITS;}
    public double getScoreXCoord()   {return scene.getWidth()  * SCORE_X_COORD_SCENE_WIDTH_RATIO;}
    public double getScoreYCoord()   {return scene.getHeight() * SCORE_Y_COORD_SCENE_HEIGHT_RATIO;}
    
    public int    getLastNoDigits() {return LAST_NO_DIGITS;}
    public double getLastXCoord()   {return scene.getWidth()  * LAST_X_COORD_SCENE_WIDTH_RATIO;}
    public double getLastYCoord()   {return scene.getHeight() * LAST_Y_COORD_SCENE_HEIGHT_RATIO;}
    
    public int    getBreakNoDigits() {return BREAK_NO_DIGITS;}
    public double getBreakXCoord()   {return scene.getWidth()  * BREAK_X_COORD_SCENE_WIDTH_RATIO;}
    public double getBreakYCoord()   {return scene.getHeight() * BREAK_Y_COORD_SCENE_HEIGHT_RATIO;}
    
    public Color getBoardBackgroundColor() {return Color.web("0x000077");}
}
