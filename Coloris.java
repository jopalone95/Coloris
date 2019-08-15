package coloris;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Coloris extends Application //remove all magic numbers!!!!!!!!!!!!!
{

    private static final double INITIAL_SCENE_WIDTH  = 1260;
    private static final double INITIAL_SCENE_HEIGHT = 988.8;
    
    private Group root; 
    private Scene scene;
    private Rectangle titleContainer;
    private Rectangle gameContainer;
    private Board board;
    private AnimationTimer timer;
    private MediaPlayer titlePlayer;
    private MediaPlayer gamePlayer;
    
    @Override
    public void start(Stage stage)
    {   
        root = new Group();
        
        Image titleBackground = new Image(getClass().getResource("/coloris/sprites/title_background.png").toString(), INITIAL_SCENE_WIDTH, INITIAL_SCENE_HEIGHT, false, false);
        titleContainer = new Rectangle(INITIAL_SCENE_WIDTH, INITIAL_SCENE_HEIGHT);
        titleContainer.setFill(new ImagePattern(titleBackground, 0, 0, 1, 1, true));
        
        Image gameBackground = new Image(getClass().getResource("/coloris/sprites/game_background.png").toString(), INITIAL_SCENE_WIDTH, INITIAL_SCENE_HEIGHT, false, false);
        gameContainer = new Rectangle(INITIAL_SCENE_WIDTH, INITIAL_SCENE_HEIGHT);
        gameContainer.setFill(new ImagePattern(gameBackground, 0, 0, 1, 1, true));
        
        
        scene = new Scene(root, INITIAL_SCENE_WIDTH, INITIAL_SCENE_HEIGHT);
        root.getChildren().addAll(gameContainer, titleContainer);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Coloris");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/coloris/sprites/icon.png").toString()));
        stage.show();
        
        setTitleScene();
    }
    
    public void setTitleScene()
    {
        Media sound = new Media(getClass().getResource("/coloris/music/avesoft.mp3").toString());
        titlePlayer = new MediaPlayer(sound);
        titlePlayer.setStartTime(Duration.seconds(5)); //magic number
        titlePlayer.setStopTime(Duration.seconds(11)); //magic number
        
        titlePlayer.setOnEndOfMedia(() -> {
            
            FadeTransition ft = new FadeTransition(Duration.seconds(1), titleContainer);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished((e) -> {
                root.getChildren().remove(titleContainer);
                setGameScene();
            });
            ft.play();
            
        });
        
        titlePlayer.play();
    }
    
    public void setGameScene()
    {
        scene.setOnKeyPressed(k -> {
            board.keyPressed(k.getCode());
        });
        
        GameInfo gameInfo = new GameInfo(scene);
        board = new Board(gameInfo);
        root.getChildren().add(board);
        
        //start playing audio here
        Media sound = new Media(getClass().getResource("/coloris/music/in_gamebgm.mp3").toString());
        gamePlayer = new MediaPlayer(sound);
        gamePlayer.setStopTime(Duration.seconds(176)); //magic number
        gamePlayer.setCycleCount(MediaPlayer.INDEFINITE);
        gamePlayer.play();
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    
    public void update()
    {
        if (board.isGameover() == false)
            board.update();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}
