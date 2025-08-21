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
import io.github.some_example_name.utils.DialogBox;


public class TutorialScreen implements Screen {
    private StretchViewport viewport;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private Sprite dialogWindow;
    private DialogBox[] dialogBoxes;
    private int indexOfBox;
    private Stage stage;
    private final DrawableScreen screen;
    private final Screen nextScreen;
    private SpriteBatch batch;
    private float visibleTextTimer;
    private String visibleText;
    private int indexOfNextChar;
    private float spriteHeight,spriteWidth;
    private static final float SPEED_TEXT_APPEARANCE = 11F, MAX_VISIBLE_TEXT_TIMER = 0.5f;

    public TutorialScreen(DrawableScreen screen, Screen nextScreen,String dialogPath) {
        this.screen = screen;
        this.nextScreen = nextScreen;
        showDialogBoxes(dialogPath);
    }

    @Override
    public void show() {
        visibleTextTimer = 0;
        indexOfNextChar = 0;
        visibleText = "";
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        screen.show();

        viewportConfiguration();
        stage = new Stage(viewport,batch);
        indexOfBox = 0;
        glyphLayout = new GlyphLayout();

        showWindowBox();
        showInput();
        calculateTextWidthAndHeight();


    }
    private void showInput(){
        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(visibleText.equals(dialogBoxes[indexOfBox].getText())){
                    indexOfBox ++;
                    if(indexOfBox >= dialogBoxes.length) {
                        dispose();
                        ((Main) Gdx.app.getApplicationListener()).setScreen(nextScreen);
                    }else {
                        calculateTextWidthAndHeight();
                        visibleText = "";
                        indexOfNextChar = 0;
                    }

                }else {
                    visibleText = dialogBoxes[indexOfBox].getText();
                }

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
        batch.setProjectionMatrix(viewport.getCamera().combined);
        screen.draw(delta);

        addInVisibleTextChar(delta);
        batch.begin();
        drawSprite();
        drawText();
        batch.end();

    }
    private void addInVisibleTextChar(float delta){
        if(!visibleText.equals(dialogBoxes[indexOfBox].getText())){

            visibleTextTimer += delta*SPEED_TEXT_APPEARANCE;
            if(visibleTextTimer >= MAX_VISIBLE_TEXT_TIMER){
                visibleTextTimer = 0;
                visibleText += dialogBoxes[indexOfBox].getText().charAt(indexOfNextChar);
                indexOfNextChar ++;
            }
        }
    }
    private void calculateTextWidthAndHeight(){
        float targetWidth = 100;
        glyphLayout.setText(font,dialogBoxes[indexOfBox].getText(), Color.WHITE,targetWidth, Align.topLeft,true);
        while (glyphLayout.height - glyphLayout.width > -100){
            targetWidth += 0.5F;
            glyphLayout.setText(font,dialogBoxes[indexOfBox].getText(), Color.WHITE,targetWidth, Align.topLeft,true);
        }
        spriteHeight = glyphLayout.height;
        spriteWidth = glyphLayout.width;


    }

    private void drawText(){
        glyphLayout.setText(font,visibleText, Color.WHITE,spriteWidth, Align.topLeft,true);
        font.draw(batch,glyphLayout,dialogBoxes[indexOfBox].getX()-spriteWidth/2,dialogBoxes[indexOfBox].getY()+spriteHeight/2);
    }
    private void drawSprite(){
        dialogWindow.setSize(spriteWidth+100,spriteHeight+50);
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
    public void dispose()  {
        if (font != null) {
            font.dispose();
            font = null;
        }

        if (batch != null) {
            batch.dispose();
            batch = null;
        }

        if (stage != null) {
            stage.dispose();
            stage = null;
        }

        if (dialogWindow != null && dialogWindow.getTexture() != null) {
            dialogWindow.getTexture().dispose();
            dialogWindow = null;
        }


        if (dialogBoxes != null) {
            dialogBoxes = null;
        }

        glyphLayout = null;
        visibleText = null;
    }
}
