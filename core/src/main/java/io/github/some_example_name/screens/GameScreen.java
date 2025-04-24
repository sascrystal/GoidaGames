package io.github.some_example_name.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.StretchViewport;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.Main;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.cards.non_target_cards.NonTargetCard;
import io.github.some_example_name.cards.PlayingCard;


public class GameScreen implements Screen {
    private boolean endTurnButtonPressed = false;
    private SpriteBatch batch;
    private Rectangle[] cardBounds;
    private boolean[] isCardVisible;
    private TextureRegion draggedCard;
    private float draggedCardX, draggedCardY;
    private boolean isDragging;
    private int draggedCardIndex;
    public static StretchViewport viewport = new StretchViewport(2400, 1080);



    public static final int HAND_META = 10;


    private float[] initialCardPositionsX,initialCardPositionsY;


    private Music backgroundMusic;


    private Sound soundEffectCardTaking, soundEffectPlaceCard, soundEffectEndTurn,
        soundEffectNotEnoughMana;


    private Texture cardInfoTexture,attackImage, BGImage, interfaceImage;// Текстура для отображения информации о карте


    private String cardName; // Название карты
    private String cardDescription; // Описание карты
    private boolean isCardInfoVisible;


    // Добавляем противника и игрока
    private final Enemy[] enemies;
    private final Player player;
    private boolean playerTurn;
    private BitmapFont font;


    // Кнопка завершения хода
    private Texture endTurnButtonTexture;
    private Rectangle endTurnButtonBounds;


    private  float elapsedTime = 0;


    private float cardAnimationTime;
    private final List<PlayingCard> animationCardQueue =  new ArrayList<>();
    private final List<Integer> animationCardQueueIndex = new ArrayList<>();

    private Vector2 touchPos;

    private final CellMap[][] map;

    // Невидимое поле для карт
    private Rectangle invisibleCardArea;

    public GameScreen(Enemy[] enemies, MapScreen map){
        this.enemies = enemies;
        this.map = map.getMap();
        this.player = map.getPlayer();
        player.beginFight();
        player.beginTurn();
    }

    @Override
    public void show() {
        // Инициализация StretchViewport
        batch = new SpriteBatch();
        showInterface();
        showCards();
        showEnemies();
        showFont();
        showEndButton();
        musicShow();
        playerTurn = true; // Начинаем с хода игрока
        cardAnimationTime = 0F;
    }
    private void showEndButton(){
        endTurnButtonTexture = new Texture(Gdx.files.internal("HUD/endhod_new.png"));
        endTurnButtonBounds = new Rectangle(
            (float) (viewport.getWorldWidth() / 1.173),
            (float) (viewport.getWorldHeight() / 2.8),
            (float) (endTurnButtonTexture.getWidth()/0.7),
            (float)(endTurnButtonTexture.getHeight()/0.7));
    }
    private void showInterface(){
        BGImage = new Texture(Gdx.files.internal("menu/BG_2.png"));
        attackImage = new Texture(Gdx.files.internal("HUD/attak.png"));
        interfaceImage = new Texture(Gdx.files.internal("HUD/interface.png"));
    }
    private  void showCards(){
        cardBounds = new Rectangle[HAND_META];
        isCardVisible = new boolean[HAND_META];
        initialCardPositionsX = new float[HAND_META];
        initialCardPositionsY = new float[HAND_META];
        invisibleCardArea = new Rectangle(0, 0, viewport.getWorldWidth(), 250); // Задаем ширину и высоту
        cardInfoTexture = new Texture(Gdx.files.internal("HUD/card_info.png")); // Загружаем текстуру
        isCardInfoVisible = false;
        cardAnimationTime = 0F;
        preRenderCards();
    }
    private void showFont(){
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(2.0f);
    }
    private void  showEnemies(){
        if(enemies[1]!= null){
            enemies[1].getBounds().setX(enemies[0].getBounds().getX()+ enemies[0].getBounds().getWidth() + 200);
        }
        if(enemies[2] != null){
            enemies[2].getBounds().setX(enemies[0].getBounds().getX()- enemies[1].getBounds().getWidth() - 200);
        }
    }


