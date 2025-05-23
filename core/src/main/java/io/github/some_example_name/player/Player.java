package io.github.some_example_name.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.some_example_name.buffs.Buff;
import io.github.some_example_name.buffs.modifier_buffs.ModifierBuff;
import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.target_cards.attack_cards.ComboAttack;
import io.github.some_example_name.cell_map_classes.events.DialogEvent;
import io.github.some_example_name.cell_map_classes.stage.Stage;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.screens.GameScreen;

public abstract class Player {
    protected List<Buff> buffs = new ArrayList<>();
    protected int cellX, cellY;
    protected int health, maxHealth;
    protected int draftCount;
    protected int manaPool;
    protected int manaPoolMax;
    protected int score;
    protected int shield;// Здоровье игрока
    protected Texture texture = new Texture(Gdx.files.internal("enemies/enemy_1.png"));

    protected List<PlayingCard> dropDeck = new ArrayList<>();
    protected List<PlayingCard> deck = new ArrayList<>();
    protected List<PlayingCard> draftDeck = new ArrayList<>();

    protected PlayingCard[] hand = new PlayingCard[GameScreen.HAND_META];

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

    public void drawMap(SpriteBatch batch, Rectangle cell) {
        batch.draw(texture, cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
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

