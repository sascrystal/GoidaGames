package io.github.some_example_name.enemy_classes.enemy_moves;

import io.github.some_example_name.buffs.Buff;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class BuffMove extends MoveEnemy {
    protected Buff buff;

    public BuffMove(Buff buff) {
        this.buff = buff;
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        x.giveBuff(buff);
    }

    @Override
    public String showNumericalValue(Enemy x, Player y) {
        return "";
    }
}
