package puppeteer.fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import puppeteer.abstracts.AbstractDoll;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class CardDollTargetField {
    public static SpireField<AbstractDoll> targetDoll = new SpireField<>(()->null);
}
