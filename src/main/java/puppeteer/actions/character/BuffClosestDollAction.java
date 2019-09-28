package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.util.HitboxHelper;

public class BuffClosestDollAction extends AbstractGameAction {
    public BuffClosestDollAction(AbstractMonster target, int amt)
    {
        setValues(target, AbstractDungeon.player, amt);
    }

    @Override
    public void update() {
        if (target != null)
        {
            AbstractDoll d = HitboxHelper.getClosestDoll(target.hb.cX, target.hb.cY);
            if (d != null)
            {
                d.charge += this.amount;
            }
        }
        this.isDone = true;
    }
}
