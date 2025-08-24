package io.github.some_example_name.cards.target_cards.attack_cards;

import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class AttackVoid extends CardAttack {

    public AttackVoid() {
        name = "Войд";
        texture = new Texture("cards/cardVoidAttack.png");
        description = "Тип: атака. Наносит 8x2 урона и восстанавливает нанесенный урон";
        cost = 2;
        damage = 8;

    }

    @Override
    public int totalDamageCalculation(Player y) {
        return super.totalDamageCalculation(y) * 2;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.healing(totalDamage);

    }
}
