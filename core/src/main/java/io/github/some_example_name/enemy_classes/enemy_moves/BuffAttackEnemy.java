package io.github.some_example_name.enemy_classes.enemy_moves;

import io.github.some_example_name.buffs.Buff;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class BuffAttackEnemy extends AttackEnemy {
    protected Buff buff;

    public BuffAttackEnemy(int damage, Buff buff) {
        super(damage);
        this.buff = buff;
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        super.enemyAction(x, y);
        x.giveBuff(buff);
    }
}
