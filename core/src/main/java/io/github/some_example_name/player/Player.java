package io.github.some_example_name.player;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.some_example_name.buffs.Buff;
import io.github.some_example_name.buffs.modifier_buffs.ModifierBuff;
import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.target_cards.attack_cards.ComboAttack;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.cell_map_classes.events.DialogEvent;
import io.github.some_example_name.cell_map_classes.stage.Stage;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.screens.GameScreen;

public abstract class Player {
    protected static final float CHARACTER_SCALE_ON_MAP = 0.1f;
    private static final float SPEED_WALKING_COMPRESSION = 2f, WALKING_COMPRESSION_AMPLITUDE = 20F;
    private static final float SPEED_WALKING_ROTATE = 8f, WALKING_ROTATE_AMPLITUDE = 20;


    protected List<Buff> buffs = new ArrayList<>();
    protected int cellX, cellY;
    protected float xOnScreen, yOnScreen;
    protected int health, maxHealth;
    protected float walkAnimationRotateTimer = 0;
    protected int draftCount;
    protected int manaPool;
    protected int manaPoolMax;
    protected int score;
    protected float walkAnimationCompressionValue;
    protected int shield;// Здоровье игрока
    protected Texture textureRightSight;
    protected Texture textureLeftSight ;

    protected Sprite sprite;
    protected List<PlayingCard> dropDeck = new ArrayList<>();
    protected List<PlayingCard> deck = new ArrayList<>();
    protected List<PlayingCard> draftDeck = new ArrayList<>();
    protected PlayingCard[] hand = new PlayingCard[GameScreen.HAND_META];
    private float walkAnimationTimer = 0;

    public void takeScore(Stage stage) {
        score += stage.getScore();
    }

    public void takeScore(DialogEvent dialogEvent) {
        score += dialogEvent.getScore();
    }

    public void takeScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public int getShield() {
        return shield;
    }

    public int getHealth() {
        return health;
    }

    public int getManaPool() {
        return manaPool;
    }

    public List<PlayingCard> getDeck() {
        return deck;
    }

    public PlayingCard[] getHand() {
        return hand;
    }

    public float getXOnScreen() {
        return xOnScreen;
    }

    public float getWidth() {
        return textureRightSight.getWidth() * CHARACTER_SCALE_ON_MAP;
    }

    public void setxOnScreen(float xOnScreen) {
        this.xOnScreen = xOnScreen;
    }

    public void setxOnScreen(CellMap cellMap) {
        xOnScreen = cellMap.getBounds().getX() + cellMap.getBounds().getWidth() / 2 - (float) textureRightSight.getWidth() * CHARACTER_SCALE_ON_MAP / 2;
    }

    public float getyOnScreen() {
        return yOnScreen;
    }

    public void setyOnScreen(float yOnScreen) {
        this.yOnScreen = yOnScreen;
    }

    public void setyOnScreen(CellMap cellMap) {
        this.yOnScreen = cellMap.getBounds().getY() + 15;
    }

    public void beginFight() {
        buffActionTrigger("BeginFight");
        draftDeck.addAll(deck);
        Collections.shuffle(draftDeck);
        ComboAttack.setCondition(false);
    }

    public void beginTurn() {
        buffActionTrigger("BeginTurn");
        takeCardsFromDraftDeck(draftCount);
        manaPool = manaPoolMax;
        shield = 0;
        decreaseBuff();
    }

    public void endTurn() {
        buffActionTrigger("EndTurn");
        int notFreeSpace = findFreeSpaceIndex();
        for (int i = 0; i < notFreeSpace; i++) {
            if (!hand[i].isEthereal()) {
                dropDeck.add(hand[i]);
            }
            hand[i] = null;
            if (draftDeck.isEmpty()) {
                draftDeck.addAll(dropDeck);
                Collections.shuffle(draftDeck);
                dropDeck.clear();
            }
        }

    }

    public void playCard(Enemy enemy, int index) {
        buffActionTrigger("cardAction");
        hand[index].cardAction(enemy, this, index);
        if (!hand[index].isBurnable()) {
            addDropDeck(hand[index]);
        }
        shiftHand(index);
    }

    public void takeCardsFromDraftDeck(int count) {
        for (int i = 0; i < count; i++) {
            int freeSpace = findFreeSpaceIndex();
            if (freeSpace == -1) {
                break;
            } else {
                if (draftDeck.isEmpty()) {
                    draftDeck.addAll(dropDeck);
                    Collections.shuffle(draftDeck);
                    dropDeck.clear();
                }
                if (draftDeck.isEmpty()) {
                    break;
                }
                hand[freeSpace] = draftDeck.get(0);
                draftDeck.remove(0);

            }

        }
    }

    public void addDropDeck(PlayingCard x) {
        dropDeck.add(x);
    }

    public void addCardInDeck(PlayingCard card) {
        deck.add(card);
    }

