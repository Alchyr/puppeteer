package puppeteer.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import javassist.CtBehavior;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.enums.CardTargetEnums;
import puppeteer.fields.CardDollTargetField;
import puppeteer.fields.DollFields;

import java.lang.reflect.Method;
import java.util.Iterator;

import static com.megacrit.cardcrawl.characters.AbstractPlayer.HOVER_CARD_Y_POSITION;
import static puppeteer.PuppeteerMod.logger;

public class DollTargetingPatches {
    private static Method playCard;

    static {
        try {
            playCard = AbstractPlayer.class.getDeclaredMethod("playCard");
            playCard.setAccessible(true);
        }
        catch (Exception e)
        {
            logger.error("Failed to get method playCard of AbstractPlayer.");
            logger.error(e.getMessage());
        }
    } //load playCard
    private static void tryPlayCard(AbstractPlayer p)
    {
        try {
            playCard.invoke(p);
        }
        catch (Exception e)
        {
            logger.error("Failed to invoke method playCard of AbstractPlayer.");
            logger.error(e.getMessage());
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "clickAndDragCards"
    )
    public static class DollUseTargetArrow {
        @SpireInsertPatch(
                locator = NeedTargetLocator.class
        )
        public static SpireReturn<Boolean> cantPlayWithoutTarget(AbstractPlayer __instance) //line 1380 (intellij line number)
        {
            if (__instance.hoveredCard.target == CardTargetEnums.DOLL)
            {
                CardCrawlGame.sound.play("CARD_OBTAIN");
                __instance.releaseCard();

                ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "isUsingClickDragControl", false);

                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }

        @SpireInsertPatch(
                locator = UseTargetingLocator.class
        )
        public static SpireReturn<Boolean> targetDoll(AbstractPlayer __instance) //line 1421
        {
            if (__instance.isHoveringDropZone && __instance.hoveredCard.target == CardTargetEnums.DOLL) {
                __instance.inSingleTargetMode = true;
                ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "arrowX", (float) InputHelper.mX);
                ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "arrowY", (float)InputHelper.mY);
                GameCursor.hidden = true;
                __instance.hoveredCard.untip();
                __instance.hand.refreshHandLayout();
                __instance.hoveredCard.target_y = AbstractCard.IMG_HEIGHT * 0.75F / 2.5F;
                __instance.hoveredCard.target_x = (float) Settings.WIDTH / 2.0F;
                __instance.isDraggingCard = false;

                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }

        @SpireInsertPatch(
                locator = NeedTargetLocatorTwo.class
        )
        public static SpireReturn<Boolean> needToTargetDoll(AbstractPlayer __instance) //line 1443
        {
            if (__instance.hoveredCard.target == CardTargetEnums.DOLL) {
                __instance.inSingleTargetMode = true;
                ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "arrowX", (float) InputHelper.mX);
                ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "arrowY", (float)InputHelper.mY);
                GameCursor.hidden = true;
                __instance.hoveredCard.untip();
                __instance.hand.refreshHandLayout();
                __instance.hoveredCard.target_y = AbstractCard.IMG_HEIGHT * 0.75F / 2.5F;
                __instance.hoveredCard.target_x = (float) Settings.WIDTH / 2.0F;
                __instance.isDraggingCard = false;

                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }

