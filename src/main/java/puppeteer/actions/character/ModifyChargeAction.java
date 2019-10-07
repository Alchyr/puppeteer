package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.abstracts.AbstractDoll;

public class ModifyChargeAction extends AbstractGameAction {
    private AbstractDoll d;

    public ModifyChargeAction(AbstractDoll d, int amt)
    {
        this.d = d;
        this.amount = amt;
    }

    @Override
    public void update() {
        d.charge += this.amount;

        if (d.charge <= 0)
        {
            d.charge = 0;
            AbstractDungeon.actionManager.addToTop(new DestroyDollAction(d, false));
        }
        this.isDone = true;
    }
}
