package puppeteer.magic;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import puppeteer.abstracts.AbstractMagic;
import puppeteer.effects.RedLaserEffect;
import puppeteer.util.HitboxHelper;

import java.util.ArrayList;

public class HouraiLaser extends AbstractMagic {
    private float originalTargetX = 0;
    private float originalTargetY = 0;

    @Override
    public void setOrigin(float x, float y) {
        super.setOrigin(x, y);
        setTarget(originalTargetX, originalTargetY);
    }

    @Override
    public void setTarget(float x, float y) {
        super.setTarget(x, y);

        originalTargetX = targetX;
        originalTargetY = targetY;

        float xChange = targetX - originX;
        float yChange = targetY - originY;

        while ((targetX > 0 && targetX < Settings.WIDTH) || (targetY > 0 && targetY < Settings.HEIGHT))
        {
            targetX += xChange;
            targetY += yChange;
        }
    }

    @Override
    public void renderArea(float startX, float startY, float targetX, float targetY) {
        sr.line(startX, startY, targetX, targetY);
    }

    @Override
    public VFXAction getVfx()
    {
        return new VFXAction(new RedLaserEffect(this.originX, this.originY, this.targetX, this.targetY));
    }

    @Override
    public ArrayList<AbstractMonster> getTargets()
    {
        ArrayList<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getMonsters().monsters);
        monsters.removeIf(AbstractCreature::isDeadOrEscaped);

        monsters.removeIf((m)->!HitboxHelper.testHitboxLine(originX, originY, targetX, targetY, m.hb));

        return monsters;
    }

    @Override
    public AbstractMagic getCopy() {
        return new HouraiLaser();
    }
}
