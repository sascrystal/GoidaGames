package io.github.some_example_name.cards.non_target_cards.defence_cards;

import io.github.some_example_name.buffs.Reinforce;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public class CupDefense extends DefenceCard {
    public CupDefense() {
        name = "Бабушкин сервиз";
        description = "Тип: способность. Дает 8 защиты. Хороший фарфор - хорошая защита!";
        shield = 8;
        cost = 1;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalShield = shield + y.buffStack(new Reinforce());
        super.cardAction(x, y, index);
    }
}
