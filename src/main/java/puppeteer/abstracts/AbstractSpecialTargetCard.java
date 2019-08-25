package puppeteer.abstracts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.util.CardInfo;

public abstract class AbstractSpecialTargetCard extends BaseCard {
    public AbstractSpecialTargetCard(CardInfo cardInfo, boolean upgradesDescription)
    {
        super(cardInfo, upgradesDescription);
    }

    @Override
    public void renderHoverShadow(SpriteBatch sb) {
        //no.
    }

    @Override
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.player != null &&
                ((AbstractDungeon.player.isDraggingCard &&
                AbstractDungeon.player.isHoveringDropZone &&
                        this.equals(AbstractDungeon.player.hoveredCard)) ||
                        (AbstractDungeon.actionManager != null &&
                                !AbstractDungeon.actionManager.cardQueue.isEmpty() &&
                                this.equals(AbstractDungeon.actionManager.cardQueue.get(0)))))
        {
            specialRender(sb);
            return;
        }
        super.render(sb);
    }

    public abstract void specialRender(SpriteBatch sb);
}
