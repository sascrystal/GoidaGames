package io.github.some_example_name.enemy_classes.enemy_moves;

import io.github.some_example_name.buffs.Buff;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class DebuffMove extends MoveEnemy {
    protected Buff debuff;

    public DebuffMove(Buff debuff) {
        this.debuff = debuff;
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        y.giveBuff(debuff);
    }

    @Override
    public String showNumericalValue(Enemy x, Player y) {
        return "";
    }
}
