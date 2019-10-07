package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import puppeteer.abstracts.AbstractDoll;

public class TriggerDollPassiveAction extends AbstractGameAction {
    private AbstractDoll d;

    public TriggerDollPassiveAction(AbstractDoll d)
    {
        this.d = d;
    }

    @Override
    public void update() {
        d.onEndTurn();
        this.isDone = true;
    }
}
