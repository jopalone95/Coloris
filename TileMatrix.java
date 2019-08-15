package coloris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;

public class TileMatrix implements Sprite
{
    
    private static final double SHOW_DESTROY_SPRITE_IN_MILLIS = 200.0;
    
    private final GameInfo gameInfo;
    private final int rowCount;
    private final int colCount;
    private final int[] noTiles; 
    private final Tile[][] matrix;
    boolean updateInProgress;
    private final ArrayList<Tile> toDestroy;
    private int previousScore;
    private int score;
    private final NumberDisplayer scoreDisplayer;
    private int columnLastAddedTo;
    
    public TileMatrix(GameInfo gameInfo, NumberDisplayer scoreDisplayer)
    {
        this.gameInfo = gameInfo;
        this.rowCount = gameInfo.getRowCount();
        this.colCount = gameInfo.getColCount();
        noTiles = new int[colCount];
        matrix  = new Tile[rowCount][colCount];
        updateInProgress = false;
        toDestroy = new ArrayList<>();
        previousScore = 0;
        score = 0;
        this.scoreDisplayer = scoreDisplayer;
        columnLastAddedTo = -1;
    }
    
    @Override
    public void update()
    {
        setUpdateInProgress(true);
        
        updateMatrix();
    }
    
    private void updateMatrix()
    {
        boolean isToRemove = false;
        HashMap<Tile, Pair> toBeRemoved = new HashMap<>();
            
        for(int i = 0; i < getRowCount(); i++) //promenio
        {
            for(int j = 0; j < getColCount(); j++)
            {
                if (matrix[i][j] != null)
                {
                    boolean isToRemoveV = checkForSameColorNeighbours(i, j, 'v', toBeRemoved);
                    boolean isToRemoveH = checkForSameColorNeighbours(i, j, 'h', toBeRemoved);
                    isToRemove = isToRemove || isToRemoveV || isToRemoveH; 
                }
            }
        }
            
        if (isToRemove == true)
        {
            setToNull(toBeRemoved);
            //najobicniji nacin da se pozove funkcija destroy sa delay-om, da bi tile-ovi zadrzali destroy sprite 0.2 sekunde,
            //mozda postoji bolji nacin, razmisli o tome
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(SHOW_DESTROY_SPRITE_IN_MILLIS), e -> destroy())
            );
            timeline.play();
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        else
        {
            previousScore = score;
            setUpdateInProgress(false);
        }
    }
    
    private void setToNull(HashMap<Tile, Pair> toBeDestroyed) //setuje sprite-ove za unistenje i vrednosti tih tile-ova u matrici na null
    {
        for(Map.Entry<Tile, Pair> entry : toBeDestroyed.entrySet())
        {
            Pair indices = entry.getValue();
            int row = indices.getRow();
            int col = indices.getCol();
            toDestroy.add(matrix[row][col]);
            
            matrix[row][col].setDestroySprite();  
            matrix[row][col].setWidth(matrix[row][col].getWidth() + 1); //mnogo dobra forica
            matrix[row][col].setHeight(matrix[row][col].getHeight() + 1); //mnogo dobra forica
            matrix[row][col] = null;      
        }
    }
    
    private void destroy()
    {
        score += toDestroy.size();
        if (score > scoreDisplayer.getMaxNumber())
            score -= scoreDisplayer.getMaxNumber();
        
        Group group = (Group)toDestroy.get(0).getParent();
        group.getChildren().removeAll(toDestroy);
        toDestroy.clear();
        
        scoreDisplayer.setNumber(score);
        scoreDisplayer.update();
        
        rearrange();
    }
    
    private void rearrange()
    {
        ParallelTransition pt = new ParallelTransition();
        pt.setOnFinished(e -> { //u slucaju da je unisteno >= 5 onda treba da se unisti poslednji red, pa tek onda da se pozove update matrix
            if (score - previousScore >= 5)
                destroyLastRow();
            else
                updateMatrix();
        });
        
        for(int i = 0; i < getColCount(); i++) //istestiraj ovaj deo algoritma
        {
            int j = 0;
            while(j < noTiles[i] && matrix[j][i] != null)
                j++;
            int k = j;
            while(k < noTiles[i] && matrix[k][i] == null)
                k++;
            
            while(k < noTiles[i])
            {
                if (matrix[k][i] != null)
                {
                    matrix[j][i] = matrix[k][i];
                    matrix[k][i] = null;
                    pt.getChildren().add(matrix[j][i].createTranslation(k - j)); 
                    j++;
                }
                k++;
            }
            
            noTiles[i] = j;
        }
        
        pt.play();
    }
    
    private boolean checkForSameColorNeighbours(int row, int col, char direction, HashMap<Tile, Pair> toBeRemoved) //direction can be vertical ('v') or horizontal ('h')
    {
        boolean removed = false;
        Tile tile     = matrix[row][col];
        int k         = (direction == 'v' ? row + 1  : col + 1);
        int limit     = (direction == 'v' ? getRowCount() : getColCount());
        Tile nextTile = null;
        if (k < limit)
            nextTile = (direction == 'v' ? matrix[k][col] : matrix[row][k]);
        
        while(k < limit && nextTile != null &&  nextTile.sameColor(tile))
        {
            k++;
            if (k < limit)
                nextTile = (direction == 'v' ? matrix[k][col] : matrix[row][k]);
        }
        
        int noSameColorTiles = (direction == 'v' ? k - row : k  - col);
        
        if (noSameColorTiles >= 3) //treba najmanje tri da se skupe da bi doslo do unistenja
        {
            int n = (direction == 'v' ? row : col);
            while(n < k)
            {
                Tile currentTile = (direction == 'v' ? matrix[n][col] : matrix[row][n]);
                if (toBeRemoved.containsKey(currentTile) == false)
                {
                    Pair indices = (direction == 'v' ? new Pair(n, col) : new Pair(row, n));
                    toBeRemoved.put(currentTile, indices);
                }
                n++;
            }
            
            removed = true;
        }
        
        return removed;
    }
    
    private void destroyLastRow()
    {
        SequentialTransition sq = new SequentialTransition();
        
        for (int i = 0; i < getColCount(); i++)
        {
            boolean somethingToDestroy = false;
            int j = 0;

            if (matrix[j][i] != null)
            {
                somethingToDestroy = true;
                toDestroy.add(matrix[j][i]);
            }
            
            ParallelTransition pt = new ParallelTransition();
            
            while (j < noTiles[i] - 1)
            {
                pt.getChildren().add(matrix[j + 1][i].createTranslation(1));
                matrix[j][i] = matrix[j + 1][i];
                j++;
            }
            
            if (noTiles[i] > 0)
            {
                matrix[j][i] = null;
                noTiles[i]--;
            }
            
            if (somethingToDestroy == true)
            {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.millis(0.0), 
                        (e) -> {
                            toDestroy.get(0).setDestroySprite();
                            toDestroy.get(0).setWidth(toDestroy.get(0).getWidth() + 1);
                            toDestroy.get(0).setHeight(toDestroy.get(0).getHeight() + 1);
                        }),
                        
                        new KeyFrame(Duration.millis(SHOW_DESTROY_SPRITE_IN_MILLIS),
                        (e) -> {
                            Group parent = (Group)toDestroy.get(0).getParent();
                            parent.getChildren().remove(toDestroy.get(0));
                            toDestroy.remove(0);
                            
                            score++;
                            scoreDisplayer.setNumber(score);
                            scoreDisplayer.update();
                            
                            if (pt.getChildren().size() > 0)
                                pt.play();
                        }),
                        
                        new KeyFrame(pt.getCycleDuration())
                );
                sq.getChildren().add(timeline);
            }
        }
        
        sq.setOnFinished((e) -> {
            previousScore = score;
            updateMatrix();
        });
        sq.play();
    }
    
    public void addToColumn(int col, ArrayList<Tile> tiles)
    {
        columnLastAddedTo = col;
        
        for(int row = 0; row < getRowCount(); row++)
        {
            if (matrix[row][col] == null)
            {
                for(int j = 0; j < tiles.size(); j++)
                {
                    matrix[row + j][col] = tiles.get(tiles.size() - j - 1);
                }
                noTiles[col] += tiles.size();
                
                return;
            }
        }
    }
    
    public void print()
    {
        for(int i = getRowCount() - 1; i >= 0; i--)
        {
            for(int j = 0; j < getColCount(); j++)
            {
                if (matrix[i][j] != null)
                    System.out.print(matrix[i][j] + " ");
                else
                    System.out.print("O ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public int getRowCount() {return rowCount;}
 
    public int getColCount() {return colCount;}
    
    public int getNoTiles(int col) {return noTiles[col];}

    public int getScore() {return score;}
    
    public boolean isUpdateInProgress() {return updateInProgress;}
    public void setUpdateInProgress(boolean updateInProgress) {this.updateInProgress = updateInProgress;}
    
    public boolean isGameover() {return noTiles[columnLastAddedTo] > gameInfo.getMaxColHeight();}
}
