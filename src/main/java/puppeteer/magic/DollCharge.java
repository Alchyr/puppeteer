package puppeteer.magic;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractMagic;
import puppeteer.util.HitboxHelper;

import java.util.ArrayList;

public class DollCharge extends AbstractMagic {
    @Override
    public void renderArea(float startX, float startY, float targetX, float targetY) {
        sr.line(startX, startY, targetX, targetY);
    }

    @Override
    public VFXAction getVfx()
    {
        return null;
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
        return new DollCharge();
    }
}