package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
        this.isDone = true;
    }
}
