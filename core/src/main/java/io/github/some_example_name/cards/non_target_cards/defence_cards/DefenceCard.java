package io.github.some_example_name.cards.non_target_cards.defence_cards;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.cards.non_target_cards.NonTargetCard;

abstract public class DefenceCard extends NonTargetCard {
    protected int shield;
    protected int totalShield;

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveShield(totalShield);

    }
}
