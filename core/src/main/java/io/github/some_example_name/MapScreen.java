package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class MapScreen implements Screen {
    private Player player;
    private SpriteBatch batch;
    private StretchViewport viewport;
    private Vector2 touchPosition;
    private CellMap[][] map;
    private Rectangle upButtonRectangle,downButtonRectangle,leftButtonRectangle,rightButtonRectangle;
    private Texture upButtonTexture,downButtonTexture,leftButtonTexture, rightButtonTexture;

    public MapScreen(Player player, CellMap[][] map) {
        this.player = player;
        this.map = map;

    }


    @Override
    public void show() {
        batch = new SpriteBatch();
        viewportConfiguration();
        showButtons();
    }

    private void viewportConfiguration(){
        viewport = new StretchViewport(2400, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }

    private void showButtons(){
        upButtonTexture = new Texture(Gdx.files.internal("buttons/upButton.png"));
        downButtonTexture = new Texture(Gdx.files.internal("buttons/downButton.png"));
        leftButtonTexture = new Texture(Gdx.files.internal("buttons/leftButton.png"));
        rightButtonTexture = new Texture(Gdx.files.internal("buttons/rightButton.png"));

        rightButtonRectangle = new Rectangle(viewport.getWorldWidth()-200,0,200,200);
        downButtonRectangle = new Rectangle(rightButtonRectangle.getX()-200,0,200,200);
        leftButtonRectangle = new Rectangle(downButtonRectangle.getX()-200, 0, 200, 200);;
        upButtonRectangle = new Rectangle(rightButtonRectangle.getX()-200,200, 200,200);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        mapDraw();
        playerDraw();
        buttonsDraw();
        batch.end();
        cellAction();
        buttonsInput();


    }
    private  void mapDraw(){
        for (int i = 0; i< map.length;i++){
            for(int j = 0; j<map[i].length;j++){
                if(map[i][j] != null){
                    map[i][j].draw(batch);
                }
            }
        }
    }

    private void playerDraw(){
        player.drawMap(batch,map[player.getCellX()][player.getCellY()].getBounds());
    }

    private void  buttonsDraw(){
        batch.draw(upButtonTexture,upButtonRectangle.getX(),upButtonRectangle.getY());
        batch.draw(downButtonTexture, downButtonRectangle.getX(),downButtonRectangle.getY());
        batch.draw(leftButtonTexture, leftButtonRectangle.getX(),leftButtonRectangle.getY());
        batch.draw(rightButtonTexture,rightButtonRectangle.getX(),rightButtonRectangle.getY());

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);

    }

    @Override
    public void pause() {


    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {


    }
    private void buttonsInput(){
        if(Gdx.input.isTouched()){
            touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition);

            if(rightButtonRectangle.contains(touchPosition.x, touchPosition.y)){
                movePlayer("right");
            }

            if(leftButtonRectangle.contains(touchPosition.x, touchPosition.y)){
                movePlayer("left");
            }

            if(upButtonRectangle.contains(touchPosition.x, touchPosition.y)){
                movePlayer("up");
            }
            if (downButtonRectangle.contains(touchPosition.x, touchPosition.y)){
                movePlayer("down");
            }
        }
    }

    private void  movePlayer(String direction){
        switch (direction){
            case "up":
                if(moveIsPossible(player.getCellX()+1,player.getCellY())){
                    map[player.getCellX()][player.getCellY()].setPlayerIn(false);
                    player.setCellX(player.getCellX()+1);
                    map[player.getCellX()][player.getCellY()].setPlayerIn(true);
                }
                break;
            case "down":
                if(moveIsPossible(player.getCellX()-1,player.getCellY())) {
                    map[player.getCellX()][player.getCellY()].setPlayerIn(false);
                    player.setCellX(player.getCellX()-1);
                    map[player.getCellX()][player.getCellY()].setPlayerIn(true);
                }
                break;
            case "right":
                if(moveIsPossible(player.getCellX(),player.getCellY()+1)) {
                    map[player.getCellX()][player.getCellY()].setPlayerIn(false);
                    player.setCellY(player.getCellY()+1);
                    map[player.getCellX()][player.getCellY()].setPlayerIn(true);
                }
                break;
            case "left":
                if(moveIsPossible(player.getCellX(),player.getCellY()-1)) {
                    map[player.getCellX()][player.getCellY()].setPlayerIn(false);
                    player.setCellY(player.getCellY()-1);
                    map[player.getCellX()][player.getCellY()].setPlayerIn(true);
                }
        }
    }

    private boolean moveIsPossible (int x, int y){
        if(x>=map.length || x<0){
            return false;
        }else if(y>=map[x].length || y<0){
            return false;
        }
        return null != map[x][y];
    }

    private void cellAction(){
        if(map[player.getCellX()][player.getCellY()].isPlayerIn() && map[player.getCellX()][player.getCellY()].isAvailable()){
            map[player.getCellX()][player.getCellY()].action(this);
        }
    }


    public Player getPlayer() {
        return player;
    }

    public CellMap[][] getMap() {
        return map;
    }
}
