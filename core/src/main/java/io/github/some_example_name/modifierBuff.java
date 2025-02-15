package io.github.some_example_name;

public class modifierBuff extends Buff{
    float modifier = 0.25f;
}

class Weakness extends  modifierBuff{
    public Weakness() {
        name ="Печеньковое безумие";
        description = "Дает за каждый стак в начале хода дает дополнительную карту печенек";
        decrease = true;
        stack = 1;
        modifier = 0.75f;
    }
}
