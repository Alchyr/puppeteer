package puppeteer.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.fields.DollFields;
import puppeteer.abstracts.AbstractMagic;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "render",
        paramtypez = { SpriteBatch.class }
)
public class DollAndMagicRenderPatch {
    private static ArrayList<AbstractMagic> magicTargetingToRender = new ArrayList<>();
    private static ArrayList<AbstractMagic> magicTargetingToRenderWithReticle = new ArrayList<>();

    public static void renderMagic(AbstractMagic m)
    {
        if (!magicTargetingToRender.contains(m) && !magicTargetingToRenderWithReticle.contains(m))
            magicTargetingToRender.add(m);
    }
    public static void renderMagicWithReticle(AbstractMagic m)
    {
        magicTargetingToRender.remove(m);
        if (!magicTargetingToRenderWithReticle.contains(m))
            magicTargetingToRenderWithReticle.add(m);
    }

    @SpirePostfixPatch
    public static void renderDolls(AbstractPlayer __instance, SpriteBatch sb)
    {
        sb.setColor(Color.WHITE);
        for (AbstractDoll d : DollFields.dolls.get(__instance))
        {
            d.render(sb);
        }

        if (!magicTargetingToRender.isEmpty() || !magicTargetingToRenderWithReticle.isEmpty())
        {
            for (AbstractMagic m : magicTargetingToRenderWithReticle)
            {
                for (AbstractMonster mo : m.getTargets())
                {
                    mo.renderReticle(sb);
                }
            }
            magicTargetingToRender.addAll(magicTargetingToRenderWithReticle);

            sb.end();

            AbstractMagic.sr.begin(ShapeRenderer.ShapeType.Line);

            AbstractMagic.sr.setColor(Color.WHITE);

            for (AbstractMagic m : magicTargetingToRender)
            {
                m.renderArea();
            }

            magicTargetingToRender.clear();
            magicTargetingToRenderWithReticle.clear();

            AbstractMagic.sr.end();

            sb.begin();
        }
    }
}
