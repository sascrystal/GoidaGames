package io.github.some_example_name;

public class MoveEnemy {
    public void enemyAction(Enemy x, Player y){}
    public void buff(int x){}
    public String showNumericalValue(){
        return "ЛОХ";
    }

}
class AttackEnemy extends MoveEnemy{
    protected int damage;

    public AttackEnemy(int damage) {
        this.damage = damage;
    }

    @Override
    public void enemyAction(Enemy x, Player y){
        y.takeDamage(damage);
    }


    @Override
    public void buff(int x){
        damage+= x;
    }

    @Override
    public String showNumericalValue(){
        return String.valueOf(damage);
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

    @Override
    public void enemyAction(Enemy x, Player y) {
        y.takeDamage(damage*(int)(Math.pow(2,x.uniqueBuff[ProgrammerBuff.getIndex()].stack)));
    }


}



