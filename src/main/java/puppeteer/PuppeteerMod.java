package puppeteer;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;
import puppeteer.character.Puppeteer;
import puppeteer.enums.CharacterEnums;
import puppeteer.util.CardFilter;
import puppeteer.util.KeywordWithProper;
import puppeteer.util.TextureLoader;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static puppeteer.enums.CharacterEnums.PUPPETEER_CARD_COLOR;

@SpireInitializer
public class PuppeteerMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber
{
    public static final Logger logger = LogManager.getLogger("Puppeteer");

    private static final String modID = "puppeteer";

    //In-game mod info.
    private static final String MODNAME = "${project.name}";
    private static final String AUTHOR = "Alchyr";
    private static final String DESCRIPTION = "${project.description}";

    //Character Color
    public static final Color PUPPETEER_COLOR = CardHelper.getColor(200.0f, 200.0f, 123.0f);
    public static final Color PUPPETEER_COLOR_DIM = CardHelper.getColor(120.0f, 120.0f, 70.0f);

    //Card Assets
    private static final String ATTACK_BACK = makeCardPath("assets/bg_attack.png");
    private static final String SKILL_BACK = makeCardPath("assets/bg_skill.png");
    private static final String POWER_BACK = makeCardPath("assets/bg_power.png");

    private static final String ATTACK_BACK_PORTRAIT = makeCardPath("assets/bg_attack_p.png");
    private static final String SKILL_BACK_PORTRAIT = makeCardPath("assets/bg_skill_p.png");
    private static final String POWER_BACK_PORTRAIT = makeCardPath("assets/bg_power_p.png");

    private static final String ENERGY_ORB = makeCardPath("assets/energy_orb.png");
    private static final String ENERGY_ORB_PORTRAIT = makeCardPath("assets/energy_orb_p.png");
    private static final String CARD_SMALL_ORB = makeCardPath("assets/small_orb.png");

    //Character assets
    private static final String CHARACTER_BUTTON = makeCharacterPath("button.png");
    private static final String CHARACTER_PORTRAIT = makeCharacterPath("portrait.png");

    //Badge
    public static final String BADGE_IMAGE = makeImagePath("badge.png");

    // Atlas and JSON files for the Animations
    public static final String SKELETON_ATLAS = makeCharacterPath("alice.atlas");

    public static final String STAND_JSON = makeCharacterPath("alice_stand.json");
    public static final String ATTACK_JSON = makeCharacterPath("alice_attack.json");


    private static final String title = "\n" +
            "**********  *   *  ******** \n" +
            "        *   *   *         * \n" +
            "     ***    *   *        *  \n" +
            "     *      *   *      **   \n" +
            "     *         *     ** *   \n" +
            "    *         *     **   *  \n" +
            "   *        **     *      *\n\n" +
            "        **        ***                                  \n" +
            "     *****         ***      *                          \n" +
            "    *  ***          **     ***                         \n" +
            "       ***          **      *                          \n" +
            "      *  **         **                                 \n" +
            "      *  **         **    ***        ****       ***    \n" +
            "     *    **        **     ***      * ***  *   * ***   \n" +
            "     *    **        **      **     *   ****   *   ***  \n" +
            "    *      **       **      **    **         **    *** \n" +
            "    *********       **      **    **         ********  \n" +
            "   *        **      **      **    **         *******   \n" +
            "   *        **      **      **    **         **        \n" +
            "  *****      **     **      **    ***     *  ****    * \n" +
            " *   ****    ** *   *** *   *** *  *******    *******  \n" +
            "*     **      **     ***     ***    *****      *****   \n" +
            "*                                                      \n" +
            " **\n";

