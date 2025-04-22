package io.github.some_example_name.cards.non_target_cards.defence_cards;

import io.github.some_example_name.buffs.Reinforce;
import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class GlassShield  extends DefenceCard {
    public GlassShield() {
        name = "Стеклянный щит";
        description = "Дет 12 защиты, сжигается";
        burnable = true;
        cost = 1;
        shield = 12;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalShield = shield + y.buffStack(new Reinforce());
        super.cardAction(x, y, index);
    }
}
