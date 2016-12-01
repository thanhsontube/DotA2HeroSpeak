package son.nt.dota2.jsoup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.MsConst;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.ItemDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.utils.Logger;
import timber.log.Timber;

/**
 * Created by sonnt on 9/20/16.
 */
public class JsoupLoader {

    private boolean isLordVoiceDone = true;
    private boolean isResponsesHeroDone = false;
    private boolean isPushALlItems = true;
    private boolean isPushALlHEROBUYItems = false;
    private boolean isPushALlHEROKillingMeeting = true;
    private boolean isPushALlNormalVoices = true;

    public static final String TAG = JsoupLoader.class.getSimpleName();
    public static final String HERO_ICON = "http://dota2.gamepedia.com/Heroes_by_release";
    public static final String HERO_AVATAR = "http://dota2.gamepedia.com/Heroes";
    public static final String HERO_x_LORD = "http://dota2.gamepedia.com/%1$s/Lore";
    public static final String HERO_LORD = "http://dota2.gamepedia.com/Anti-Mage/Lore";
    public static final String HERO_LORD2 = "http://dota2.gamepedia.com/Underlord/Lore";
    public static final String HERO_RESPONSE = "http://dota2.gamepedia.com/Queen_of_Pain/Responses";
    public static final String HERO_RESPONSE2 = "http://dota2.gamepedia.com/Drow_Ranger/Responses";
    public static final String HERO_RESPONSE_Crystal = "http://dota2.gamepedia.com/Crystal_Maiden/Responses";
    public static final String HERO_RESPONSE_UNDER_LORD = "http://dota2.gamepedia.com/Underlord/Responses";
    public static final String HERO_RESPONSE_PA = "http://dota2.gamepedia.com/Phantom_Assassin/Responses";
    public static final String HERO_RESPONSE_TE = "http://dota2.gamepedia.com/Terrorblade/Responses";
    public static final String HERO_RESPONSE_SNIPER = "http://dota2.gamepedia.com/Sniper/Responses";
    public static final String ITEMS = "http://dota2.gamepedia.com/Items";


    public static final String HERO_RESPONSE3 = "http://dota2.gamepedia.com/index.php?title=Drow_Ranger/Responses&action=edit";

    //http://dota2.gamepedia.com/Items
    public void withGetItems() {
        Timber.d(">>>" + "withGetItems");
        getItems();
    }

    public void withGetHeroBasic_Avatar_Description() {
        new GetHeroBasic_Avatar_Description().execute();
    }

    /**
     * get data in http://dota2.gamepedia.com/Anti-Mage/Lore
     */
    public void withGetHeroBasic_Lord() {
        Logger.debug(TAG, ">>>" + "withGetHeroBasic_Lord");
        if (isLordVoiceDone) {
            return;
        }
        getLordVoice();
    }

    /**
     * get data in http://dota2.gamepedia.com/Crystal_Maiden/Responses
     */

    public void withGetHeroBasic_Response() {
        Logger.debug(TAG, ">>>" + "withGetHeroBasic_Response");
        getResponsesVoice();

    }


    public void withGetHeroBasic_PUSHLord() {
//        pushALL();
    }

    int itemCount = 0;

