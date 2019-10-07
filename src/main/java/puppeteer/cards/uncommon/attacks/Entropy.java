package puppeteer.cards.uncommon.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.generic.IncreaseCostAction;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Entropy extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Entropy",
            2,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 18;
    private static final int UPG_DAMAGE = 3;

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public Entropy() {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
        applySingle(m, getVuln(m, this.magicNumber));

        addToBot(new IncreaseCostAction(1, 1));
    }

    public AbstractCard makeCopy() {
        return new Entropy();
    }
}