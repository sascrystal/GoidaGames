package io.github.some_example_name.cards.non_target_cards.defence_cards;


import io.github.some_example_name.buffs.BonusCard;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class Evade extends DefenceCard {
    public Evade() {
        name = "Уворот";
        description = "Тип: способность. Дает 8 защиты, 1 карту из колоды и 1 карту в следующем ходу";
        shield = 8;
        cost = 1;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.takeCardsFromDraftDeck(1);
        y.giveBuff(new BonusCard(1));
    }
}
