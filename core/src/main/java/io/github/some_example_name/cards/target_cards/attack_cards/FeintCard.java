package io.github.some_example_name.cards.target_cards.attack_cards;

import io.github.some_example_name.buffs.modifier_buffs.Weakness;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public class FeintCard extends CardAttack {
    protected int weaknessStack;

    public FeintCard() {
        name = "Финт";
        description = "Тип: атака. Наносит 8 урона и накладывает 2 слабости";
        cost = 1;
        damage = 8;
        weaknessStack = 2;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalDamage = totalDamageCalculation(y);
        super.cardAction(x, y, index);
        x.giveBuff(new Weakness(2));
    }
}
