package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.some_example_name.Main;
import io.github.some_example_name.SettingsGame;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.cell_map_classes.cell_maps.EventCell;
import io.github.some_example_name.cell_map_classes.cell_maps.FightCell;
import io.github.some_example_name.player.Player;


public class MapScreen implements Screen, DrawableScreen {
    private static final short DIRECTION_UP = 0, DIRECTION_RIGHT = 1, DIRECTION_DOWN = 2, DIRECTION_LEFT = 3;
    private static final float CHARACTER_SPEED = 300f;
    private static final float SIZE_MOVEMENT_BUTTONS = 200;
    private static final float SCALE_BUTTON_DECK_DEMONSTRATION = 0.2F;
    private final Player player;
    private final CellMap[][] map;
    public boolean playerIsMoving;
    private Stage stage;
    private SpriteBatch batch;
    private StretchViewport viewport;
    private Texture heathBarTexture, healthLineTexture;
    private Texture background;
    private Music backgroundMusic;
    private BitmapFont font;
    private float elapsedTime;
    private short characterDirection;

    public MapScreen(Player player, CellMap[][] map) {
        this.player = player;
        this.map = map;

    }

    @Override
    public StretchViewport getViewport() {
        return viewport;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        elapsedTime = 0;
        playerIsMoving = false;
        viewportConfiguration();
        stage = new Stage(viewport, batch);
        showHeathPointBar();
        showDeckDemonstrationButton();
        showFont();
        showBackground();
        showButtons();
        Gdx.input.setInputProcessor(stage);
        if (backgroundMusic == null || !backgroundMusic.isPlaying()) {
            showMusic();
        }
    }

    private void showBackground() {
        background = new Texture(Gdx.files.internal("backgrounds/background_map.jpg"));
    }

    private void viewportConfiguration() {
        viewport = new StretchViewport(SettingsGame.SCREEN_WIDTH, SettingsGame.SCREEN_HEIGHT);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }

    private void showButtons() {
        Texture rightButtonTexture = new Texture(Gdx.files.internal("buttons/right_button.png"));
        ImageButton rightButton = new ImageButton(new TextureRegionDrawable(rightButtonTexture));
        rightButton.setSize(SIZE_MOVEMENT_BUTTONS, SIZE_MOVEMENT_BUTTONS);
        rightButton.setPosition(viewport.getWorldWidth() - SIZE_MOVEMENT_BUTTONS, 0);
        rightButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!playerIsMoving) {
                    movePlayer("right");
                    player.changeSight("right");
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(rightButton);


        Texture downButtonTexture = new Texture(Gdx.files.internal("buttons/down_button.png"));
        ImageButton downButton = new ImageButton(new TextureRegionDrawable(downButtonTexture));
        downButton.setSize(SIZE_MOVEMENT_BUTTONS, SIZE_MOVEMENT_BUTTONS);
        downButton.setPosition(rightButton.getX() - SIZE_MOVEMENT_BUTTONS, 0);
        downButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!playerIsMoving) {
                    movePlayer("down");
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(downButton);

        Texture leftButtonTexture = new Texture(Gdx.files.internal("buttons/left_button.png"));
        ImageButton leftButton = new ImageButton(new TextureRegionDrawable(leftButtonTexture));
        leftButton.setSize(SIZE_MOVEMENT_BUTTONS, SIZE_MOVEMENT_BUTTONS);
        leftButton.setPosition(downButton.getX() - SIZE_MOVEMENT_BUTTONS, 0);
        leftButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!playerIsMoving) {
                    movePlayer("left");
                    player.changeSight("left");
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(leftButton);

        Texture upButtonTexture = new Texture(Gdx.files.internal("buttons/up_button.png"));
        ImageButton upButton = new ImageButton(new TextureRegionDrawable(upButtonTexture));
        upButton.setSize(SIZE_MOVEMENT_BUTTONS, SIZE_MOVEMENT_BUTTONS);
        upButton.setPosition(downButton.getX(), SIZE_MOVEMENT_BUTTONS);
        upButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!playerIsMoving) {
                    movePlayer("up");
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(upButton);
    }

