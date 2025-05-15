package io.github.some_example_name.enemy_classes.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.buffs.Buff;
import io.github.some_example_name.buffs.modifier_buffs.ModifierBuff;
import io.github.some_example_name.enemy_classes.enemy_moves.MoveEnemy;
import io.github.some_example_name.player.Player;

public abstract class Enemy {
    protected static final Texture heart = new Texture(Gdx.files.internal("HUD/heart.png"));
    protected Texture texture; // Текстура противника
    protected Rectangle bounds; // Границы для проверки коллизий
    protected Animation<TextureRegion> animation; // Анимация для противника
    protected int health; // Здоровье противника

    protected List<Buff> buffs = new ArrayList<>();

    protected MoveEnemy[] moveList;
    protected float stateTime;// Время для анимации
    protected int indexMoveList;


    protected Sound takingDamageSoundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/takingDamageGamblerSoundEffect.wav"));

    public MoveEnemy[] getMoveList() {
        return moveList;
    }

    public int getIndexMoveList() {
        return indexMoveList;
    }


    public void draw(SpriteBatch batch, BitmapFont font, float elapsedTime, Player player) {
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame,
            bounds.x,
            bounds.y,
            bounds.width,
            bounds.height);// Получаем текущий кадр анимации

        font.draw(batch, "Health: " + health, bounds.getX(), bounds.getY() + bounds.getHeight() + 100);
        // кто читает эту заметку тот лох


        moveList[getIndexMoveList()].draw(batch, font, elapsedTime, this, player);


    }


    public boolean isAlive() {
        return health > 0; // Проверка, жив ли противник
    }

    public Rectangle getBounds() {
        return bounds; // Возврат границ
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Освобождение текстуры
        }
    }

    public void identifyIndexMoveList() { // определение действия моба
        indexMoveList = (int) (Math.random() * moveList.length);
    }

    public void takeDamage(int damage) {
        health -= damage; // Уменьшение здоровья
        takingDamageSoundEffect.play(0.7f);
        buffActionTrigger("TakeDamage");
        if (health <= 0) {
            health = 0; // Убедитесь, что здоровье не становится отрицательным
        }
    }

    public void endTurn(Player y) {
        identifyIndexMoveList();
        buffActionTrigger("EndTurn");
        decreaseBuff();
        moveList[indexMoveList].enemyAction(this, y);// Противник наносит урон игроку
    }


    public boolean buffExist(Buff x) {
        if (buffs.isEmpty()) {
            return false;
        }
        boolean check = false;
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
        if (buffs.isEmpty()) {
            return 1;
        }
        for (int i = 0; i < buffs.size(); i++) {
            if (x.getName().equals(buffs.get(i).getName())) {
                return x.getModifier();
            }
        }
        return 1;
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
}