    //makePaths
    public static String makePath(String resourcePath) {
        return modID + "/" + resourcePath;
    }
    public static String makeImagePath(String resourcePath) {
        return modID + "/images/" + resourcePath;
    }
    public static String makePowerPath(String resourcePath) {
        return modID + "/images/powers/" + resourcePath;
    }
    public static String makeLargePowerPath(String resourcePath) {
        return modID + "/images/powers/big/" + resourcePath;
    }
    public static String makeCardPath(String resourcePath) {
        return modID + "/images/cards/" + resourcePath;
    }
    public static String makeCharacterPath(String resourcePath) {
        return modID + "/images/character/" + resourcePath;
    }
    public static String makeDollPath(String resourcePath) {
        return modID + "/images/dolls/" + resourcePath;
    }
    public static String makeEffectPath(String resourcePath) {
        return modID + "/images/effects/" + resourcePath;
    }
    public static String makeOrbPath(String resourcePath) {
        return modID + "/images/orb/" + resourcePath;
    }
    public static String makeLocalizationPath(String resourcePath) {
        return modID + "/localization/" + resourcePath;
    }

    public static String makeID(String id)
    {
        return modID + ":" + id;
    }



    @SuppressWarnings("unused")
    public static void initialize() {
        new PuppeteerMod();
    }

    public PuppeteerMod()
    {
        logger.info(title);

        BaseMod.subscribe(this);

        logger.info("Creating Alice's color.");
        BaseMod.addColor(PUPPETEER_CARD_COLOR, PUPPETEER_COLOR, PUPPETEER_COLOR, PUPPETEER_COLOR,
                PUPPETEER_COLOR, PUPPETEER_COLOR, PUPPETEER_COLOR, PUPPETEER_COLOR,
                ATTACK_BACK, SKILL_BACK, POWER_BACK, ENERGY_ORB,
                ATTACK_BACK_PORTRAIT, SKILL_BACK_PORTRAIT, POWER_BACK_PORTRAIT,
                ENERGY_ORB_PORTRAIT, CARD_SMALL_ORB);

        logger.info("Color created!");
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Here comes Alice!");

        BaseMod.addCharacter(new Puppeteer(),
                CHARACTER_BUTTON, CHARACTER_PORTRAIT, CharacterEnums.PUPPETEER);
    }

    @Override
    public void receivePostInitialize() {
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, null);

        logger.info("Done loading badge Image and mod options");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        //haha no relics yet

        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditCards() {
        //add variables


        //add cards
        try
        {
            autoAddCards();
        }
        catch (Exception e)
        {
            logger.error("Failed to automatically add cards!");
            logger.error(e.getMessage());
        }
    }


    @Override
    public void receiveEditStrings()
    {
        String lang = "eng";

        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocalizationPath(lang + "/RelicStrings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath(lang + "/CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocalizationPath(lang + "/CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizationPath(lang + "/PowerStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocalizationPath(lang + "/UIStrings.json"));

        lang = getLangString();
        if (lang.equals("eng")) return;

        try
        {
            BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocalizationPath(lang + "/RelicStrings.json"));
            BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath(lang + "/CardStrings.json"));
            BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocalizationPath(lang + "/CharacterStrings.json"));
            BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizationPath(lang + "/PowerStrings.json"));
            BaseMod.loadCustomStringsFile(UIStrings.class, makeLocalizationPath(lang + "/UIStrings.json"));
        }
        catch (Exception e)
        {
            logger.error("Failed to load other language strings. ");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void receiveEditKeywords()
    {
        String lang = getLangString();

        try
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(makeLocalizationPath(lang + "/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);

            if (keywords != null) {
                for (KeywordWithProper keyword : keywords) {
                    BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
        catch (Exception e)
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(makeLocalizationPath("eng/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);

            if (keywords != null) {
                for (KeywordWithProper keyword : keywords) {
                    BaseMod.addKeyword("astrologer", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }

    private String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }

    //This has been copied many times, now
    private static void autoAddCards() throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, ClassNotFoundException
    {
        ClassFinder finder = new ClassFinder();
        URL url = PuppeteerMod.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new CardFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        ArrayList<AbstractCard> addedCards = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());

            boolean isCard = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractCard.class.getName())) {
                    isCard = true;
                    break;
                }
            }
            if (!isCard) {
                continue;
            }

            AbstractCard card = (AbstractCard) Loader.getClassPool().getClassLoader().loadClass(cls.getName()).newInstance();

            BaseMod.addCard(card);
            addedCards.add(card);
        }

        for (AbstractCard c : addedCards)
        {
            UnlockTracker.unlockCard(c.cardID);
        }
    }
}
