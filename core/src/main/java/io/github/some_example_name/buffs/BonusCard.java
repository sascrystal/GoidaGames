package io.github.some_example_name.buffs;

import io.github.some_example_name.player.Player;

public class BonusCard extends Buff {

    public BonusCard(int stack) {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе";
        this.stack = stack;
        decrease = true;
        inBeginTurn = true;

    }

    public BonusCard() {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе";
        stack = 1;
        decrease = true;
        inBeginTurn = true;
    }

    @Override
    public void buffAction(Player player) {
        player.takeCardsFromDraftDeck(stack);
    }

    @Override
    public void decreaseStack() {
        stack = 0;
    }
}
