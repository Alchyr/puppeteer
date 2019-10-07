package puppeteer.cards.uncommon.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.ReinforcedBody;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.character.SolitaryAction;
import puppeteer.actions.generic.PerformXAction;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Solitary extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Solitary",
            -1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 9;
    private static final int UPG_BLOCK = 3;

    private static final int MAGIC = 1;

    public Solitary() {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PerformXAction(new SolitaryAction(p, this.block), p, this.energyOnUse, this.freeToPlayOnce));
    }

    public AbstractCard makeCopy() {
        return new Solitary();
    }
}