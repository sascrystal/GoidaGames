package io.github.some_example_name.buffs;

import io.github.some_example_name.enemy_classes.enemies.Enemy;

public class HamsterBuff extends Buff {
    public HamsterBuff() {
        name = "Хомячье Безумие";
        description = "Дает 1 силу каждый раз, когда используется карта на него";
        decrease = false;
        inCardAction = true;
        stack = 1;
    }

    @Override
    public void buffAction(Enemy owner) {
        super.buffAction(owner);
        owner.giveBuff(new Power());
    }
}