        private static class NeedTargetLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isHoveringDropZone");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
        private static class UseTargetingLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isHoveringDropZone");
                return new int[] { LineFinder.findAllInOrder(ctBehavior, finalMatcher)[2] };
            }
        }
        private static class NeedTargetLocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "target");
                return new int[] { LineFinder.findAllInOrder(ctBehavior, finalMatcher)[6] };
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "updateSingleTargetInput"
    )
    public static class YouUseMYMethodNow {
        @SpirePrefixPatch
        public static SpireReturn forDollTargeting(AbstractPlayer __instance)
        {
            if (__instance.hoveredCard != null && __instance.hoveredCard.target == CardTargetEnums.DOLL)
            {
                dollSingleTarget(__instance);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "playCard"
    )
    public static class SetTargetDoll {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void setDollTarget(AbstractPlayer __instance)
        {
            if (__instance.hoveredCard.target == CardTargetEnums.DOLL)
            {
                AbstractDoll d = CardDollTargetField.targetDoll.get(__instance.hoveredCard);
                AbstractDoll newTarget = DollFields.targetDoll.get(__instance);

                if (newTarget != null)
                {
                    d = newTarget;
                }

                if (d != null)
                {
                    if (!DollFields.dolls.get(__instance).contains(d))
                    {
                        //target doll is invalid
                        d = null;
                    }
                }

                CardDollTargetField.targetDoll.set(__instance.hoveredCard, d);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "target");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
    
    
    
    
    private static void dollSingleTarget(AbstractPlayer p)
    {
        if (Settings.isTouchScreen && !((boolean)ReflectionHacks.getPrivate(p, AbstractPlayer.class, "isUsingClickDragControl")) && !InputHelper.isMouseDown) {// 1025
            Gdx.input.setCursorPosition((int) MathUtils.lerp((float)Gdx.input.getX(), (float)Settings.WIDTH / 2.0F, Gdx.graphics.getDeltaTime() * 10.0F), (int)MathUtils.lerp((float)Gdx.input.getY(), (float)Settings.HEIGHT * 1.1F, Gdx.graphics.getDeltaTime() * 4.0F));
        }

        ReflectionHacks.setPrivate(p, AbstractPlayer.class, "hoveredMonster", null);

        AbstractCard cardFromHotkey;
        if (p.isInKeyboardMode) {
            if (InputActionSet.releaseCard.isJustPressed() || CInputActionSet.cancel.isJustPressed()) {
                cardFromHotkey = p.hoveredCard;
                p.inSingleTargetMode = false;

                //p.hoverCardInHand(cardFromHotkey);
                // v the contents of that method, none of which are private. Unlike the method.
                p.toHover = cardFromHotkey;
                if (Settings.isControllerMode && AbstractDungeon.actionManager.turnHasEnded) {
                    p.toHover = null;
                }
                if (cardFromHotkey != null && !p.inspectMode) {
                    Gdx.input.setCursorPosition((int)cardFromHotkey.hb.cX, (int)((float)Settings.HEIGHT - HOVER_CARD_Y_POSITION));
                }
            }
        } else {
            DollFields.targetDoll.set(p, null);

            for (AbstractDoll d : DollFields.dolls.get(p))
            {
                d.hb.update();
                if (d.hb.hovered)
                {
                    DollFields.targetDoll.set(p, d);
                    break;
                }
            }
        }

        if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && !InputHelper.justClickedRight && (float)InputHelper.mY >= ((float) ReflectionHacks.getPrivate(p, AbstractPlayer.class, "hoverStartLine")) - 100.0F * Settings.scale && (float)InputHelper.mY >= 50.0F * Settings.scale) {
            cardFromHotkey = InputHelper.getCardSelectedByHotkey(p.hand);
            if (cardFromHotkey != null && !isCardQueued(cardFromHotkey)) {
                boolean isSameCard = cardFromHotkey == p.hoveredCard;
                p.releaseCard();

                DollFields.targetDoll.set(p, null);

                if (isSameCard) {
                    GameCursor.hidden = false;
                } else {
                    p.hoveredCard = cardFromHotkey;
                    p.hoveredCard.setAngle(0.0F, false);
                    ReflectionHacks.setPrivate(p, AbstractPlayer.class, "isUsingClickDragControl", true);
                    p.isDraggingCard = true;
                }
            }

            if (!InputHelper.justClickedLeft && !InputActionSet.confirm.isJustPressed() && !CInputActionSet.select.isJustPressed()) {
                if (!((boolean)ReflectionHacks.getPrivate(p, AbstractPlayer.class, "isUsingClickDragControl")) && InputHelper.justReleasedClickLeft && DollFields.targetDoll.get(p) != null) {
                    if (p.hoveredCard.canUse(p, null)) {
                        tryPlayCard(p);
                    } else {
                        AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, p.hoveredCard.cantUseMessage, true));
                        //p.energyTip(p.hoveredCard); just a tutorial tip for energy costs
                        p.releaseCard();
                    }

                    p.inSingleTargetMode = false;
                    GameCursor.hidden = false;
                    DollFields.targetDoll.set(p, null);
                }
            } else {
                InputHelper.justClickedLeft = false;
                if (DollFields.targetDoll.get(p) == null) {
                    CardCrawlGame.sound.play("UI_CLICK_1");
                } else {
                    if (p.hoveredCard.canUse(p, null)) {
                        tryPlayCard(p);
                    } else {
                        AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, p.hoveredCard.cantUseMessage, true));
                        p.releaseCard();
                    }

                    ReflectionHacks.setPrivate(p, AbstractPlayer.class, "isUsingClickDragControl", false);
                    p.inSingleTargetMode = false;
                    GameCursor.hidden = false;
                    DollFields.targetDoll.set(p, null);
                }
            }
        } else {
            if (Settings.isTouchScreen) {
                InputHelper.moveCursorToNeutralPosition();
            }

            p.releaseCard();
            CardCrawlGame.sound.play("UI_CLICK_2");
            ReflectionHacks.setPrivate(p, AbstractPlayer.class, "isUsingClickDragControl", false);
            p.inSingleTargetMode = false;
            GameCursor.hidden = false;
            DollFields.targetDoll.set(p, null);
        }
    }




    private static boolean isCardQueued(AbstractCard card) {
        Iterator var2 = AbstractDungeon.actionManager.cardQueue.iterator();

        CardQueueItem item;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            item = (CardQueueItem)var2.next();
        } while(item.card != card);

        return true;
    }
}
