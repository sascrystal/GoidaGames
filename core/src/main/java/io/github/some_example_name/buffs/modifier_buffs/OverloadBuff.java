package io.github.some_example_name.buffs.modifier_buffs;

public class OverloadBuff extends ModifierBuff {
    public OverloadBuff() {
        name = "Перегрузка";
        description = "Убирает возможность наносить урон";
        decrease = true;
        stack = 1;
        modifier = 0f;
    }
}
