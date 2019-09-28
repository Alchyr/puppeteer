package puppeteer.abstracts;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.PuppeteerMod;
import puppeteer.fields.CardDollTargetField;
import puppeteer.fields.DollFields;
import puppeteer.util.CardInfo;

import java.util.ArrayList;

import static puppeteer.PuppeteerMod.makeID;

public abstract class AbstractDollTargetCard extends BaseCard {
    private static UIStrings dollCardText = CardCrawlGame.languagePack.getUIString(makeID("DollCard"));

    public AbstractDollTargetCard(CardInfo cardInfo, boolean upgradesDescription)
    {
        super(cardInfo, upgradesDescription);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (DollFields.dolls.get(p).isEmpty())
        {
            this.cantUseMessage = dollCardText.TEXT[0];
            return false;
        }
        return super.canUse(p, m);
    }

    protected AbstractDoll getTargetDoll()
    {
        AbstractDoll d = CardDollTargetField.targetDoll.get(this);
        ArrayList<AbstractDoll> dolls = DollFields.dolls.get(AbstractDungeon.player);

        if (d == null || !dolls.contains(d))
        {
            if (dolls.isEmpty())
            {
                PuppeteerMod.logger.error("wtf using doll target card with no dolls?");
                return null;
            }
            else if (dolls.size() == 1)
            {
                return dolls.get(0);
            }
            return dolls.get(AbstractDungeon.cardRandomRng.random(0, dolls.size() - 1));
        }
        return d;
    }
    protected AbstractDoll getDirectHoverDoll()
    {
        AbstractDoll d = CardDollTargetField.targetDoll.get(this);
        ArrayList<AbstractDoll> dolls = DollFields.dolls.get(AbstractDungeon.player);

        if (d == null || !dolls.contains(d))
        {
            if (dolls.isEmpty())
            {
                PuppeteerMod.logger.error("wtf using doll target card with no dolls?");
            }
            return null;
        }
        return d;
    }
}
