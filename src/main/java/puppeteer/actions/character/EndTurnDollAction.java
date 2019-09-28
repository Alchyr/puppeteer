package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.abstracts.AbstractDoll;

public class EndTurnDollAction extends AbstractGameAction {
    private AbstractDoll d;

    public EndTurnDollAction(AbstractDoll d)
    {
        this.d = d;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToTop(new ModifyChargeAction(d, -1));
        d.onEndTurn();
        this.isDone = true;
    }
}
