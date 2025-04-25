package io.github.some_example_name.cards.target_cards.attack_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class ComboAttack extends CardAttack {
    private static boolean condition;

    static {
        condition = false;
    }

    public static void setCondition(boolean condition) {
        ComboAttack.condition = condition;
    }

    public ComboAttack() {
        name = "Комбо атака";
        description = "Тип: атака. Наносит 7 урона, если использовано в одному ходу 2 карты этого типа, то получаешь в руку бесплатную эфирную карту комбо атаки";
        texture = new Texture(Gdx.files.internal("cards/cardComboAttack.png"));
        damage = 7;
        cost = 1;
    }

    public ComboAttack(int cost) {
        name = "Комбо атака";
        description = "Тип: атака. Наносит 7 урона, если использовано в одному ходу 2 карты этого типа, то получаешь в руку бесплатную эфирную карту комбо атаки эфирная";
        texture = new Texture(Gdx.files.internal("cards/cardComboAttack.png"));
        damage = 7;
        this.cost = cost;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        if (condition) {
            y.giveTheCard(new ComboAttack(0));
            condition = false;
        } else {
            condition = true;
        }
        totalDamage = totalDamageCalculation(y);
        super.cardAction(x, y, index);

    }
}
