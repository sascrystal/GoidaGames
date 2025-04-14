package io.github.some_example_name.cards.non_target_cards.defence_cards;

import io.github.some_example_name.buffs.Reinforce;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public class Inheritance extends DefenceCard {
    public Inheritance() {
        name = "Наследство";
        description = "Тип: способность. Дает 5 защиты и 1 укрепление. Махорка перемешанная с чаем " +
            "- по дедушкиному рецепту";
        shield = 5;
        cost = 1;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveBuff(new Reinforce()); //????
    }
}
