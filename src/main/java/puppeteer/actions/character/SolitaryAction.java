package puppeteer.actions.character;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.abstracts.AbstractXAction;
import puppeteer.actions.generic.IncreaseCostAction;

public class SolitaryAction extends AbstractXAction {
    private int blockPerEnergy;

    public SolitaryAction(AbstractPlayer p, int block)
    {
        this.source = p;
        this.blockPerEnergy = block;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToTop(new IncreaseCostAction(Integer.MAX_VALUE, 1));

        if (this.amount > 0) {
            for(int i = 0; i < this.amount; ++i) {
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.source, this.blockPerEnergy));
            }
        }

        this.isDone = true;
    }
}
