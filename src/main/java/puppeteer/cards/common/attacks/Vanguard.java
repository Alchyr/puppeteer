package puppeteer.cards.common.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.character.BuffClosestDollAction;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Vanguard extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Vanguard",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private final static int DAMAGE = 8;
    private final static int UPG_DAMAGE = 1;

    private final static int BUFF = 1;
    private final static int UPG_BUFF = 1;

    public Vanguard()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(BUFF, UPG_BUFF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        addToBot(new BuffClosestDollAction(m, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Vanguard();
    }
}
