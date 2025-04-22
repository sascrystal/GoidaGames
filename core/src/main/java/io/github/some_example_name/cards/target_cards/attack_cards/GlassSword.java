package io.github.some_example_name.cards.target_cards.attack_cards;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class GlassSword extends  Attack{
    public GlassSword() {
        name ="Стеклянный меч";
        description = "Наносит 17 урона, сжигается";
        damage = 17;
        cost = 1;
        burnable = true;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalDamage = totalDamageCalculation(y);
        super.cardAction(x, y, index);
    }
}