    public void shiftHand(int index) {
        if (index == hand.length - 1) {
            hand[index] = null;
        } else {
            for (int i = index; i < hand.length - 1; i++) {
                hand[i] = hand[i + 1];
            }
            if (hand[hand.length - 1] != null) {
                hand[hand.length - 1] = null;
            }
        }
    }

    public int findFreeSpaceIndex() {
        int index = -1;
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void giveTheCard(PlayingCard x) {
        int i = findFreeSpaceIndex();
        if (i != -1) {
            hand[i] = x;
        }
    }

    public boolean buffExist(Buff x) {
        boolean check = false;
        if (buffs.isEmpty()) {
            return false;
        }
        for (int i = 0; i < buffs.size(); i++) {
            if (x.getName().equals(buffs.get(i).getName())) {
                check = true;
                break;
            }
        }
        return check;
    }

    public void giveBuff(Buff x) {
        if (buffExist(x)) {
            for (int i = 0; i < buffs.size(); i++) {
                if (x.getName().equals(buffs.get(i).getName())) {
                    buffs.get(i).addStack();
                }
            }
        } else {
            buffs.add(x);
        }
    }

    public int buffStack(Buff x) {
        if (buffs.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < buffs.size(); i++) {
            if (x.getName().equals(buffs.get(i).getName())) {
                return buffs.get(i).getStack();
            }
        }
        return 0;
    }

    public float modifierBuff(ModifierBuff x) {
        float modifier = 1;
        if (buffs.isEmpty()) {
            return 1;
        }
        for (int i = 0; i < buffs.size(); i++) {
            if (x.getName().equals(buffs.get(i).getName())) {
                return x.getModifier();
            }
        }
        return modifier;
    }

    public void buffActionTrigger(String situation) {
        if (buffs.isEmpty()) {
            return;
        }
        for (int i = 0; i < buffs.size(); i++) {
            if (buffs.get(i).triggerBuff(situation)) {
                buffs.get(i).buffAction(this);
            }
        }
    }

    public void decreaseBuff() {
        if (buffs.isEmpty()) {
            return;
        }
        for (int i = 0; i < buffs.size(); i++) {
            if (buffs.get(i).isDecrease()) {
                buffs.get(i).decreaseStack();
                if (buffs.get(i).getStack() == 0) {
                    buffs.remove(i);
                    i--;
                }
            }
        }
    }

    public void takeDamage(int damage) {
        if (shield - damage > 0) {
            shield -= damage;

        } else {
            health -= (damage - shield);// Уменьшение здоровья
            if (health < 0) {
                health = 0; // Убедитесь, что здоровье не становится отрицательным
            }
        }
    }

    public void giveShield(int plusShield) {
        shield += plusShield;
    }

    public void useManaForCard(PlayingCard x) {
        takeScore(x.getCost());
        manaPool -= x.getCost();
    }

    public void healing(int heal) {
        if (maxHealth <= heal + health) {
            health = maxHealth;
        } else {
            health += heal;
        }

    }
    public void changeSight(String direction){
        switch (direction){
            case "left":
                sprite.setTexture(textureLeftSight);
                break;
            case"right":
                sprite.setTexture(textureRightSight);
                break;
        }
    }

    public boolean notRotated() {
        return walkAnimationRotateTimer == 0;
    }

    public void walkAnimation(float delta) {
        walkAnimationTimer += delta * SPEED_WALKING_COMPRESSION;
        walkAnimationCompressionValue = (float) (Math.abs(Math.sin(walkAnimationTimer) * WALKING_COMPRESSION_AMPLITUDE));
        if (walkAnimationTimer >= Math.PI) {
            walkAnimationTimer = 0;
            walkAnimationCompressionValue = 0;
        }
    }

    public void rotateAnimation(float delta) {
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        walkAnimationRotateTimer += delta * SPEED_WALKING_ROTATE;
        float walkAnimationRotateValue = (float) Math.sin(walkAnimationRotateTimer) * WALKING_ROTATE_AMPLITUDE;
        sprite.setRotation(walkAnimationRotateValue);
        if(walkAnimationRotateTimer >= 2*Math.PI){
            walkAnimationRotateTimer = 0;
            walkAnimationRotateValue = (float) Math.sin(walkAnimationRotateTimer) * WALKING_ROTATE_AMPLITUDE;
            sprite.setRotation(walkAnimationRotateValue);
        }
    }

    public boolean inStatic() {
        return walkAnimationCompressionValue == 0;
    }

    public void drawMap(SpriteBatch batch) {
        sprite.setX(xOnScreen);
        sprite.setY(yOnScreen);
        sprite.setSize(textureRightSight.getWidth() * CHARACTER_SCALE_ON_MAP, textureRightSight.getHeight() * CHARACTER_SCALE_ON_MAP - walkAnimationCompressionValue);
        sprite.draw(batch);
    }

    public void draftDeckClear() {
        draftDeck.clear();
    }


    public void dropDeckClear() {
        dropDeck.clear();
    }

    public void handClear() {
        Arrays.fill(hand, null);
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public float getPercentageOfHealthPoints() {
        return (float) health / maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}

