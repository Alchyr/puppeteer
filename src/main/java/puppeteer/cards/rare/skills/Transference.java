package puppeteer.cards.rare.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.abstracts.AbstractDollTargetCard;
import puppeteer.actions.character.DestroyDollAction;
import puppeteer.actions.character.IncreaseAllChargeAction;
import puppeteer.enums.CardTargetEnums;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Transference extends AbstractDollTargetCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Transference",
            2,
            CardType.SKILL,
            CardTargetEnums.DOLL,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    public Transference()
    {
        super(cardInfo, true);

        setExhaust(true, false);
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDoll d = getTargetDoll();

        if (d != null)
        {
            AbstractDungeon.actionManager.addToBottom(new DestroyDollAction(d));
            AbstractDungeon.actionManager.addToBottom(new IncreaseAllChargeAction(d));
        }
    }
}