    private void showHeathPointBar() {
        heathBarTexture = new Texture(Gdx.files.internal("HUD/health_bar_map.png"));
        healthLineTexture = new Texture(Gdx.files.internal("HUD/health_lane_map.png"));
    }

    private void showDeckDemonstrationButton() {
        Texture deckButtonDemonstrationTexture = new Texture(Gdx.files.internal("map_screen/deck.png"));
        ImageButton buttonDeckDemonstration = new ImageButton(new TextureRegionDrawable(deckButtonDemonstrationTexture));
        MapScreen mapScreen = this;
        buttonDeckDemonstration.setPosition(viewport.getWorldWidth() - 200, viewport.getWorldHeight() - 600);
        buttonDeckDemonstration.setSize(deckButtonDemonstrationTexture.getWidth() * SCALE_BUTTON_DECK_DEMONSTRATION, deckButtonDemonstrationTexture.getHeight() * SCALE_BUTTON_DECK_DEMONSTRATION);
        buttonDeckDemonstration.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(new ShowDeckScreen(player.getDeck(), mapScreen));
                super.clicked(event, x, y);
            }
        });
        stage.addActor(buttonDeckDemonstration);


    }

    private void showFont() {
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(1f);
    }

    private void showMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/hub_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f);
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        draw(delta);
        logic(delta);
        input();
    }

    @Override
    public void draw(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        ScreenUtils.clear(0, 0, 0, 1);
        elapsedTime += delta;
        stage.draw();
        batch.begin();
        backgroundDraw();
        mapDraw(elapsedTime);

        playerDraw(delta);
        healthBarDraw();
        scoreDraw();
        batch.end();
        stage.draw();
    }


    private void backgroundDraw() {
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getScreenHeight());
    }

    private void mapDraw(float elapsedTime) {
        for (CellMap[] cellMaps : map) {
            for (CellMap cellMap : cellMaps) {
                if (cellMap != null) {
                    cellMap.draw(batch, elapsedTime);
                }
            }
        }
    }


    private void playerDraw(float delta) {
        moveCharacter(delta);
        player.drawMap(batch);
    }

    private void moveCharacter(float delta) {
        if (playerIsMoving) {
            float yArrivalPoint;
            float xArrivalPoint;
            switch (characterDirection) {
                case DIRECTION_UP:
                    player.setyOnScreen(player.getyOnScreen() + delta * CHARACTER_SPEED);
                    yArrivalPoint = map[player.getCellY() - 1][player.getCellX()].getBounds().getY() + 15;
                    if (player.getyOnScreen() >= yArrivalPoint) {
                        playerIsMoving = false;
                        player.setCellY(player.getCellY() - 1);
                        map[player.getCellY()][player.getCellX()].setPlayerIn(true);
                    }
                    break;
                case DIRECTION_RIGHT:
                    player.setxOnScreen(player.getXOnScreen() + delta * CHARACTER_SPEED);
                    xArrivalPoint = map[player.getCellY()][player.getCellX() + 1].getBounds().getX() + map[player.getCellY()][player.getCellX() + 1].getBounds().getWidth() / 2 - player.getWidth() / 2;
                    if (player.getXOnScreen() >= xArrivalPoint) {
                        playerIsMoving = false;
                        player.setCellX(player.getCellX() + 1);
                        map[player.getCellY()][player.getCellX()].setPlayerIn(true);
                    }
                    break;
                case DIRECTION_DOWN:
                    player.setyOnScreen(player.getyOnScreen() - delta * CHARACTER_SPEED);
                    yArrivalPoint = map[player.getCellY() + 1][player.getCellX()].getBounds().getY() + 15;
                    if (player.getyOnScreen() <= yArrivalPoint) {
                        playerIsMoving = false;
                        player.setCellY(player.getCellY() + 1);
                        map[player.getCellY()][player.getCellX()].setPlayerIn(true);
                    }
                    break;
                case DIRECTION_LEFT:
                    player.setxOnScreen(player.getXOnScreen() - delta * CHARACTER_SPEED);
                    xArrivalPoint = map[player.getCellY()][player.getCellX() - 1].getBounds().getX() + map[player.getCellY()][player.getCellX() - 1].getBounds().getWidth() / 2 - player.getWidth() / 2;
                    if (player.getXOnScreen() <= xArrivalPoint) {
                        playerIsMoving = false;
                        player.setCellX(player.getCellX() - 1);
                        map[player.getCellY()][player.getCellX()].setPlayerIn(true);
                    }
                    break;
            }
        }
        walkAnimation(delta);
    }

    private void walkAnimation(float delta) {
        if (playerIsMoving || !player.inStatic()) {
            player.walkAnimation(delta);
        }
        if (playerIsMoving || !player.notRotated()) {
            player.rotateAnimation(delta);
        }

    }


    private void healthBarDraw() {
        float width = 800;
        float height = width / ((float) heathBarTexture.getWidth() / heathBarTexture.getHeight());

        batch.draw(heathBarTexture, viewport.getWorldWidth() - width - 100,
            viewport.getWorldHeight() - height, width, height);

        float widthLine = width * ((float) healthLineTexture.getWidth() / heathBarTexture.getWidth());
        float heightLine = widthLine / ((float) healthLineTexture.getWidth() / healthLineTexture.getHeight());
        float widthLineWithPercentage = widthLine * player.getPercentageOfHealthPoints(); // TODO: изменить эту парашу (как? сделать нормальный спрайт)

        batch.draw(healthLineTexture,
            viewport.getWorldWidth() - widthLineWithPercentage - 116,
            viewport.getWorldHeight() - heightLine - 15,
            widthLineWithPercentage, heightLine);
        font.draw(batch,
            player.getHealth() + "/" + player.getMaxHealth(),
            viewport.getWorldWidth() - widthLine / 2 - 120,
            viewport.getWorldHeight() - heightLine / 2 - 5);
    }

    private void scoreDraw() {
        font.draw(batch, "Score:" + player.getScore(), viewport.getWorldWidth() - 800, viewport.getWorldHeight() - 100);

    }

    private void logic(float delta) {
        stage.act(delta);
        cellAction();
    }

    private void cellAction() {
        if (map[player.getCellY()][player.getCellX()].isPlayerIn() && map[player.getCellY()][player.getCellX()].isAvailable()) {
            if (map[player.getCellY()][player.getCellX()] instanceof FightCell || map[player.getCellY()][player.getCellX()] instanceof EventCell) {
                dispose();
            }
            map[player.getCellY()][player.getCellX()].action(this);
        }
    }

    private void input() {
        if (Gdx.input.isTouched()) {
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition);

        }
    }

    private void movePlayer(String direction) {
        switch (direction) {
            case "down":
                if (moveIsPossible(player.getCellY() + 1, player.getCellX())) {
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    characterDirection = DIRECTION_DOWN;
                    playerIsMoving = true;
                }
                break;
            case "up":
                if (moveIsPossible(player.getCellY() - 1, player.getCellX())) {
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    characterDirection = DIRECTION_UP;
                    playerIsMoving = true;
                }
                break;
            case "right":
                if (moveIsPossible(player.getCellY(), player.getCellX() + 1)) {
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    characterDirection = DIRECTION_RIGHT;
                    playerIsMoving = true;
                }
                break;
            case "left":
                if (moveIsPossible(player.getCellY(), player.getCellX() - 1)) {
                    map[player.getCellY()][player.getCellX()].setPlayerIn(false);
                    characterDirection = DIRECTION_LEFT;
                    playerIsMoving = true;
                }
        }
    }

    private boolean moveIsPossible(int x, int y) {
        if (x >= map.length || x < 0) {
            return false;
        } else if (y >= map[x].length || y < 0) {
            return false;
        }
        return null != map[x][y];
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height);
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
        backgroundMusic.stop();
        backgroundMusic.dispose();
    }

    public Player getPlayer() {
        return player;
    }

    public CellMap[][] getMap() {
        return map;
    }
}
