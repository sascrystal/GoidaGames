package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

import io.github.some_example_name.utils.Account;
import io.github.some_example_name.Main;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.CharacterKnight;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.utils.ShopSkin;

public class FirstScreen implements Screen {
    private static final float NEW_GAME_BUTTON_SCALING = 0.48f, CONTINUE_GAME_BUTTON_SCALING = 0.5f,
        TUTORIAL_BUTTON_SCALING = 0.5f, LOGO_SCALING = 0.8f,SHOP_BUTTON_SCALING = 0.5F,
        LOGO_SHOP_SCALING = 0.6F, SHOP_SCALE = 1.2f, MOVEMENT_BUTTONS_SCALE = 0.30F;
    private static final float INDENT_BUTTONS = 10, STARTING_POSITION = 200,INDENT_MOVEMENTS_BUTTONS = 60,BUTTON_BUY_SCALE = 0.2F;
    private static final float POSITION_FOR_MOVEMENT_BUTTONS= 400;
    private static final float STARTING_POSITION_FOR_FIRST_BUY_BUTTON = 700,BETWEEN_SPACE = 250,Y_POSITION_FOR_BUY_BUTTONS = 100;
    private static final float AMPLITUDE_SHAKING = 30, SPEED_SHAKING = 2f, STEP_VALUE = 0.3f;
    private static final float INVENTORY_BUTTON_SCALE = 0.5f;
    private static final float SCALE_FOR_SKINS_INVENTORY = 0.3f,Y_POSITION_FOR_INVENTORY_SKINS = 300;
    private static final float START_POSITION_FOR_SHOP_SKINS,INDENT_FOR_SHOP_SKINS,Y_POSITION_FOR_SHOP_SKINS;
    private static final float SELECT_BUTTON_SCALE = 0.4f;
    private static final float MONEY_SHOP_BUTTON_SCALE = 0.1f;
    private static final float SCALE_FOR_FIRST_ICON_MONEY=0.2F,SCALE_FOR_SECOND_ICON_MONEY=0.2F,SCALE_FOR_THIRD_ICON_MONEY=0.4F;
    private static final float Y_POSITION_FOR_FIRST_AND_SECOND_ICON_MONEY = 650,Y_POSITION_FOR_THIRD_ICON_MONEY =200;

    private static final String NAME_FOR_BUY_BUTTONS = "buy_button";

    static {
        Texture buyButtonTexture = new Texture(Gdx.files.internal("menu/buy_button.png"));
        START_POSITION_FOR_SHOP_SKINS = STARTING_POSITION_FOR_FIRST_BUY_BUTTON+buyButtonTexture.getWidth()*BUTTON_BUY_SCALE/2;
        INDENT_FOR_SHOP_SKINS = BETWEEN_SPACE+buyButtonTexture.getWidth()*BUTTON_BUY_SCALE;
        Y_POSITION_FOR_SHOP_SKINS = Y_POSITION_FOR_BUY_BUTTONS + 100;
    }

    private MenuCondition menuCondition;
    private ArrayList<ShopSkin> lockedSkins, unlockedSkins;
    private Sprite inventoryWindow;
    private ShopSkin[] allSkins;
    private Texture  moneyTexture;
    private Sprite moneyShopWindow;
    private BitmapFont font;
    private int indexOfFirstSkin;
    private Sound shopBellSound;
    private Account account;
    private Texture BG_image;
    private Texture logoTexture, logoShopTexture;
    private Texture buyButtonTexture, buyButtonUnavaibleTexture;
    private Sprite firstIconForMoney, secondIconForMoney, thirdIconForMoney;

    private  Texture shopTexture;
    private SpriteBatch batch;
    private StretchViewport viewport;
    private Stage mainStage,shopStage,inventoryStage,shopMoneyStage;
    private Music backgroundMusic, backgroundNoiseMenu;
    private float logoStep = 0, logoShakingDelta = 0, logoELapsedTime = 0;

