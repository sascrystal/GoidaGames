package io.github.some_example_name.cards.target_cards.attack_cards;

import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.buffs.modifier_buffs.Weakness;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public class SugarSplash extends CardAttack {
    public SugarSplash() {
        name = "Сахарный взрыв";
        texture = new Texture("cards/cardSugarSplash.png");
        description = "Тип: атака. Наносит 5 урона и добавляет 1 слабость.";
        damage = 5;
        cost = 0;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        x.giveBuff(new Weakness(1));
    }
}
