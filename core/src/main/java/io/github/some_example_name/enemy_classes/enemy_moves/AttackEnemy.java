package io.github.some_example_name.enemy_classes.enemy_moves;


import io.github.some_example_name.buffs.Power;
import io.github.some_example_name.buffs.modifier_buffs.OverloadBuff;
import io.github.some_example_name.buffs.modifier_buffs.Weakness;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class AttackEnemy extends MoveEnemy {
    protected int damage;

    public AttackEnemy(int damage) {
        this.damage = damage;
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        int totalDamage = (int) ((damage + x.buffStack(new Power())) * x.modifierBuff(new Weakness()) * x.modifierBuff(new OverloadBuff()));
        y.takeDamage(totalDamage);
    }

    @Override
    public String showNumericalValue(Enemy x, Player y) {
        int totalDamage = (int) ((damage + x.buffStack(new Power())) * x.modifierBuff(new Weakness()) * x.modifierBuff(new OverloadBuff()));
        return String.valueOf(totalDamage);
    }
}