    @Override
    public void show() {
        menuCondition = MenuCondition.MAIN;
        allSkins = ShopSkin.getAllSkins();
        account = new Account();
        batch = new SpriteBatch();
        viewport = new StretchViewport(2400, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.apply();
        showMainStage();
        showShopStage();
        showInventoryStage();
        showShopMoneyStage();
        showMusic();
        BG_image = new Texture(Gdx.files.internal("menu/menu_background.png"));
        logoTexture = new Texture(Gdx.files.internal("menu/goblin_cards_logo.png"));
        logoShopTexture = new Texture(Gdx.files.internal("menu/shop_logo.png"));

    }
    private void showShopMoneyStage(){
        shopMoneyStage = new Stage(viewport,batch);
        showMoneyShopWindow();
        showMoneyShopIconsForBuyButtons();
        showMoneyShopReturnButton();
        showMoneyShopBuyButtons();

    }
    private void showMoneyShopIconsForBuyButtons(){
        Texture firstIcon = new Texture("menu/coin.png");
        Texture secondIcon = new Texture("menu/second_icon_for_buy_buttons_money.png");
        Texture thirdIcon = new Texture("menu/third_icon_for_buy_buttons_money.png");

        firstIconForMoney = new Sprite(firstIcon);
        secondIconForMoney = new Sprite(secondIcon);
        thirdIconForMoney = new Sprite(thirdIcon);

        firstIconForMoney.setSize(
            firstIcon.getWidth()*SCALE_FOR_FIRST_ICON_MONEY,
            firstIcon.getHeight()*SCALE_FOR_FIRST_ICON_MONEY
        );
        secondIconForMoney.setSize(
            secondIcon.getWidth()*SCALE_FOR_SECOND_ICON_MONEY,
            secondIcon.getHeight()*SCALE_FOR_FIRST_ICON_MONEY
        );
        thirdIconForMoney.setSize(
            thirdIcon.getWidth()*SCALE_FOR_FIRST_ICON_MONEY,
            thirdIcon.getHeight()*SCALE_FOR_THIRD_ICON_MONEY
        );

        firstIconForMoney.setPosition(
            moneyShopWindow.getX()+moneyShopWindow.getWidth()/4-firstIconForMoney.getWidth()/2,
            Y_POSITION_FOR_FIRST_AND_SECOND_ICON_MONEY
        );
        secondIconForMoney.setPosition(
            moneyShopWindow.getX()+3*moneyShopWindow.getWidth()/4-secondIconForMoney.getWidth()/2,
            Y_POSITION_FOR_FIRST_AND_SECOND_ICON_MONEY
        );
        thirdIconForMoney.setPosition(
            moneyShopWindow.getX()+moneyShopWindow.getWidth()/2-thirdIconForMoney.getWidth()/2,
            Y_POSITION_FOR_THIRD_ICON_MONEY
        );
    }
    private void showMoneyShopBuyButtons(){
        Texture buyTexture = new Texture("menu/buy_button.png");

        ImageButton[] buyButtons = new ImageButton[3];
        for(ImageButton buyButton : buyButtons){
            buyButton = new ImageButton(new TextureRegionDrawable(buyTexture));
            buyButton.setSize(
                buyTexture.getWidth()*BUTTON_BUY_SCALE,
                buyTexture.getHeight()*BUTTON_BUY_SCALE
            );
        }




    }
    private void showMoneyShopReturnButton(){
        Texture returnButtonTexture = new Texture(Gdx.files.internal("buttons/left_button.png"));
        ImageButton returnButton = new ImageButton(new TextureRegionDrawable(returnButtonTexture));
        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuCondition = MenuCondition.MAIN;
                Gdx.input.setInputProcessor(mainStage);
                super.clicked(event, x, y);
            }
        });
        returnButton.setSize(returnButtonTexture.getWidth()*MOVEMENT_BUTTONS_SCALE,returnButtonTexture.getHeight()*MOVEMENT_BUTTONS_SCALE);
        returnButton.setPosition(viewport.getWorldWidth() /2 - moneyShopWindow.getWidth()/2-returnButton.getWidth(), viewport.getWorldHeight()-returnButton.getHeight());
        shopMoneyStage.addActor(returnButton);

    }
    private void showMoneyShopWindow(){
        Texture moneyShopWindowTexture = new Texture("menu/money_shop_window.png");
        moneyShopWindow = new Sprite(moneyShopWindowTexture);
        moneyShopWindow.setSize(moneyShopWindowTexture.getWidth()*(viewport.getWorldHeight()/moneyShopWindowTexture.getHeight()),
            viewport.getWorldHeight());
        moneyShopWindow.setPosition(viewport.getWorldWidth()/2 - moneyShopWindow.getWidth()/2,0);
    }
    private void showMoneySprite(){
        moneyTexture = new Texture("menu/coin.png");
    }
    private void showMusic(){
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mainMenuMusic.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.7f);
        backgroundMusic.play();

        backgroundNoiseMenu = Gdx.audio.newMusic(Gdx.files.internal("music/backgroundNoiseMenu.mp3"));
        backgroundNoiseMenu.setLooping(true);
        backgroundNoiseMenu.setVolume(0.5f);
        backgroundNoiseMenu.play();

        shopBellSound = Gdx.audio.newSound(Gdx.files.internal("music/shop_bell.wav"));

    }
    private void showShopStage(){
        shopStage = new Stage(viewport,batch);
        shopTexture = new Texture(Gdx.files.internal("menu/shop.png"));


        Texture textureLeftShop = new Texture(Gdx.files.internal("buttons/left_button.png"));
        Texture textureRightShop = new Texture(Gdx.files.internal("buttons/right_button.png"));

        ImageButton leftShopButton = new ImageButton(new TextureRegionDrawable(textureLeftShop));
        ImageButton returnButton = new ImageButton(new TextureRegionDrawable(textureLeftShop));
        ImageButton rightShopButton = new ImageButton(new TextureRegionDrawable(textureRightShop));

        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuCondition = MenuCondition.MAIN;
                Gdx.input.setInputProcessor(mainStage);
                super.clicked(event, x, y);
            }
        });


        leftShopButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(indexOfFirstSkin-3>= 0){
                    indexOfFirstSkin-=3;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        rightShopButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(indexOfFirstSkin+3< lockedSkins.size()){
                    indexOfFirstSkin+=3;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        leftShopButton.setSize(
            textureLeftShop.getWidth()*MOVEMENT_BUTTONS_SCALE,
            textureLeftShop.getHeight()*MOVEMENT_BUTTONS_SCALE);
        rightShopButton.setSize(
            textureRightShop.getWidth()*MOVEMENT_BUTTONS_SCALE,
            textureRightShop.getHeight()*MOVEMENT_BUTTONS_SCALE);
        returnButton.setSize(
            textureLeftShop.getWidth()*MOVEMENT_BUTTONS_SCALE,
            textureLeftShop.getHeight()*MOVEMENT_BUTTONS_SCALE);

        leftShopButton.setPosition(
            POSITION_FOR_MOVEMENT_BUTTONS,
            INDENT_MOVEMENTS_BUTTONS);
        rightShopButton.setPosition(
            viewport.getWorldWidth()-POSITION_FOR_MOVEMENT_BUTTONS-textureRightShop.getWidth()*MOVEMENT_BUTTONS_SCALE,
            INDENT_MOVEMENTS_BUTTONS);
        returnButton.setPosition(
            viewport.getWorldWidth() /2 - (float) shopTexture.getWidth() *SHOP_SCALE/2,
            shopTexture.getHeight() *SHOP_SCALE-returnButton.getHeight());

        shopStage.addActor(leftShopButton);
        shopStage.addActor(rightShopButton);
        shopStage.addActor(returnButton);


        Texture buyButtonTexture = new Texture(Gdx.files.internal("menu/buy_button.png"));
        buyButtonUnavaibleTexture = new Texture(Gdx.files.internal("menu/buy_button_unavailable.png"));
        ImageButton[] buttonsBuy = new ImageButton[3];
        for (int i = 0; i<buttonsBuy.length; i++) {
            buttonsBuy[i] = new ImageButton(new TextureRegionDrawable(buyButtonTexture));
            buttonsBuy[i].setName(NAME_FOR_BUY_BUTTONS+i);
            final int index = i;
            buttonsBuy[i].setSize(buyButtonTexture.getWidth()*BUTTON_BUY_SCALE,buyButtonTexture.getHeight()*BUTTON_BUY_SCALE);
            buttonsBuy[i].setPosition(STARTING_POSITION_FOR_FIRST_BUY_BUTTON+i*(BETWEEN_SPACE+buyButtonTexture.getWidth()*BUTTON_BUY_SCALE),Y_POSITION_FOR_BUY_BUTTONS);
            buttonsBuy[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(indexOfFirstSkin+index <lockedSkins.size() &&lockedSkins.get(indexOfFirstSkin+index)!= null){
                        if (lockedSkins.get(indexOfFirstSkin+index).getPrice() <= account.getMoney()){
                            lockedSkins.get(indexOfFirstSkin+index).setUnlocked(true);
                            account.setMoney(account.getMoney()-lockedSkins.get(indexOfFirstSkin+index).getPrice());
                            lockedSkins = ShopSkin.takeOnlyLockedSkins(allSkins);
                        }
                    }
                    super.clicked(event, x, y);
                }
            });
        }
        for (ImageButton buttonBuy : buttonsBuy) {
            shopStage.addActor(buttonBuy);
        }



    }
    private void showInventoryStage(){
        inventoryStage = new Stage(viewport,batch);
        showInventoryWindow();
        showInventoryReturnButton();
        showSwapButtonSkin();
        showSelectButton();

    }
    private void showSelectButton(){
        Texture selectButtonTexture = new Texture(Gdx.files.internal("menu/select_button.png"));
        ImageButton selectButton = new ImageButton(new TextureRegionDrawable(selectButtonTexture));
        selectButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                account.setIndexOfSelectedSkin(indexOfFirstSkin);
                menuCondition = MenuCondition.MAIN;
                Gdx.input.setInputProcessor(mainStage);
                super.clicked(event, x, y);
            }
        });
        selectButton.setSize(selectButtonTexture.getWidth()*SELECT_BUTTON_SCALE,selectButtonTexture.getHeight()*SELECT_BUTTON_SCALE);
        selectButton.setPosition(viewport.getWorldWidth()/2-selectButton.getWidth()/2,INDENT_MOVEMENTS_BUTTONS);
        inventoryStage.addActor(selectButton);

    }
    private void showSwapButtonSkin(){
        Texture textureLeftShop = new Texture(Gdx.files.internal("buttons/left_button.png"));
        Texture textureRightShop = new Texture(Gdx.files.internal("buttons/right_button.png"));

        ImageButton leftShopButton = new ImageButton(new TextureRegionDrawable(textureLeftShop));
        ImageButton rightShopButton = new ImageButton(new TextureRegionDrawable(textureRightShop));

        leftShopButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(indexOfFirstSkin-1>= 0){
                    indexOfFirstSkin-=1;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        rightShopButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(indexOfFirstSkin+1< unlockedSkins.size()){
                    indexOfFirstSkin+=1;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        leftShopButton.setSize(textureLeftShop.getWidth()*MOVEMENT_BUTTONS_SCALE,textureLeftShop.getHeight()*MOVEMENT_BUTTONS_SCALE);
        rightShopButton.setSize(textureRightShop.getWidth()*MOVEMENT_BUTTONS_SCALE,textureRightShop.getHeight()*MOVEMENT_BUTTONS_SCALE);

        leftShopButton.setPosition(inventoryWindow.getX(),INDENT_MOVEMENTS_BUTTONS);
        rightShopButton.setPosition(inventoryWindow.getX()+inventoryWindow.getWidth()-rightShopButton.getWidth(),INDENT_MOVEMENTS_BUTTONS);

        inventoryStage.addActor(leftShopButton);
        inventoryStage.addActor(rightShopButton);


    }
    public void showInventoryReturnButton(){
        Texture returnButtonTexture = new Texture(Gdx.files.internal("buttons/left_button.png"));
        ImageButton returnButton = new ImageButton(new TextureRegionDrawable(returnButtonTexture));
        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuCondition = MenuCondition.MAIN;
                Gdx.input.setInputProcessor(mainStage);
                super.clicked(event, x, y);
            }
        });
        returnButton.setSize(returnButtonTexture.getWidth()*MOVEMENT_BUTTONS_SCALE,returnButtonTexture.getHeight()*MOVEMENT_BUTTONS_SCALE);
        returnButton.setPosition(viewport.getWorldWidth() /2 - inventoryWindow.getWidth()/2-returnButton.getWidth(), viewport.getWorldHeight()-returnButton.getHeight());
        inventoryStage.addActor(returnButton);

    }

    public void showInventoryWindow(){
        Texture inventoryWindowTexture = new Texture(Gdx.files.internal("menu/inventory_window.png"));
        inventoryWindow = new Sprite(inventoryWindowTexture);
        inventoryWindow.setSize(inventoryWindowTexture.getWidth()*(viewport.getWorldHeight()/inventoryWindowTexture.getHeight()),
            viewport.getWorldHeight());
        inventoryWindow.setPosition(viewport.getWorldWidth()/2 - inventoryWindow.getWidth()/2,0);

    }
    private void showMainStage(){
        mainStage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(mainStage);
        showMoneyShopButton();
        showMoneySprite();
        Texture startButtonTexture = new Texture(Gdx.files.internal("menu/new_game_button.png"));
        Texture continueButtonTexture = new Texture(Gdx.files.internal("menu/continue_button_unavailable.png"));
        Texture tutorialButtonTexture = new Texture(Gdx.files.internal("menu/tutorial_button.png"));
        Texture shopButtonTexture = new Texture(Gdx.files.internal("menu/shop_button.png"));
        Texture inventoryButtonTexture = new Texture(Gdx.files.internal("menu/inventory_button.png"));


        ImageButton startButton = new ImageButton(new TextureRegionDrawable(startButtonTexture));
        ImageButton continueButton = new ImageButton(new TextureRegionDrawable(continueButtonTexture));
        ImageButton tutorialButton = new ImageButton(new TextureRegionDrawable(tutorialButtonTexture));
        ImageButton shopButton = new ImageButton(new TextureRegionDrawable(shopButtonTexture));
        ImageButton inventoryButton = new ImageButton(new TextureRegionDrawable(inventoryButtonTexture));


        startButton.setSize(startButtonTexture.getWidth() * NEW_GAME_BUTTON_SCALING, startButtonTexture.getHeight() * NEW_GAME_BUTTON_SCALING);
        continueButton.setSize(continueButtonTexture.getWidth() * CONTINUE_GAME_BUTTON_SCALING, continueButtonTexture.getHeight() * CONTINUE_GAME_BUTTON_SCALING);
        tutorialButton.setSize(tutorialButtonTexture.getWidth() * TUTORIAL_BUTTON_SCALING, tutorialButtonTexture.getHeight() * TUTORIAL_BUTTON_SCALING);
        shopButton.setSize(shopButtonTexture.getWidth()*SHOP_BUTTON_SCALING, shopButtonTexture.getHeight()*SHOP_BUTTON_SCALING);
        inventoryButton.setSize(inventoryButtonTexture.getWidth()*INVENTORY_BUTTON_SCALE,inventoryButtonTexture.getHeight()*INVENTORY_BUTTON_SCALE);


        continueButton.setPosition(INDENT_BUTTONS, STARTING_POSITION);
        tutorialButton.setPosition(INDENT_BUTTONS, continueButton.getY() + continueButton.getHeight());
        startButton.setPosition(INDENT_BUTTONS, tutorialButton.getY() + tutorialButton.getHeight());
        inventoryButton.setPosition(viewport.getWorldWidth()-inventoryButton.getWidth(),STARTING_POSITION);
        shopButton.setPosition(viewport.getWorldWidth()-shopButton.getWidth(),inventoryButton.getY()+inventoryButton.getHeight());


        inventoryButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuCondition = MenuCondition.INVENTORY;
                unlockedSkins = ShopSkin.takeOnlyUnlockedSkins(allSkins);
                Gdx.input.setInputProcessor(inventoryStage);
                indexOfFirstSkin =account.getIndexOfSelectedSkin();
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start button pressed!");
                dispose();
                Player player = new CharacterKnight();
                CellMap[][] map = CellMap.generateAct1(player);
                MapScreen act1 = new MapScreen(player, map);
                DialogueScreen dialogueScreen = new DialogueScreen("intro", act1);
                ((Main) Gdx.app.getApplicationListener()).setScreen(dialogueScreen);
            }
        });


        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {//TODO: сделать продолжение игры
            }
        });


        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Player player = new CharacterKnight();
                CellMap[][] map = CellMap.createTrainingAct(player);
                MapScreen training = new MapScreen(player, map);
                ((Main) Gdx.app.getApplicationListener()).setScreen(training);
            }
        });


        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lockedSkins = ShopSkin.takeOnlyLockedSkins(allSkins);
                menuCondition = MenuCondition.SHOP;
                indexOfFirstSkin = 0;
                Gdx.input.setInputProcessor(shopStage);
                shopBellSound.play(0.5f);
                super.clicked(event, x, y);
            }
        });


        mainStage.addActor(startButton);
        mainStage.addActor(continueButton);
        mainStage.addActor(tutorialButton);
        mainStage.addActor(shopButton);
        mainStage.addActor(inventoryButton);

    }
    private void showMoneyShopButton(){
        Texture moneyShopButtonTexture = new Texture("menu/button_money_shop.png");
        ImageButton moneyShopButton = new ImageButton(new TextureRegionDrawable(moneyShopButtonTexture));
        moneyShopButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuCondition = MenuCondition.MONEY_SHOP;
                Gdx.input.setInputProcessor(shopMoneyStage);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        moneyShopButton.setSize(
            moneyShopButtonTexture.getWidth()*MONEY_SHOP_BUTTON_SCALE,
            moneyShopButtonTexture.getHeight()*MONEY_SHOP_BUTTON_SCALE);
        moneyShopButton.setPosition(
            viewport.getWorldWidth()-moneyShopButton.getWidth(),
            viewport.getWorldHeight()-moneyShopButton.getHeight()
        );
        mainStage.addActor(moneyShopButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        logoPositionCalculate(delta);
        batch.begin();
        batch.draw(BG_image, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        if(menuCondition == MenuCondition.MAIN){
            logoDraw();
        }
        batch.end();
        mainStage.draw();
        mainStage.act(delta);
        batch.begin();
        if(menuCondition ==  MenuCondition.SHOP){
            drawShop();
            //drawCoin();
        }
        if (menuCondition == MenuCondition.INVENTORY){
            drawInventory();
        }
        if (menuCondition == MenuCondition.MONEY_SHOP){
            drawMoneyShop();
            drawIconMoney();
        }
        batch.end();
        if(menuCondition ==  MenuCondition.SHOP){
            shopStage.draw();
        }
        if(menuCondition == MenuCondition.INVENTORY){
            inventoryStage.draw();
        }
        if(menuCondition == MenuCondition.MONEY_SHOP){
            shopMoneyStage.draw();

        }
        shopStage.act(delta);

    }
    private void drawIconMoney(){
        firstIconForMoney.draw(batch);
        secondIconForMoney.draw(batch);
        thirdIconForMoney.draw(batch);
    }
    private void drawMoneyShop(){
        moneyShopWindow.draw(batch);

    }
    private void drawInventory(){
        inventoryWindow.draw(batch);
        drawInventorySkin();

    }
    private void logoPositionCalculate(float delta){
        logoStep += (delta) * SPEED_SHAKING;
        if (logoStep >= STEP_VALUE) {
            logoELapsedTime += logoStep;
            logoShakingDelta = (float) Math.abs(Math.sin(logoELapsedTime));
            logoStep = 0;
        }
    }

    private void drawShop(){
        batch.draw(shopTexture,  viewport.getWorldWidth() /2 - (float) shopTexture.getWidth() *SHOP_SCALE/2,0,shopTexture.getWidth()*SHOP_SCALE,shopTexture.getHeight()*SHOP_SCALE);
        drawShopLogo();
        drawShopSkins();
    }
    private void drawInventorySkin(){
        if(unlockedSkins.size()>indexOfFirstSkin&& unlockedSkins.get(indexOfFirstSkin) != null ){
            unlockedSkins.get(indexOfFirstSkin).draw(batch,viewport.getWorldWidth()/2,Y_POSITION_FOR_INVENTORY_SKINS,SCALE_FOR_SKINS_INVENTORY);
        }


    }
    private void drawShopSkins(){
        for(int i=0;i<3;i++){
            if(lockedSkins.size()>indexOfFirstSkin+i && lockedSkins.get(indexOfFirstSkin+i) != null ){
                lockedSkins.get(indexOfFirstSkin+i).draw(batch,START_POSITION_FOR_SHOP_SKINS+INDENT_FOR_SHOP_SKINS*i,Y_POSITION_FOR_SHOP_SKINS);
            }
        }
    }

    private void drawShopLogo(){
        batch.draw(logoShopTexture, (viewport.getWorldWidth() / 2) -  (logoShopTexture.getWidth()*LOGO_SHOP_SCALING) / 2,
            950 - logoShakingDelta * AMPLITUDE_SHAKING,
            logoShopTexture.getWidth() * LOGO_SHOP_SCALING,
            logoShopTexture.getHeight() * LOGO_SHOP_SCALING);
    }

    private void logoDraw() {
        batch.draw(logoTexture, (viewport.getWorldWidth() / 2)  - (logoTexture.getWidth()*LOGO_SCALING) / 2,
            600 - logoShakingDelta * AMPLITUDE_SHAKING,
            logoTexture.getWidth() * LOGO_SCALING,
            logoTexture.getHeight() * LOGO_SCALING);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        mainStage.getViewport().update(width, height, true);
        shopStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        ShopSkin.saveSkins(allSkins);
        account.saveMoney();
        account.saveIndexOfSelectedSkin();
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        ShopSkin.saveSkins(allSkins);
        account.saveMoney();
        account.saveIndexOfSelectedSkin();

        BG_image.dispose();
        logoTexture.dispose();
        logoShopTexture.dispose();
        shopTexture.dispose();
        //buyButtonTexture.dispose();
        //buyButtonUnavaibleTexture.dispose();

        if (shopBellSound != null) {
            shopBellSound.dispose();
        }

        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }
        if (backgroundNoiseMenu != null) {
            backgroundNoiseMenu.stop();
            backgroundNoiseMenu.dispose();
        }

        if (mainStage != null) {
            mainStage.dispose();
        }
        if (shopStage != null) {
            shopStage.dispose();
        }
        if (inventoryStage != null) {
            inventoryStage.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }

        if (inventoryWindow != null && inventoryWindow.getTexture() != null) {
            inventoryWindow.getTexture().dispose();
        }
    }
    private enum MenuCondition{
        MAIN,
        SHOP,
        INVENTORY,
        MONEY_SHOP
    }
}

