package io.github.some_example_name.cards.non_target_cards.buff_cards;

import io.github.some_example_name.buffs.Power;
import io.github.some_example_name.buffs.Reinforce;
import io.github.some_example_name.cards.non_target_cards.NonTargetCard;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class DanceKiller extends NonTargetCard {
    public DanceKiller() {
        name = "Убийца";
        description = "Тип: способность. дает одну силу и одно укрепление";
        cost = 1;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveBuff(new Power());
        y.giveBuff(new Reinforce());

    }
}
