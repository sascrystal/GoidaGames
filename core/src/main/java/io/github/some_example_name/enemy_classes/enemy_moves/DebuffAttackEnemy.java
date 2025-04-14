package io.github.some_example_name.enemy_classes.enemy_moves;

import io.github.some_example_name.buffs.Buff;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class DebuffAttackEnemy extends AttackEnemy {
    protected Buff debuff;

    public DebuffAttackEnemy(int damage, Buff buff) {
        super(damage);
        this.debuff = buff;
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        super.enemyAction(x, y);
        y.giveBuff(debuff);
    }
}