    @Override
    public void render(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        elapsedTime += delta;
        cardAnimationUpdate(delta);
        music();
        draw();
        handleInput();

    }
    private void cardAnimationUpdate(float delta){
        if (!animationCardQueue.isEmpty()) {
            cardAnimationTime += delta;
            // Проверка завершения анимации
            if (animationCardQueue.get(0).getEffect().isAnimationFinished(cardAnimationTime)) {
                animationCardQueue.remove(0);
                animationCardQueueIndex.remove(0);
                cardAnimationTime = 0F;
            }
        }
    }
    private void music(){
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f);
        backgroundMusic.play();
    }
    private void draw(){
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        interfaceDraw();
        playerStatsDraw();
        enemiesDraw();
        renderingCards();
        draggedCardDraw();
        if (isCardInfoVisible) {
            cardInfoDraw();
        }
        if(isPlayerWin()){
            playerWin();
        }
        if (!animationCardQueue.isEmpty()) {
            cardAnimationDraw();
        }
        batch.end();

    }
    private void interfaceDraw(){
        batch.draw(BGImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(interfaceImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(endTurnButtonTexture, endTurnButtonBounds.x, endTurnButtonBounds.y);
    }
    private void playerStatsDraw(){
        font.draw(batch, String.valueOf(player.getHealth()), 20, (float)(viewport.getWorldHeight() / 1.5));
        font.draw(batch, String.valueOf(player.getShield()), 120, (float)(viewport.getWorldHeight() / 1.5));
        font.draw(batch, String.valueOf(player.getManaPool()), 220, (float)(viewport.getWorldHeight() / 2.25));
    }
    private void enemiesDraw(){
        for (int i = 0; i<3; i++){
            if(enemies[i] != null && enemies[i].isAlive()){
                enemies[i].draw(batch, font, elapsedTime,player);
            }
        }
    }
    private void draggedCardDraw(){
        if (isDragging && draggedCard != null) {
            batch.draw(draggedCard, draggedCardX, draggedCardY, cardBounds[0].width, cardBounds[0].height);
        }
    }
    private void cardInfoDraw(){
        float cardInfoX = viewport.getWorldWidth() - cardInfoTexture.getWidth() - 10;
        float cardInfoY = viewport.getWorldHeight() - cardInfoTexture.getHeight() - 10;


        // Отрисовка текстуры информации о карте
        batch.draw(cardInfoTexture, cardInfoX, cardInfoY);


        // Отрисовка названия карты
        font.draw(batch, cardName, cardInfoX + ((float) cardInfoTexture.getWidth() / 2) - 250, cardInfoY + cardInfoTexture.getHeight() - 10); // Отображаем название карты


        // Отрисовка описания карты с переносом текста
        float descriptionX = cardInfoX + 10; // Отступ для описания
        float descriptionY = cardInfoY + cardInfoTexture.getHeight() - 110; // Позиция для описания
        float maxWidth = cardInfoTexture.getWidth() - 20; // Максимальная ширина для текста (отступы)

        font.draw(batch, cardDescription, descriptionX, descriptionY, maxWidth, 1, true); // true для переноса строк
    }
    private void cardAnimationDraw(){
        animationCardQueue.get(0).drawAnimation(cardAnimationTime, batch, enemies[animationCardQueueIndex.get(0)]);
    }





    private void handleInput() {
        if (Gdx.input.isTouched()) {
            touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            endTurnButtonPressed = endTurnButtonBounds.contains(touchPos) && playerTurn; // Устанавливаем флаг нажатия
            if (!isDragging) {
                touchCards();
            } else {
                moveCard();
            }
        } else {
            if (isDragging) {




                // Проверяем, находится ли карта в невидимом поле
                if (invisibleCardArea.contains(draggedCardX, draggedCardY)) {
                    soundEffectPlaceCard.play(0.7f);
                    // Возвращаем карту на исходную позицию
                    returnCard();
                } else {
                    boolean returnCard = true;
                    if (manaPoolCheck()){
                        if(player.getHand()[draggedCardIndex] instanceof NonTargetCard &&
                            !invisibleCardArea.contains(cardBounds[draggedCardIndex])){
                            useCard();
                            returnCard = false;
                        }else {
                            for(int i = 0; i<3; i++) {
                                if (manaPoolCheck() && enemies[i] != null &&
                                    enemies[i].getBounds().contains(touchPos.x, Gdx.graphics.getHeight() - touchPos.y)) {
                                    useCard(i);
                                    returnCard = false;
                                    break;
                                }
                            }
                        }

                    }
                    if (returnCard){
                        soundEffectNotEnoughMana.play(0.7f);
                        returnCard();
                    }
                }
            }
            isDragging = false; // Завершаем перетаскивание
            draggedCard = null; // Освобождаем ссылку на перетаскиваемую карту


            // Если кнопка завершения хода была нажата, выполняем завершение хода
            if (endTurnButtonPressed) {
                soundEffectEndTurn.play(0.8f);
                endTurn(); // Завершение хода
                endTurnButtonPressed = false; // Сбрасываем флаг
            }
        }
    }

    private void touchCards(){
        for (int i = 0; i < cardBounds.length; i++) {
            if (player.getHand()[i]!= null && isCardVisible[i] && cardBounds[i].contains(touchPos)) {
                takeCard(i);
                break;
            }
        }
    }
    private void takeCard(int index){
        isDragging = true;
        draggedCard = new TextureRegion(player.getHand()[index].getTexture());
        draggedCardX = touchPos.x - cardBounds[index].width / 2;
        draggedCardY = touchPos.y - cardBounds[index].height / 2;


        // Сохраняем информацию о карте
        cardName = player.getHand()[index].getName();
        cardDescription = player.getHand()[index].getDescription();
        isCardInfoVisible = true; // Показываем информацию о карте


        // Сделаем карту невидимой сразу, как только она берется
        soundEffectCardTaking.play(0.7f);
        isCardVisible[index] = false;
        draggedCardIndex = index;
    }
    private void moveCard(){
        isCardInfoVisible = true; // Скрываем информацию о карте
        // Обновляем позицию перетаскиваемой карты
        draggedCardX =  touchPos.x-  (float) draggedCard.getRegionWidth() / 2;
        draggedCardY = touchPos.y -  (float) draggedCard.getRegionHeight() / 2;
    }

    private void endTurn() {
        playerTurn = false;// Завершаем ход игрока
        for (int i = 0; i<3; i++){
            if(enemies[i] != null && enemies[i].isAlive()){
                enemies[i].endTurn(player);
            }
        }

        // Проверка здоровья игрока
        if (player.getHealth() <= 0) {
            backgroundMusic.stop();
            this.dispose();
            ((Main) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
        }

        player.endTurn();

        playerTurn = true; // Ход переходит обратно к игроку
        player.beginTurn(); // Обновляем карты в руке игрока

        // Обновляем видимость карт
        // делаем все карты видимыми (или можете настроить по вашему усмотрению)
        Arrays.fill(isCardVisible, true);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}


    @Override
    public void resume() {}


    @Override
    public void hide() {}


    @Override
    public void dispose() {
        cardInfoTexture.dispose();
        batch.dispose();
        BGImage.dispose();
        interfaceImage.dispose();
        attackImage.dispose();
        endTurnButtonTexture.dispose();
        for(int i = 0; i<3 ; i++){
            if(enemies[i] != null){
                enemies[i].dispose();
            }
        }
        font.dispose();

        for (int i = 0; i < player.getHand().length-1 && player.getHand()[i] != null;  i++) {
            player.getHand()[i].getTexture().dispose();
        }

        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }

        soundEffectEndTurn.dispose();
        soundEffectCardTaking.dispose();
        soundEffectNotEnoughMana.dispose();
        soundEffectPlaceCard.dispose();
        animationCardQueue.clear();
    }

    private void preRenderCards() {
        float cardHeight = 300;
        float cardWidth = (float)(cardHeight * 0.6); // Ширина карты
        float startX = 500; // Центрирование по X
        float startY = 20; // Фиксированная позиция Y


        for (int i = 0; i < cardBounds.length; i++) {
            cardBounds[i] = new Rectangle(startX + i * cardWidth, startY, cardWidth, cardHeight);


            isCardVisible[i] = true;
            initialCardPositionsX[i] = cardBounds[i].x;
            initialCardPositionsY[i] = cardBounds[i].y;
        }
    }

    private void renderingCards() {
        for (int i = 0; i < player.getHand().length; i++) {
            if (isCardVisible[i] && player.getHand()[i] != null) {
                // Используем координаты cardBounds для отрисовки карт
                batch.draw(player.getHand()[i].getTexture(), cardBounds[i].x, cardBounds[i].y, cardBounds[i].width, cardBounds[i].height);


            }
        }
    }

    private void useCard(int i){
        animationCardQueue.add(player.getHand()[draggedCardIndex]);
        animationCardQueueIndex.add(i);

        enemies[i].buffActionTrigger("CardAction");

        player.playCard(enemies[i], draggedCardIndex);
        isCardInfoVisible = false; // Показываем информацию о карте
        preRenderCards();
    }

    private void useCard(){
        animationCardQueue.add(player.getHand()[draggedCardIndex]);
        animationCardQueueIndex.add(0);
        player.playCard(enemies[0], draggedCardIndex);
        isCardInfoVisible = false; // Показываем информацию о карте
        preRenderCards();
    }

    private void returnCard(){
        cardBounds[draggedCardIndex].setPosition(initialCardPositionsX[draggedCardIndex],
            initialCardPositionsY[draggedCardIndex]);


        isCardVisible[draggedCardIndex] = true;
        isCardInfoVisible = false;
    }

    private boolean manaPoolCheck(){
        return player.getManaPool() - player.getHand()[draggedCardIndex].getCost() >= 0;


    }

    private void musicShow(){
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fightMusic.mp3"));
        soundEffectCardTaking = Gdx.audio.newSound(Gdx.files.internal("sounds/takeCard.wav"));
        soundEffectPlaceCard = Gdx.audio.newSound(Gdx.files.internal("sounds/placeCard.wav"));
        soundEffectEndTurn = Gdx.audio.newSound(Gdx.files.internal("sounds/endTurn.wav"));
        soundEffectNotEnoughMana = Gdx.audio.newSound(Gdx.files.internal("sounds/notEnoughMana.wav"));
    }
    private boolean isPlayerWin(){
        boolean isPlayerWin = true;
        for(int i = 0; i<3;i++){
            if(enemies[i]!= null && enemies[i].isAlive()){
                isPlayerWin = false;
                break;
            }
        }
        return isPlayerWin;

    }
    private void playerWin(){
        player.dropDeckClear();
        player.draftDeckClear();
        player.handClear();
        backgroundMusic.stop();
        this.dispose();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new MapScreen(player, map));
    }

}
//СОСАТЬ АМЕРИКА

