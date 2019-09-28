package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.abstracts.AbstractDoll;

public class BlockChargeAction extends AbstractGameAction {
    private AbstractDoll d;

    public BlockChargeAction(AbstractDoll d)
    {
        this.d = d;
        this.actionType = ActionType.BLOCK;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, d.charge));

        this.isDone = true;
    }
}
