package puppeteer.actions.generic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class IncreaseDrawnCostAction extends AbstractGameAction {
    private DrawAndSaveCardsAction cardSource;

    public IncreaseDrawnCostAction(DrawAndSaveCardsAction d, int amount)
    {
        this.cardSource = d;
        this.amount = amount;
    }

    @Override
    public void update() {
        for (AbstractCard c : cardSource.getDrawnCards())
        {
            if (canIncrease(c)) {
                c.costForTurn += this.amount;
                c.isCostModified = c.costForTurn != c.cost;
                c.superFlash(Color.SLATE);
            }
        }
        this.isDone = true;
    }

    private boolean canIncrease(AbstractCard c) {
        return c.costForTurn >= 0;
    }
}
