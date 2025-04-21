package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.Player;


public class MapScreen implements Screen {
    private final Player player;
    private SpriteBatch batch;
    private StretchViewport viewport;
    private final CellMap[][] map;
    private Rectangle upButtonRectangle,downButtonRectangle,leftButtonRectangle,rightButtonRectangle;
    private Texture upButtonTexture,downButtonTexture,leftButtonTexture, rightButtonTexture;
    private Texture heathBarTexture, healthLineTexture;
    private BitmapFont font;
    private  float elapsedTime;
    private boolean wasTouched;

    public MapScreen(Player player, CellMap[][] map) {
        this.player = player;
        this.map = map;

    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        elapsedTime = 0;
        wasTouched = false;
        viewportConfiguration();
        showButtons();
        showHeathPointBar();
        showFont();
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
        leftButtonRectangle = new Rectangle(downButtonRectangle.getX()-200, 0, 200, 200);
        upButtonRectangle = new Rectangle(rightButtonRectangle.getX()-200,200, 200,200);
    }

    private void showHeathPointBar(){
        heathBarTexture = new Texture(Gdx.files.internal("HUD/health_bar_map.png"));
        healthLineTexture = new Texture(Gdx.files.internal("HUD/health_lane_map.png"));
    }
    private void showFont(){
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(1f);
    }

    @Override
    public void render(float delta) {
        draw(delta);
        logic();
        input();
    }

    private void draw(float delta){
        batch.setProjectionMatrix(viewport.getCamera().combined);
        ScreenUtils.clear(0, 0, 0, 1);
        elapsedTime += delta;
        batch.begin();
        mapDraw(elapsedTime);
        playerDraw();
        buttonsDraw();
        healthBarDraw();
        batch.end();
    }
    private  void mapDraw(float elapsedTime){
        for (CellMap[] cellMaps : map) {
            for (CellMap cellMap : cellMaps) {
                if (cellMap != null) {
                    cellMap.draw(batch,elapsedTime);
                }
            }
        }
    }
    private void playerDraw(){
        player.drawMap(batch,map[player.getCellY()][player.getCellX()].getBounds());
    }

    private void  buttonsDraw(){
        batch.draw(upButtonTexture,upButtonRectangle.getX(),upButtonRectangle.getY());
        batch.draw(downButtonTexture, downButtonRectangle.getX(),downButtonRectangle.getY());
        batch.draw(leftButtonTexture, leftButtonRectangle.getX(),leftButtonRectangle.getY());
        batch.draw(rightButtonTexture,rightButtonRectangle.getX(),rightButtonRectangle.getY());
    }
    private void healthBarDraw(){
        float width = 800;
        float height =width/((float) heathBarTexture.getWidth() /heathBarTexture.getHeight());

        batch.draw(heathBarTexture,viewport.getWorldWidth()-width-100,
            viewport.getWorldHeight()- height, width,height);

        float widthLine = width*((float) healthLineTexture.getWidth() /heathBarTexture.getWidth());
        float heightLine = widthLine/((float) healthLineTexture.getWidth() /healthLineTexture.getHeight());
        float widthLineWithPercentage = widthLine*player.getPercentageOfHealthPoints(); // TODO: упросить эту парашу (как? сделать нормальный спрайт блять)

        batch.draw(healthLineTexture,
            viewport.getWorldWidth()-widthLineWithPercentage-116,
            viewport.getWorldHeight()-heightLine-15,
            widthLineWithPercentage,heightLine);
        font.draw(batch,
            player.getHealth()+"/"+player.getMaxHealth(),
            viewport.getWorldWidth()-widthLine/2-120,
            viewport.getWorldHeight()- heightLine/2 -5);
    }

    private void logic(){
        cellAction();
    }
    private void cellAction(){
        if(map[player.getCellY()][player.getCellX()].isPlayerIn() && map[player.getCellY()][player.getCellX()].isAvailable()){
            map[player.getCellY()][player.getCellX()].action(this);
        }
    }
    private void input(){
        buttonsInput();
    }
    private void buttonsInput(){
        if(Gdx.input.isTouched()&& !wasTouched){
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
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
        wasTouched = Gdx.input.isTouched();
    }

    private void  movePlayer(String direction){
        switch (direction){
            case "down":
                if(moveIsPossible(player.getCellY()+1,player.getCellX())){
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    player.setCellY(player.getCellY()+1);
                    map[player.getCellY()][player.getCellX()].setPlayerIn(true);
                }
                break;
            case "up":
                if(moveIsPossible(player.getCellY()-1,player.getCellX())) {
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    player.setCellY(player.getCellY()-1);
                    map[player.getCellY()][player.getCellX()].setPlayerIn(true);
                }
                break;
            case "right":
                if(moveIsPossible(player.getCellY(),player.getCellX()+1)) {
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    player.setCellX(player.getCellX()+1);
                    map[player.getCellY()][player.getCellX()].setPlayerIn(true);
                }
                break;
            case "left":
                if(moveIsPossible(player.getCellY(),player.getCellX()-1)) {
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    player.setCellX(player.getCellX()-1);
                    map[player.getCellY()][player.getCellX()].setPlayerIn(true);
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

    public Player getPlayer() {
        return player;
    }

    public CellMap[][] getMap() {
        return map;
    }
}
