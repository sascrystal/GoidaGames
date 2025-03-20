package io.github.some_example_name;

public class ModifierBuff extends Buff{
    float modifier = 0.75f;
}

class Weakness extends  ModifierBuff{
    public Weakness(int stack) {
        name ="Печеньковое безумие";
        description = "Дает за каждый стак в начале хода дает дополнительную карту печенек";
        decrease = true;
        this.stack = stack;
        modifier = 0.75f;
    }
    public Weakness() {
        name ="Печеньковое безумие";
        description = "Дает за каждый стак в начале хода дает дополнительную карту печенек";
        decrease = true;
        stack = 1;
        modifier = 0.75f;
    }
}
class OverloadBuff extends ModifierBuff {
    public OverloadBuff() {
        name = "Перегрузка";
        description = "Убирает возможность наносить урон";
        decrease = true;
        stack = 1;
        modifier = 0f;
    }
}

