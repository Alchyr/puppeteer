package puppeteer.cards.common.skills;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.abstracts.AbstractDollTargetCard;
import puppeteer.actions.character.BlockChargeAction;
import puppeteer.actions.character.DestroyDollAction;
import puppeteer.enums.CardTargetEnums;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Deanimate extends AbstractDollTargetCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Deanimate",
            0,
            CardType.SKILL,
            CardTargetEnums.DOLL,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    public Deanimate()
    {
        super(cardInfo, true);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDoll d = getTargetDoll();

        if (d != null)
        {
            AbstractDungeon.actionManager.addToBottom(new DestroyDollAction(d));
            AbstractDungeon.actionManager.addToBottom(new BlockChargeAction(d));
        }
    }
}