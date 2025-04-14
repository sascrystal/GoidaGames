package io.github.some_example_name.buffs;

import io.github.some_example_name.enemy_classes.enemies.Enemy;

public class MentalDamage extends Buff {

    public MentalDamage() {
        name = "Ментальный урон";
        description = "В начале хода наносит за каждый стак";
        stack = 4;

        decrease = false;
        inEndTurn = true;
    }

    @Override
    public void buffAction(Enemy owner) {
        owner.takeDamage(stack);
    }
}
