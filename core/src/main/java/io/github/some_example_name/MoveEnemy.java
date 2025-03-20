package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class MoveEnemy {
    protected TextureAtlas frames = new TextureAtlas(Gdx.files.internal("enemyMove/questionMark.atlas"));
    protected Animation<TextureRegion> animation = new Animation<>(1/3F,
        frames.findRegions("questionMark"), Animation.PlayMode.LOOP);



    abstract void enemyAction(Enemy x, Player y);
    abstract  String showNumericalValue(Enemy x, Player y);


    public void draw(Batch batch, BitmapFont font, float elapsedTime, Enemy enemy,Player player){

        font.draw(batch, String.valueOf(showNumericalValue(enemy,player)),
            (enemy.getBounds().getX()+enemy.getBounds().getWidth()+50),
            enemy.getBounds().getY() + enemy.getBounds().getHeight()+100);

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame,
            enemy.getBounds().getX() + enemy.getBounds().getWidth() - 40,
            enemy.getBounds().getY() + enemy.getBounds().getHeight() + 30,
            100,130);
    }
}

class AttackEnemy extends MoveEnemy  {
    protected int damage;

    public AttackEnemy(int damage) {
        this.damage = damage;
    }

    @Override
    public void enemyAction(Enemy x, Player y){
        int totalDamage = (int)((damage +x.buffStack(new Power()))*x.modifierBuff(new Weakness())*x.modifierBuff(new OverloadBuff()));
        y.takeDamage(totalDamage);
    }

  @Override
    public String showNumericalValue(Enemy x, Player y){
        int totalDamage = (int)((damage +x.buffStack(new Power()))*x.modifierBuff(new Weakness())*x.modifierBuff(new OverloadBuff()));
        return String.valueOf(totalDamage);
    }
}

class DebuffAttackEnemy extends AttackEnemy {
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

class BuffAttackEnemy extends AttackEnemy {
    protected Buff buff;

    public BuffAttackEnemy(int damage, Buff buff) {
        super(damage);
        this.buff = buff;
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        super.enemyAction(x, y);
        x.giveBuff(buff);
    }
}

class SelfHarm extends AttackEnemy{
    public SelfHarm(int damage) {
        super(damage);
    }

    @Override
    public void enemyAction(Enemy x, Player y) {
        int totalDamage = (int)((damage +x.buffStack(new Power()))*x.modifierBuff(new Weakness())*x.modifierBuff(new OverloadBuff()));
        x.takeDamage(totalDamage);
    }
}

class AttackProgrammer extends AttackEnemy{
    public AttackProgrammer(int damage) {
        super(damage);
    }
}

class DebuffMove extends MoveEnemy{
    protected Buff debuff;

    public DebuffMove(Buff debuff) {
        this.debuff = debuff;
    }

    @Override
    void enemyAction(Enemy x, Player y) {
        y.giveBuff(debuff);
    }

    @Override
    String showNumericalValue(Enemy x, Player y) {
        return "";
    }
}

class BuffMove extends MoveEnemy{
    protected Buff buff;

    public BuffMove(Buff buff) {
        this.buff = buff;
    }

    @Override
    void enemyAction(Enemy x, Player y) {
        x.giveBuff(buff);
    }

    @Override
    String showNumericalValue(Enemy x, Player y) {
        return "";
    }
}



