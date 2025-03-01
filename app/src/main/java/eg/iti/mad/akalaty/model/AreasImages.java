package eg.iti.mad.akalaty.model;

import java.util.HashMap;

import eg.iti.mad.akalaty.R;

public class AreasImages {
    private static final HashMap<String,Integer> areasImgHashMap = new HashMap<String,Integer>(){
        {
            put("American", R.drawable.american);
            put("British", R.drawable.british);
            put("Canadian", R.drawable.canidian);
            put("Chinese", R.drawable.chinese);
            put("Croatian", R.drawable.croatian);
            put("Dutch", R.drawable.dutch);
            put("Egyptian", R.drawable.egyptian);
            put("Filipino", R.drawable.filipino);
            put("French", R.drawable.french);
            put("Greek", R.drawable.greek);
            put("Indian", R.drawable.indian);
            put("Irish", R.drawable.irish);
            put("Italian", R.drawable.italian);
            put("Jamaican", R.drawable.jamaican);
            put("Japanese", R.drawable.japan);
            put("Kenyan", R.drawable.kenyan);
            put("Malaysian", R.drawable.malaysian);
            put("Mexican", R.drawable.mexican);
            put("Moroccan", R.drawable.moroccan);
            put("Norwegian", R.drawable.norwegian);
            put("Polish", R.drawable.polish);
            put("Portuguese", R.drawable.portuguese);
            put("Russian", R.drawable.russian);
            put("Spanish", R.drawable.spanish);
            put("Thai", R.drawable.thai);
            put("Tunisian", R.drawable.tunisian);
            put("Turkish", R.drawable.turkish);
            put("Ukrainian", R.drawable.ukrainian);
            put("Uruguayan", R.drawable.uruguayan);
            put("Vietnamese", R.drawable.vietnamese);
        }
    };

    public static int getAreaByName(String areaName){
        if (areasImgHashMap.get(areaName)==null){
            return areasImgHashMap.get("Egyptian");
        }
        return areasImgHashMap.get(areaName);
    }
}
