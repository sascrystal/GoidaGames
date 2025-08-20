package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.some_example_name.Main;
import io.github.some_example_name.SettingsGame;
import io.github.some_example_name.utils.DialogBox;
import io.github.some_example_name.utils.ShopSkin;

public class TutorialScreen implements Screen {
    private StretchViewport viewport;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private Sprite dialogWindow;
    private DialogBox[] dialogBoxes;
    private int indexOfBox;
    private Stage stage;
    private DrawableScreen screen;
    private Screen nextScreen;
    private SpriteBatch batch;

    public TutorialScreen(DrawableScreen screen, Screen nextScreen,String dialogPath) {
        this.screen = screen;
        this.nextScreen = nextScreen;
        showDialogBoxes(dialogPath);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        screen.show();

        viewportConfiguration();
        stage = new Stage(viewport,batch);
        indexOfBox = 0;
        glyphLayout = new GlyphLayout();

        showWindowBox();
        showInput();

    }
    private void showInput(){
        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                indexOfBox ++;
                super.clicked(event, x, y);
            }
        });
        Gdx.input.setInputProcessor(stage);
    }
    private void showWindowBox(){
        Texture texture = new Texture("goida.png");
        dialogWindow = new Sprite(texture);
    }
    private void showDialogBoxes(String path){
        Json json = new Json();
        dialogBoxes = json.fromJson(DialogBox[].class, Gdx.files.internal(path));

    }
    private void viewportConfiguration(){
        viewport = new StretchViewport(screen.getViewport().getWorldWidth(), screen.getViewport().getWorldHeight());
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }

    @Override
    public void render(float delta) {
        draw(delta);
    }
    private void draw(float delta){
        if(indexOfBox >= dialogBoxes.length){
            dispose();
            ((Main) Gdx.app.getApplicationListener()).setScreen(nextScreen);
        }else {
            screen.draw(delta);
            calculateTextWidthAndHeight(delta);
            batch.begin();
            drawSprite();
            drawText();

            batch.end();
        }

    }
    private void calculateTextWidthAndHeight(float delta){
        float targetWidth = 100;
        glyphLayout.setText(font,dialogBoxes[indexOfBox].getText(), Color.WHITE,targetWidth, Align.left,true);
        while (glyphLayout.height - glyphLayout.width > -100){
            targetWidth += delta;
            glyphLayout.setText(font,dialogBoxes[indexOfBox].getText(), Color.WHITE,targetWidth, Align.left,true);
        }
    }

    private void drawText(){
        font.draw(batch,glyphLayout,dialogBoxes[indexOfBox].getX()-glyphLayout.width/2,dialogBoxes[indexOfBox].getY()+glyphLayout.height/2);
    }
    private void drawSprite(){
        dialogWindow.setSize(glyphLayout.width+50,glyphLayout.height+50);
        dialogWindow.setPosition(dialogBoxes[indexOfBox].getX()-dialogWindow.getWidth()/2,dialogBoxes[indexOfBox].getY()- dialogWindow.getHeight()/2);

        dialogWindow.draw(batch);
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        screen.resize(width,height);

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
}