    private void getItems() {
        itemCount = 0;
        Observable<List<ItemDto>> observable = Observable.create(subscriber -> {

            List<ItemDto> list = new ArrayList<ItemDto>();
            Object[] data = getRootNodeItems(ITEMS);
            Timber.d(">>>" + "data:" + data.length);

            int shop = 0;
            for (int i = 0; i < data.length; i++) {
                try {
                    TagNode tagNode = (TagNode) data[i];


                    if (tagNode.getChildTagList().get(0).getName().equals("div")) {
                        String group = "Unknown";
                        if (shop == 0) {
                            group = "Secret Shop";
                        }
                        if (shop == 1) {
                            group = "Side Lane Shop";
                        }
                        if (shop == 2) {
                            group = "Dropped Items";
                        }
                        if (shop == 3) {
                            group = "Seasonal Events";
                        }
                        list.addAll(getSpecialItems(tagNode, group));
                        shop++;
                    } else {

                        list.addAll(getSmallItems(tagNode));
                    }

                } catch (Exception e) {
                    Timber.e(">>>" + "Err small item:" + e);
                }

            }


//            for (ItemDto dto : list) {
//                pushItem(dto);
//            }

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(list);
            realm.commitTransaction();
            realm.close();

            subscriber.onNext(list);
            subscriber.onCompleted();


        });
        Observer<List<ItemDto>> observers = new Observer<List<ItemDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ItemDto> itemDtos) {
                Timber.d(">>>" + "onNext:" + itemDtos.size());
                for (ItemDto d : itemDtos) {
                    Timber.d(">>>" + "Items:" + d.getItemName());
                }

//                pushALLItems(itemDtos);


            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observers);
    }

    private List<ItemDto> getSpecialItems(TagNode tag, String group) {
        Timber.d(">>>" + "getSpecialItems");
        List<ItemDto> list = new ArrayList<ItemDto>();
        ItemDto dto = null;
        try {
            String xPath = "//tr[@valign='top']";
            Object[] data = tag.evaluateXPath(xPath);

            for (Object o : data) {
                TagNode tagImages = (TagNode) o;
                for (TagNode t : tagImages.getChildTagList()) {
                    dto = new ItemDto(group);
                    final TagNode tagNode1 = t.getChildTagList().get(0);
                    String itemName = tagNode1.getAttributeByName("title");
                    final String href = tagNode1.getAttributeByName("href");
                    String itemHref = "http://dota2.gamepedia.com" + href;
                    String itemId = href.replace("/", "");
                    String itemIcon = tagNode1.getChildTagList().get(0).getAttributeByName("src");
                    String itemPrice = "";
                    try {
                        itemPrice = t.getChildTagList().get(2).getText().toString();
                        itemPrice = itemPrice.replace("(", "").replace(")", "");
                    } catch (Exception i) {

                    }

                    itemIcon = itemIcon.substring(0, itemIcon.indexOf("?version"));

                    dto.setItemName(itemName);
                    dto.setItemIcon(itemIcon);
                    dto.setItemId(itemId);
                    dto.setItemNo(itemCount);
                    dto.setItemHref(itemHref);
                    dto.setItemPrice(TextUtils.isEmpty(itemPrice) ? 0 : Integer.parseInt(itemPrice));

                    //http://dota2.gamepedia.com/Greater_Salve
                    Timber.d(">>>" + itemCount + ".itemName:" + itemName + ";price:" + itemPrice + ";href:" + itemHref + " ;itemIcon:" + itemIcon);


                    itemCount++;
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            Timber.e(">>>" + "getSpecialItems:" + e);
        }


        return list;
    }

    private List<ItemDto> getSmallItems(TagNode tagNode) {
        List<ItemDto> list = new ArrayList<ItemDto>();
        ItemDto dto = null;
        String group = "Unknown";
        for (TagNode tag : tagNode.getChildTagList()) {
            try {
                if (tag.getName().equals("h3")) {
                    final String itemGroup = tag.getText().toString().replace("Edit", "").trim();
                    Timber.d(">>>" + "Item Group:" + itemGroup);
                    group = itemGroup;
                } else {
                    String xPath = "//tr[@valign='top']";
                    Object[] data = tag.evaluateXPath(xPath);

                    for (Object o : data) {
                        TagNode tagImages = (TagNode) o;
                        for (TagNode t : tagImages.getChildTagList()) {
                            dto = new ItemDto(group);
                            final TagNode tagNode1 = t.getChildTagList().get(0);
                            String itemName = tagNode1.getAttributeByName("title");
                            final String href = tagNode1.getAttributeByName("href");
                            String itemHref = "http://dota2.gamepedia.com" + href;
                            String itemId = href.replace("/", "");
                            String itemIcon = tagNode1.getChildTagList().get(0).getAttributeByName("src");
                            String itemPrice = "";
                            try {
                                itemPrice = t.getChildTagList().get(2).getText().toString();
                                itemPrice = itemPrice.replace("(", "").replace(")", "");
                            } catch (Exception i) {

                            }

                            itemIcon = itemIcon.substring(0, itemIcon.indexOf("?version"));

                            dto.setItemName(itemName);
                            dto.setItemIcon(itemIcon);
                            dto.setItemId(itemId);
                            dto.setItemNo(itemCount);
                            dto.setItemHref(itemHref);
                            dto.setItemPrice(TextUtils.isEmpty(itemPrice) ? 0 : Integer.parseInt(itemPrice));

                            //http://dota2.gamepedia.com/Greater_Salve
                            Timber.d(">>>" + itemCount + ".itemName:" + itemName + ";price:" + itemPrice + ";href:" + itemHref + " ;itemIcon:" + itemIcon);


                            itemCount++;
                            list.add(dto);
                        }
                    }

                }


            } catch (Exception e) {
                Timber.e(">>>" + "err:" + e);
            }


        }
        return list;

    }

    /**
     * this one will get Avatar
     */
    class GetHeroBasic_Avatar_Description extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_AVATAR).get();
                String img = document.select("a[href=/Doom]").select("img").attr("src");
                img = img.substring(0, img.lastIndexOf("?version"));
                Logger.debug(TAG, ">>>" + "img:" + img);

//                Elements elements = document.select("a[href=/Doom]").get(0).parents();
//                String des = elements.select("td[style=padding:8px]").text();
//                Logger.debug(TAG, ">>>" + "des:" + des);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String getAvatar(String heroID) {
        try {
            Document document = Jsoup.connect(HERO_AVATAR).get();
            final String cssQuery = "a[href=/" + heroID + "]";
            String img = document.select(cssQuery).select("img").attr("src");
            img = img.substring(0, img.lastIndexOf("?version"));
            Logger.debug(TAG, ">>>" + "img:" + img);
            return img;

//                Elements elements = document.select("a[href=/Doom]").get(0).parents();
//                String des = elements.select("td[style=padding:8px]").text();
//                Logger.debug(TAG, ">>>" + "des:" + des);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //http://dota2.gamepedia.com/Heroes_by_release

    /**
     * this one gets heroID and heroIcon
     */
    public void withGetHeroBasic_Icon() {
        new GetHeroBasic_Icon().execute();

    }

    /**
     * this one will get : name; heroID; heroIcon; no
     */
    class GetHeroBasic_Icon extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_ICON).get();
                Elements icons = document.select("span[style=white-space:nowrap]");
                Logger.debug(TAG, ">>>" + "icons:" + icons.size());
                List<HeroBasicDto> heroes = new ArrayList<>();
                HeroBasicDto dto;
                int no = 1;
                for (Element icon : icons) {
                    Elements iconChild = icon.select("a[href]");
                    Element child = iconChild.get(0);
                    String title = child.attr("title");
                    String href = child.attr("href");
                    Logger.debug(TAG, ">>>" + "title:" + title + ";href:" + href);

                    String heroIcon = child.select("img").attr("src");
                    heroIcon = heroIcon.substring(0, heroIcon.lastIndexOf("?version"));
                    Logger.debug(TAG, ">>>" + "src:" + heroIcon);

                    dto = new HeroBasicDto();
                    dto.name = title;
                    dto.heroId = href.replace("/", "");
                    dto.heroIcon = heroIcon;
                    dto.no = no;

                    heroes.add(dto);
                    no++;
                }

                for (HeroBasicDto hero : heroes) {

                    final String avatar = getAvatar(hero.heroId);
                    if (avatar == null) {
                        Logger.error(TAG, ">>> Error:" + "Null avatar:" + avatar);
                    } else {
                        hero.avatar = avatar;
                    }

                }

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference reference = firebaseDatabase.getReference();


                for (HeroBasicDto heroBasicDto : heroes) {
                    reference.child(HeroBasicDto.class.getSimpleName()).push().setValue(heroBasicDto)
                            .addOnSuccessListener(aVoid -> Logger.debug(TAG, ">>>" + "onSuccess 2"))
                            .addOnFailureListener(e -> Logger.error(TAG, ">>> Error:" + "onFailure:" + e))
                    ;
                }


            } catch (Exception e) {
                Logger.error(TAG, ">>> Error:" + e);
            }
            return null;
        }
    }


    private TagNode getRootNode(String url) {
        try {
            CleanerProperties props = new CleanerProperties();

            props.setTranslateSpecialEntities(true);
            props.setTransResCharsToNCR(true);
            props.setOmitComments(true);

            TagNode tagNode = new HtmlCleaner(props).clean(new URL(url));
            String xPath = "//div[@id='mw-content-text']";
            Object[] data = tagNode.evaluateXPath(xPath);
            TagNode nodeA = (TagNode) data[0];
            return nodeA;
        } catch (Exception e) {
            Timber.d(">>>" + "err:" + e);
            return null;
        }
    }

    private Object[] getRootNodeItems(String url) {
        try {
            CleanerProperties props = new CleanerProperties();

            props.setTranslateSpecialEntities(true);
            props.setTransResCharsToNCR(true);
            props.setOmitComments(true);

            TagNode tagNode = new HtmlCleaner(props).clean(new URL(url));
            String xPath = "//div[@class='nomobile']";
            Object[] data = tagNode.evaluateXPath(xPath);
            return data;
        } catch (Exception e) {
            Timber.d(">>>" + "err:" + e);
            return null;
        }
    }


    private List<HeroResponsesDto> processWithTextPOnly(TagNode tagNode, String group, HeroBasicDto fromHero) {
        List<HeroResponsesDto> list = new ArrayList<>();
        Timber.d(">>>" + "---processWithTextPOnly group:" + group);
        try {
            HeroResponsesDto dto = null;
            for (TagNode tag : tagNode.getChildTagList()) {
                dto = new HeroResponsesDto(itemCount);


                String soundContent = cutOffString(tag.getText().toString());


                List<TagNode> listSound = (List<TagNode>) tag.getElementListHavingAttribute("class", true);
//                Timber.d(">>>" + "listSound:" + listSound.size());

                String originalSound = "";
                String arcanaSound = "";
                if (listSound != null && !listSound.isEmpty()) {
                    originalSound = listSound.get(0).getAttributeByName("href");
                    if (listSound.size() >= 2) {
                        arcanaSound = listSound.get(listSound.size() - 1).getAttributeByName("href");
                    }
                }
//                Timber.d(">>>" + "originalSound:" + originalSound + " ;arcanaSound:" + arcanaSound);


                dto.setHeroId(fromHero.heroId);
                dto.setHeroIcon(fromHero.heroIcon);
                dto.setHeroName(fromHero.name);
                dto.setVoiceGroup(group);

                dto.setText(soundContent);
                dto.setLink(originalSound);
                dto.setLinkArcana(arcanaSound);

                list.add(dto);
                Timber.d(">>>" + "TextOnly:" + itemCount + "; data:" + dto.toString());
                itemCount++;

//                Timber.d(">>>" + "soundContent:" + soundContent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // todo care about arcana sound.
    private List<HeroResponsesDto> workingWithText(TagNode tagNode, String group, HeroBasicDto fromHero) {
        Timber.d(">>>" + "workingWithText id:" + fromHero.heroId + ";group:" + group);
        List<HeroResponsesDto> list = new ArrayList<>();
        try {
            HeroResponsesDto dto = null;
            TagNode finalTag;
            final TagNode tagNodeFirst = tagNode.getChildTagList().get(0);
            finalTag = tagNodeFirst;
            if (tagNodeFirst.getName().equals("p")) {
                final TagNode tagNodeUL = tagNode.getChildTagList().get(1);
                finalTag = tagNodeUL;
            }
            for (TagNode tag : finalTag.getChildTagList()) {
                dto = new HeroResponsesDto(itemCount);
                String soundContent = cutOffString(tag.getText().toString());
//                String soundLink = getSoundLink(tag);

                List<TagNode> listSound = (List<TagNode>) tag.getElementListHavingAttribute("class", true);
                String originalSound = "";
                String arcanaSound = "";
                if (listSound != null && !listSound.isEmpty()) {
                    originalSound = listSound.get(0).getAttributeByName("href");
                    if (listSound.size() >= 2) {
                        arcanaSound = listSound.get(listSound.size() - 1).getAttributeByName("href");
                        if (arcanaSound == null || !arcanaSound.startsWith("http")) {
                            arcanaSound = "";
                        }
                    }
                }
//                Timber.d(">>>" + "originalSound:" + originalSound + " ;arcanaSound:" + arcanaSound);


                dto.setHeroId(fromHero.heroId);
                dto.setHeroIcon(fromHero.heroIcon);
                dto.setHeroName(fromHero.name);
                dto.setVoiceGroup(group);

                dto.setText(soundContent);
                dto.setLink(originalSound);
                dto.setLinkArcana(arcanaSound);

                list.add(dto);
                itemCount++;

                Timber.d(">>>" + "Normal Sounds:" + itemCount + "; data:" + dto.toString());

            }

        } catch (Exception e) {
            Timber.e(">>>" + "Err workingWithText:" + e);
        }
        return list;
    }

    private List<HeroResponsesDto> workWithResponseHasIcon(TagNode tagNode, String group, HeroBasicDto fromHero) {
        Timber.d(">>>" + "--workWithResponseHasIcon id:" + fromHero.heroId + ";group:" + group);
        if (!mIsItem) {
            return new ArrayList<>();
        }
        List<HeroResponsesDto> list = new ArrayList<>();
        boolean isKillingSpecificEnemy = false;
        boolean isFirstBlood = false;
        for (TagNode tag : tagNode.getChildTagList()) {

            try {
                if (tag.getName().contains("ul")) {
                    if (isKillingSpecificEnemy) {
                        isKillingSpecificEnemy = false;
                        final List<HeroResponsesDto> killingEnemy = getKillingEnemy(tag, group, fromHero);
                        list.addAll(killingEnemy);
                    } else {
                        final List<HeroResponsesDto> collection = processWithTextPOnly(tag, isFirstBlood ? "First Blood" : group, fromHero);
                        list.addAll(collection);
                        isFirstBlood = false;
                    }

                }

                if (tag.getName().contains("p")) {
//                    Logger.debug(TAG, ">>>" + "p > text:" + tag.getText());
                    isKillingSpecificEnemy = true;
                    if (tag.getText().toString().contains("First Blood")) {
                        isKillingSpecificEnemy = false;
                        isFirstBlood = true;
                    }
                }
            } catch (Exception e) {
                Timber.e(">>>" + "err workWithResponseHasIcon:" + e);
            }
        }

        return list;

    }

    private String getSoundLink(TagNode tag) {
        TagNode tagMp3 = tag.getChildTagList().get(0);

        return tagMp3.getAttributeByName("href");
    }

    private String getHeroID(TagNode tagNode) {
        TagNode tagToHero = tagNode.getChildTagList().get(1);
        String toHeroId = tagToHero.getAttributeByName("href");

        if (toHeroId != null) {
            toHeroId = toHeroId.replace("/", "");
            if ("Abyssal_Underlord".equals(toHeroId)) {
                toHeroId = "Underlord";
            }

        }
        return toHeroId;
    }

    private String cutOffString(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }

        String output = input.replace("Play", "").trim();

        //remove u
        if (input.length() < 2) {
            return output;
        }

        String twoFirstChars = output.substring(0, 2);
        if (twoFirstChars.equals("u ") || twoFirstChars.equals("r ")) {
            output = output.substring(1, output.length());
        }

        return output.trim();
    }

    //http://dota2.gamepedia.com/Crystal_Maiden/Responses
    private List<HeroResponsesDto> getKillingEnemy(TagNode nodeA, String soundGroup, HeroBasicDto fromHero) {
        List<HeroResponsesDto> list = new ArrayList<>();
        HeroResponsesDto dto = null;

        try {
            for (TagNode tag : nodeA.getChildTagList()) {
                try {
                    dto = new HeroResponsesDto(itemCount);

                    String soundContent = cutOffString(tag.getText().toString());


                    List<TagNode> listSound = (List<TagNode>) tag.getElementListHavingAttribute("class", true);
//                    Timber.d(">>>" + "listSound:" + listSound.size());

                    String originalSound = "";
                    String arcanaSound = "";
                    if (listSound != null && !listSound.isEmpty()) {
                        originalSound = listSound.get(0).getAttributeByName("href");
                        if (listSound.size() >= 2) {
                            arcanaSound = listSound.get(listSound.size() - 1).getAttributeByName("href");
                            if (arcanaSound == null || !arcanaSound.startsWith("http")) {
                                arcanaSound = "";
                            }
                        }
                    }
//                    Timber.d(">>>" + "originalSound:" + originalSound + " ;arcanaSound:" + arcanaSound);


                    List<TagNode> tagImage = (List<TagNode>) tag.getElementListHavingAttribute("src", true);
//                    Timber.d(">>>" + "tagImage:" + tagImage.size());

                    //add


                    //to heroID
                    String toHeroId = "";
                    if (tagImage != null && !tagImage.isEmpty()) {
                        toHeroId = tagImage.get(0).getParent().getAttributeByName("href");
                    }
                    if (!TextUtils.isEmpty(toHeroId)) {
                        toHeroId = toHeroId.replace("/", "");
                        if ("Abyssal_Underlord".equals(toHeroId)) {
                            toHeroId = "Underlord";
                        }

                        dto.setToHeroId(toHeroId);


//                        Timber.d(">>>" + "toHeroId:" + toHeroId + ";soundContent:" + soundContent + ";soundLink:" + originalSound);

                        if (mIsItem) {
                            Realm realm = Realm.getDefaultInstance();
                            ItemDto itemDto = realm.where(ItemDto.class)
                                    .equalTo("itemId", toHeroId)
                                    .findFirst();

                            if (itemDto != null) {
                                Timber.d(">>>" + "Items:" + itemDto.getItemName());

                                dto.setToHeroName(itemDto.getItemName());
                                dto.setToHeroIcon(itemDto.getItemIcon());
                            } else {
                                Timber.e(">>>" + "Not found:" + toHeroId);
                            }
                            realm.close();
                        } else {
                            Realm realm = Realm.getDefaultInstance();
                            HeroBasicDto itemDto = realm.where(HeroBasicDto.class)
                                    .equalTo("heroId", toHeroId)
                                    .findFirst();

                            if (itemDto != null) {
                                Timber.d(">>>" + "toHeoID:" + itemDto.heroId);

                                dto.setToHeroName(itemDto.name);
                                dto.setToHeroIcon(itemDto.heroIcon);
                            } else {
                                Timber.e(">>>" + "Not found:" + toHeroId);
                            }
                            realm.close();

                        }

                    }

                    dto.setHeroId(fromHero.heroId);
                    dto.setHeroIcon(fromHero.heroIcon);
                    dto.setHeroName(fromHero.name);
                    dto.setVoiceGroup(soundGroup);

                    dto.setText(soundContent);
                    dto.setLink(originalSound);
                    dto.setLinkArcana(arcanaSound);
                    list.add(dto);
                    Timber.d(">>>" + "Image Sounds:" + itemCount + "; data:" + dto.toString());
                    itemCount++;

                } catch (Exception e) {
                    Logger.error(TAG, ">>> Error getKillingEnemy lv 1" + "error:" + e.toString());
                    processWithTextPOnly(tag, soundGroup, fromHero);

                }


            }


        } catch (Exception e) {
            Logger.error(TAG, ">>> Error:" + "error:" + e.toString());
        }
        return list;
    }

    private void processResponse(String group, TagNode tagNode) {
        try {
            Logger.debug(TAG, ">>>" + "processResponse group:" + group + ";tagNode:" + tagNode.getAllChildren().size());

            String xpathA = "//a[@class='sm2_button']";
            Object[] objA = tagNode.evaluateXPath(xpathA);
            if (objA == null || objA.length == 0) {
                return;
            }

            for (Object object : objA) {
                TagNode nodeTitle = (TagNode) object;
                String mp3 = nodeTitle.getAttributeByName("href");
                Logger.debug(TAG, ">>>" + "mp3:" + mp3);
            }
        } catch (XPatherException e) {
            Logger.error(TAG, ">>> Error:" + group + ":" + e);
        }
    }

    private void processResponseIcon(String group, TagNode tagNode) {
        try {
            Logger.debug(TAG, ">>>" + "processResponseIcon group:" + group + ";tagNode:" + tagNode.getAllChildren().size());

            String xpathA = "//a[@class='sm2_button']";
            Object[] objA = tagNode.evaluateXPath(xpathA);
            if (objA == null || objA.length == 0) {
                return;
            }

            for (Object object : objA) {
                TagNode nodeTitle = (TagNode) object;
                String mp3 = nodeTitle.getAttributeByName("href");
                Logger.debug(TAG, ">>>" + "mp3:" + mp3);
            }
        } catch (XPatherException e) {
            Logger.error(TAG, ">>> Error:" + group + ":" + e);
        }
    }

    private void getResponseWithJsoup() {
        Logger.debug(TAG, ">>>" + "*****getResponseWithJsoup****");
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo working
    //http://dota2.gamepedia.com/Crystal_Maiden/Responses
    //new GetHeroBasic_Responses().execute();
    //Acquiring an item

    boolean mIsItem = false;

    private void getResponsesVoice() {
        itemCount = 0;
        Timber.d(">>>" + "getResponsesVoice");
        if (isResponsesHeroDone) {
            return;
        }
        Observable<List<HeroResponsesDto>> responseObservable = Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();
            final List<HeroBasicDto> dtoBasic = realm.where(HeroBasicDto.class)
                    .findAll();

            final List<HeroBasicDto> heroBasicDtos = realm.copyFromRealm(dtoBasic);
            realm.close();

            List<HeroResponsesDto> list = new ArrayList<HeroResponsesDto>();
            int i = 0;
            for (final HeroBasicDto heroBasicDto : heroBasicDtos) {
                try {
                    i++;
                    //todo hack
                    if (i == 2) {
//                        break;
                    }
                    String heroId = heroBasicDto.heroId;

                    //todo hack
//                    heroId = "Terrorblade";

                    String path = "http://dota2.gamepedia.com/" + heroId + "/Responses";

                    Timber.d(">>>" + i + ":" + "working with : " + path);

//                    if (!heroBasicDto.isArcana()) {
//                        Timber.d(">>>" + "Skip:" + heroId);
//                        continue;
//                    }
                    Timber.d(">>>" + "Continue:" + heroId);

                    //Get Group Killing a specific enemy
                    TagNode nodeA = getRootNode(path);
                    if (nodeA == null) {
                        Timber.i(">>>" + "getResponseWithHtml null Root NODE:" + path);
                        continue;
                    }

                    List<TagNode> tagNodes = nodeA.getChildTagList();
                    int j = 0;
                    boolean isKillingEnemy = false;
                    boolean isMeetingAnAlly = false;
                    boolean isItem = false;
                    boolean isNormalSound = false;
                    String soundGroup = "Unknown";
                    for (TagNode tag : tagNodes) {

                        try {
                            if (tag.getName().contains("h2")) {
//                                    Logger.debug(TAG, ">>>" + j + " h2:" + tag.getText());
                                if (tag.getText().toString().contains("Killing an enemy")) {
                                    isKillingEnemy = true;
                                    isNormalSound = false;
                                }
                                if (tag.getText().toString().contains("Meeting an ally")) {
                                    Timber.e(">>>" + "Meeting an ally");
                                    isMeetingAnAlly = true;
                                    isNormalSound = false;
                                }

                                if (tag.getText().toString().contains("Acquiring an item")) {
                                    Timber.e(">>>" + "Acquiring an item\n");
                                    isNormalSound = false;
                                    isItem = true;
                                    mIsItem = true;
                                }


                                //normal
                                if (!isKillingEnemy && !isMeetingAnAlly && !isItem) {
                                    soundGroup = tag.getText().toString().trim();
                                    isNormalSound = true;
                                }

                            } else {
//                                if (isKillingEnemy) {
//                                    list.addAll(workWithResponseHasIcon(tag, "Killing an enemy", heroBasicDto));
//                                    isKillingEnemy = false;
//                                }
//                                if (isMeetingAnAlly) {
//                                    list.addAll(workWithResponseHasIcon(tag, "Meeting an ally", heroBasicDto));
//                                    isMeetingAnAlly = false;
//                                }
                                if (isItem) {
                                    list.addAll(workWithResponseHasIcon(tag, "Acquiring an item", heroBasicDto));
                                    isItem = false;
                                    mIsItem = false;
                                }
                                if (isNormalSound) {
//                                    list.addAll(workingWithText(tag, soundGroup, heroBasicDto));
                                    isNormalSound = false;
                                }
                            }
                            j++;

                        } catch (Exception e1) {
                            Timber.d(">>>" + "Err Killing an enemy : " + e1 + ";path:" + path);
                        }

                    }

                } catch (Exception e) {
                    Timber.e(">>>" + "Err: getResponsesVoice :" + heroBasicDto.heroId + ":" + e);
                }
            }

            subscriber.onNext(list);
            subscriber.onCompleted();

        });

        Observer<List<HeroResponsesDto>> finalList = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroResponsesDto> heroResponsesDtos) {
                Timber.d(">>>" + "onNext:" + heroResponsesDtos.size());
                pushALLHeroItems(heroResponsesDtos);
//                pushALLHeroKillingMeeting(heroResponsesDtos);
//                pushALLHeroNormalVoices(heroResponsesDtos);

//                for (HeroResponsesDto dto : heroResponsesDtos) {
//                    Timber.d(">>>" + "items:" + dto.toString());
//                }

            }
        };
        responseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(finalList);

    }

    //using rx, the old one new GetHeroBasic_Lord().execute("Anti-Mage")
    private void getLordVoice() {
        Logger.debug(TAG, ">>>" + "************* getLordVoice *******:");
        if (isLordVoiceDone) {
            return;
        }
        Observable<List<HeroResponsesDto>> responsesDtoObservable = Observable.create(new Observable.OnSubscribe<List<HeroResponsesDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroResponsesDto>> subscriber) {

                Realm realm = Realm.getDefaultInstance();
                final List<HeroBasicDto> dtoBasic = realm.where(HeroBasicDto.class)
                        .findAll();

                final List<HeroBasicDto> heroBasicDtos = realm.copyFromRealm(dtoBasic);
                realm.close();

                List<HeroResponsesDto> list = new ArrayList<HeroResponsesDto>();
                for (HeroBasicDto heroBasicDto : heroBasicDtos) {

                    try {
                        String heroId = heroBasicDto.heroId;
                        String path = "http://dota2.gamepedia.com/" + heroId + "/Lore";
                        Document document = Jsoup.connect(path).get(); //hack antimage

                        Elements mains = document.select("div[id=mw-content-text]").get(0).getElementsByTag("ul");
//                    Logger.debug(TAG, ">>>" + "mains:" + mains.size());
                        Logger.debug(TAG, ">>>" + "Begin:" + path);


                        int no = -1;
                        for (Element element : mains) { //A1

                            try {
                                Element before = element.previousElementSibling();
                                if (before != null && before.text() != null && before.text().contains("Allies meeting")) {

                                    Logger.debug(TAG, ">>>" + "***Allies meeting***");
                                    final Elements elementsByTag = element.getElementsByTag("li");
                                    HeroResponsesDto dto = null;
                                    for (Element d : elementsByTag) {
                                        try {
                                            no++;
                                            dto = new HeroResponsesDto(no);

                                            dto.setHeroId(heroId);
                                            dto.setHeroName(heroBasicDto.name);
                                            dto.setHeroIcon(heroBasicDto.heroIcon);
                                            dto.setVoiceGroup("Allies meeting");
                                            final String text = d.ownText();
                                            String mp3 = d.select("a[class=sm2_button]").attr("href");
                                            String id = d.select("img[alt]").get(0).parent().attr("href");
                                            Logger.debug(TAG, ">>>" + "id:" + id + ";text:" + text + ";mp3:" + mp3);

                                            dto.setText(text);
                                            dto.setLink(mp3);
                                            String toHeroId = id.replace("/", "");
                                            if (toHeroId.equals("Abyssal_Underlord")) {
                                                toHeroId = "Underlord";
                                            }
                                            dto.setToHeroId(toHeroId);

                                            Realm realm2 = Realm.getDefaultInstance();
                                            final HeroBasicDto toHero = realm2.where(HeroBasicDto.class)
                                                    .equalTo("heroId", toHeroId)
                                                    .findFirst();

                                            HeroBasicDto toHero2 = realm2.copyToRealm(toHero);
                                            realm2.close();
                                            dto.setToHeroIcon(toHero2.heroIcon);
                                            dto.setToHeroName(toHero2.name);

                                            dto.setSub("[" + toHero2.name + "]" + " is meeting " + "[" + heroBasicDto.name + "]");


                                            list.add(dto);

                                        } catch (Exception e) {
                                            Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord elementsByTag Allies meeting:" + e);
                                        }

                                    }


                                }
                                if (before.text().contains("Enemies killing")) {
                                    Logger.debug(TAG, ">>>" + "***Enemies killing***");
                                    final Elements href = element.getElementsByAttribute("href");
                                    final Elements elementsByTag = element.getElementsByTag("li");
                                    HeroResponsesDto dto = null;
                                    for (Element d : elementsByTag) { //A2
                                        try {
                                            no++;
                                            dto = new HeroResponsesDto(no);
                                            dto.setHeroId(heroId);
                                            dto.setHeroName(heroBasicDto.name);
                                            dto.setHeroIcon(heroBasicDto.heroIcon);
                                            dto.setVoiceGroup("Enemies killing");

                                            final String text = d.ownText();
                                            String mp3 = d.select("a[class=sm2_button]").attr("href");

                                            String id = d.select("img[alt]").get(0).parent().attr("href");

                                            Logger.debug(TAG, ">>>" + "id:" + id + ";text:" + text + ";mp3:" + mp3);
                                            dto.setText(text);
                                            dto.setLink(mp3);
                                            String toHeroId = id.replace("/", "");
                                            if (toHeroId.equals("Abyssal_Underlord")) {
                                                toHeroId = "Underlord";
                                            }
                                            dto.setToHeroId(toHeroId);

                                            Realm realm2 = Realm.getDefaultInstance();
                                            final HeroBasicDto toHero = realm2.where(HeroBasicDto.class)
                                                    .equalTo("heroId", toHeroId)
                                                    .findFirst();

                                            HeroBasicDto toHero2 = realm2.copyToRealm(toHero);
                                            realm2.close();
                                            dto.setToHeroIcon(toHero2.heroIcon);
                                            dto.setToHeroName(toHero2.name);

                                            dto.setSub("[" + toHero2.name + "]" + " is killing " + "[" + heroBasicDto.name + "]");

                                            list.add(dto);
                                        } catch (Exception e) {
                                            Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord elementsByTag Enemies killing:" + e);
                                        }


                                    }


                                }
                                if (before.text().contains("Others")) {
                                    Logger.debug(TAG, ">>>" + "***Others***");
                                    final Elements href = element.getElementsByAttribute("href");
                                    final Elements elementsByTag = element.getElementsByTag("li");

                                    HeroResponsesDto dto = null;
                                    for (Element d : elementsByTag) {
                                        try {
                                            no++;
                                            dto = new HeroResponsesDto(no);

                                            dto.setHeroId(heroId);
                                            dto.setHeroName(heroBasicDto.name);
                                            dto.setHeroIcon(heroBasicDto.heroIcon);
                                            dto.setVoiceGroup("Others");

                                            final String text = d.ownText();
                                            String mp3 = d.select("a[class=sm2_button]").attr("href");
                                            String id = d.select("img[alt]").get(0).parent().attr("href");

                                            Logger.debug(TAG, ">>>" + "id:" + id + ";text:" + text + ";mp3:" + mp3);
                                            dto.setText(text);
                                            dto.setLink(mp3);
                                            String toHeroId = id.replace("/", "");
                                            if (toHeroId.equals("Abyssal_Underlord")) {
                                                toHeroId = "Underlord";
                                            }
                                            dto.setToHeroId(toHeroId);

                                            Realm realm2 = Realm.getDefaultInstance();
                                            final HeroBasicDto toHero = realm2.where(HeroBasicDto.class)
                                                    .equalTo("heroId", toHeroId)
                                                    .findFirst();

                                            HeroBasicDto toHero2 = realm2.copyToRealm(toHero);
                                            realm2.close();
                                            dto.setToHeroIcon(toHero2.heroIcon);
                                            dto.setToHeroName(toHero2.name);

                                            list.add(dto);
                                        } catch (Exception e) {
                                            Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord elementsByTag Others:" + e);
                                        }


                                    }

                                }


                            } catch (Exception e) {
                                Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord .. A1 :" + e);
                            }


                        }
                        Logger.debug(TAG, ">>>" + "----END ----  list:" + list.size());


                    } catch (Exception e) {
                        Logger.error(TAG, ">>> ROOT Error:" + e);
                        subscriber.onError(e);
                        subscriber.onCompleted();
                    }
                }

                Logger.debug(TAG, ">>>" + "&&&&&&&& FINAL:" + list.size());
                subscriber.onNext(list);
                subscriber.onCompleted();


            }
        });

        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {
                Logger.debug(TAG, ">>>" + "------ RESULT ------ onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                Logger.error(TAG, ">>> Error:" + "onError:" + e);

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                Timber.d(">>> RESULT size>>>:" + list.size());
                saveFirst(list);
//                for (HeroResponsesDto d : list) {
//                    Timber.d("id:" + d.getHeroId() + ";group:" + d.getVoiceGroup() + ";othersID:" + d.getToHeroId());
//                    //save and push
//                }

            }
        };

        responsesDtoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void saveFirst(List<HeroResponsesDto> heroes) {
        Observable.create((Observable.OnSubscribe<List<HeroResponsesDto>>) subscriber -> {
            Realm realm = Realm.getDefaultInstance();
            removeAll(realm);
            try {
                for (HeroResponsesDto heroBasicDto : heroes) {
                    realm.beginTransaction();
                    realm.copyToRealm(heroBasicDto);
                    realm.commitTransaction();
                }

            } catch (Exception e) {
                Logger.error(TAG, ">>> Error:" + "saveFirst");

            } finally {
                realm.close();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();


    }

    private void pushALL() {
        mPushCounter = 0;

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Realm realm = Realm.getDefaultInstance();
                try {
                    List<HeroResponsesDto> list = realm.where(HeroResponsesDto.class).findAll();
                    List<HeroResponsesDto> listData = realm.copyFromRealm(list);
                    realm.close();
                    for (HeroResponsesDto d : listData) {
                        push(d);
                    }

                } catch (Exception e) {
                    Logger.error(TAG, ">>> Error pushALL:" + e);

                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    private void pushALLItems(final List<ItemDto> listData) {
        if (isPushALlItems) {
            return;
        }
        mPushCounter = 0;

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Realm realm = Realm.getDefaultInstance();
                try {
                    for (ItemDto d : listData) {
                        pushItem(d);
                    }

                } catch (Exception e) {
                    Logger.error(TAG, ">>> Error pushALL:" + e);

                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    private void pushALLHeroItems(final List<HeroResponsesDto> listData) {
        if (isPushALlHEROBUYItems) {
            return;
        }
        mPushCounter = 0;

        Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            try {
                for (HeroResponsesDto d : listData) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference();

                    reference.child(MsConst.TABLE_HERO_ITEMS).push().setValue(d)
                            .addOnSuccessListener(aVoid -> Logger.debug(TAG, ">>>" + "onSuccess pushItem:" + d.getToHeroId()))
                            .addOnFailureListener(e -> Logger.error(TAG, ">>> Error:" + "onFailure:" + e))
                    ;
                }

            } catch (Exception e) {
                Logger.error(TAG, ">>> Error pushALL:" + e);

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    private void pushALLHeroKillingMeeting(final List<HeroResponsesDto> listData) {
        if (isPushALlHEROKillingMeeting) {
            return;
        }
        mPushCounter = 0;

        Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            try {
                for (HeroResponsesDto d : listData) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference();

                    reference.child(MsConst.TABLE_HERO_KILLING_MEETING).push().setValue(d)
                            .addOnSuccessListener(aVoid -> Logger.debug(TAG, ">>>push " + mPushCounter++ + ":" + "onSuccess pushItem:" + d.getHeroId()))
                            .addOnFailureListener(e -> Logger.error(TAG, ">>> Error:" + "onFailure:" + e))

                    ;
                }

            } catch (Exception e) {
                Logger.error(TAG, ">>> Error pushALL:" + e);
                mPushCounter++;

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    private void pushALLHeroNormalVoices(final List<HeroResponsesDto> listData) {
        if (isPushALlNormalVoices) {
            return;
        }
        mPushCounter = 0;

        Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            try {
                for (HeroResponsesDto d : listData) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference();

                    reference.child(MsConst.TABLE_HERO_NORMAL_VOICE).push().setValue(d)
                            .addOnSuccessListener(aVoid -> Logger.debug(TAG, ">>>push " + mPushCounter++ + ":" + "onSuccess pushItem:" + d.toString()))
                            .addOnFailureListener(e -> Logger.error(TAG, ">>> Error:" + "onFailure:" + e))
                    ;
                }

            } catch (Exception e) {
                mPushCounter ++;
                Logger.error(TAG, ">>> Error pushALL:" + e);

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    int mPushCounter = 0;

    private void push(HeroResponsesDto heroBasicDto) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

        reference.child(MsConst.TABLE_LORD_RESPONSES).push().setValue(heroBasicDto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Logger.debug(TAG, ">>>" + "onSuccess HeroLordSounds:" + heroBasicDto.getHeroId() + ";mPushCounter:" + mPushCounter);
                        mPushCounter++;

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Logger.error(TAG, ">>> Error:" + "onFailure:" + e);
                        mPushCounter++;

                    }
                })
        ;

    }

    private void pushItem(ItemDto itemDto) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

        reference.child(MsConst.TABLE_ITEMS).push().setValue(itemDto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Logger.debug(TAG, ">>>" + "onSuccess pushItem:" + itemDto.getItemName());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Logger.error(TAG, ">>> Error:" + "onFailure:" + e);

                    }
                })
        ;

    }

    private void removeAll(Realm realm) {
        realm.beginTransaction();
        realm.delete(HeroResponsesDto.class);
        realm.commitTransaction();
    }

}
