package puppeteer.patches.AutoReturnPatches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import puppeteer.fields.AutoReturnField;
import puppeteer.interfaces.OnReturnToHandCard;

import java.lang.reflect.Field;

import static puppeteer.PuppeteerMod.logger;

@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class AutoReturnOnUsePatch {
    private static Field actionCard;
    private static Field duration;

    static {
        try {
            actionCard = UseCardAction.class.getDeclaredField("targetCard");
            actionCard.setAccessible(true);

            duration = AbstractGameAction.class.getDeclaredField("duration");
            duration.setAccessible(true);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }


    @SpireInsertPatch(
            locator = ReturnLocator.class
    )
    public static SpireReturn returnToHand(UseCardAction __instance) throws IllegalAccessException {
        AbstractCard c = (AbstractCard) actionCard.get(__instance);

        if (AutoReturnField.autoReturn.get(c) && AbstractDungeon.player.hand.contains(c))
        {
            duration.set(__instance, (float)duration.get(__instance) - Gdx.graphics.getDeltaTime());

            if (c instanceof OnReturnToHandCard)
            {
                ((OnReturnToHandCard) c).onReturnToHand();
            }

            AbstractDungeon.player.hand.applyPowers();
            AbstractDungeon.player.hand.glowCheck();

            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static class ReturnLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "purgeOnUse");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
