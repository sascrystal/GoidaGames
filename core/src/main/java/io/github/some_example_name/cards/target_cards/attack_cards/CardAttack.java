package io.github.some_example_name.cards.target_cards.attack_cards;

import io.github.some_example_name.buffs.Power;
import io.github.some_example_name.buffs.modifier_buffs.Weakness;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.cards.target_cards.TargetCard;

public abstract class CardAttack extends TargetCard {
    protected int damage;
    protected int totalDamage;

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        totalDamage = totalDamageCalculation(y);
        x.takeDamage(totalDamage);
    }

    public int totalDamageCalculation(Player y) {
        return (int) ((damage + y.buffStack(new Power())) * y.modifierBuff(new Weakness()));

    }

}
