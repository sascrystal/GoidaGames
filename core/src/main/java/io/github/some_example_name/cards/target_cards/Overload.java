package io.github.some_example_name.cards.target_cards;

import io.github.some_example_name.buffs.modifier_buffs.OverloadBuff;
import io.github.some_example_name.enemy_classes.enemies.Enemy;

import io.github.some_example_name.player.Player;

public class Overload extends TargetCard {

    public Overload() {
        name = "Перегрузка";
        description = "Накладывает дебафф <<Перегрузка>>";
        cost = 3;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        x.giveBuff(new OverloadBuff());
    }
}
