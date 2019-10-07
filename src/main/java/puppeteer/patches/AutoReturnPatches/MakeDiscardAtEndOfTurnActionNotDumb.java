package puppeteer.patches.AutoReturnPatches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.actions.generic.EndTurnDiscardAction;

import java.lang.reflect.Field;

import static puppeteer.PuppeteerMod.logger;

@SpirePatch(
        clz = DiscardAtEndOfTurnAction.class,
        method = "update"
)
public class MakeDiscardAtEndOfTurnActionNotDumb {
    private static Field isEndTurn;

    static {
        try {
            isEndTurn = DiscardAction.class.getDeclaredField("endTurn");
            isEndTurn.setAccessible(true);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @SpirePostfixPatch
    public static void NODUMBDISCARDSTUPIDBIGDUMBIDIOT(DiscardAtEndOfTurnAction __instance) throws IllegalAccessException {
        if (__instance.isDone)
        {
            if (AbstractDungeon.actionManager.actions.removeIf(a -> {
                try {
                    return (a instanceof DiscardAction && (boolean)isEndTurn.get(a));
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage());
                    return false;
                }
            }))
            {
                int index = 0;
                for (; index < AbstractDungeon.actionManager.actions.size(); ++index)
                {
                    if (AbstractDungeon.actionManager.actions.get(index) instanceof RestoreRetainedCardsAction)
                    {
                        break;
                    }
                }

                AbstractDungeon.actionManager.actions.add(index, new EndTurnDiscardAction());
            }
        }
    }
}
