package io.github.some_example_name;

abstract class MoveEnemy {
    public void enemyAction(Enemy x, Player y){}
    public void buff(int x){}
    abstract  String showNumericalValue(Enemy x, Player y);

}
class AttackEnemy extends MoveEnemy{
    protected int damage;

    public AttackEnemy(int damage) {
        this.damage = damage;
    }

    @Override
    public void enemyAction(Enemy x, Player y){
        int totalDamage = (int)((damage +x.buffStack(new Power()))*x.modifierBuff(new Weakness()));
        y.takeDamage(totalDamage);
    }




    @Override
    public String showNumericalValue(Enemy x, Player y){
        int totalDamage = (int)((damage +x.buffStack(new Power()))*x.modifierBuff(new Weakness()));
        return String.valueOf(totalDamage);
    }

}

class SelfHarm extends AttackEnemy{
    public SelfHarm(int damage) {
        super(damage);
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        x.takeDamage(damage);
    }
}

class AttackProgrammer extends AttackEnemy{
    public AttackProgrammer(int damage) {
        super(damage);
    }

    //@Override
    //public void enemyAction(Enemy x, Player y) {
        //y.takeDamage(damage*(int)(Math.pow(2,x.uniqueBuff[ProgrammerBuff.getIndex()].stack)));
    //}


}



