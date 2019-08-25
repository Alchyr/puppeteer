package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.fields.DollFields;

public class EndTurnDollsAction extends AbstractGameAction {
    public EndTurnDollsAction()
    {

    }

    @Override
    public void update() {
        for (AbstractDoll d : DollFields.dolls.get(AbstractDungeon.player))
        {
            d.onEndTurn();
        }
        this.isDone = true;
    }
}
