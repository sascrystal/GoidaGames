package io.github.some_example_name.buffs;

import io.github.some_example_name.player.Player;

public class ExtraMana extends  Buff {
    public ExtraMana(int stack) {
        name = "Дополнительная мана";
        description = "За каждый стак дает доп 1 ману";
        this.stack = stack;
        decrease = false;
        inBeginTurn = true;

    }

    public ExtraMana() {
        name = "Дополнительная мана";
        description = "За каждый стак дает доп 1 ману";
        stack = 1;
        decrease = false;
        inBeginTurn = true;
    }

    @Override
    public void buffAction(Player player) {
        player.addMana(stack);
    }
}
