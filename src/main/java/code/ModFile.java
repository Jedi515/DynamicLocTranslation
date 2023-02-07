package code;

import basemod.BaseMod;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ModFile implements
        EditStringsSubscriber,
        EditKeywordsSubscriber{

    public static final String modID = "DynamicLocTranslation";
    public static Map<String, String> replaceDict = new HashMap<>();

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public ModFile() {
        BaseMod.subscribe(this);
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static void initialize() {
        ModFile thismod = new ModFile();
    }

    @Override
    public void receiveEditStrings() {
        loadDynamicMap("eng");
        if (Settings.language != Settings.GameLanguage.ENG) loadDynamicMap(Settings.language.toString().toLowerCase());
    }

    private void loadDynamicMap(String langKey)
    {
        Gson gson = new Gson();
        String filepath =  modID + "Resources/localization/" + langKey + "/dynamicdict.json";
        if (Gdx.files.internal(filepath).exists()) {
            String json = Gdx.files.internal(filepath).readString(String.valueOf(StandardCharsets.UTF_8));
            replaceDict.putAll(gson.fromJson(json, replaceDict.getClass()));
        }
    }

    @Override
    public void receiveEditKeywords() {
        loadKeywords("eng");
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadKeywords(Settings.language.toString().toLowerCase());
        }
    }

    private void loadKeywords(String langKey)
    {
        String filepath = modID + "Resources/localization/" + langKey + "/Keywordstrings.json";

        if (Gdx.files.internal(filepath).exists())
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(filepath).readString(String.valueOf(StandardCharsets.UTF_8));
            com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

            if (keywords != null) {
                for (Keyword keyword : keywords) {
                    BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }
}
