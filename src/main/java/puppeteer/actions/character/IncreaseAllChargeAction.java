package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.fields.DollFields;

public class IncreaseAllChargeAction extends AbstractGameAction {
    private AbstractDoll doll;

    public IncreaseAllChargeAction(int amt)
    {
        doll = null;
        this.amount = amt;
    }
    public IncreaseAllChargeAction(AbstractDoll source)
    {
        this.doll = source;
    }

    @Override
    public void update() {
        if (this.doll != null)
            this.amount = this.doll.charge;

        for (AbstractDoll d : DollFields.dolls.get(AbstractDungeon.player))
        {
            d.charge += this.amount;
        }

        this.isDone = true;
    }
}
