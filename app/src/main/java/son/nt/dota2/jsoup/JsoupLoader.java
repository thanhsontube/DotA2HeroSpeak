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
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.utils.Logger;
import timber.log.Timber;

/**
 * Created by sonnt on 9/20/16.
 */
public class JsoupLoader {
    public static final String TAG = JsoupLoader.class.getSimpleName();
    public static final String HERO_ICON = "http://dota2.gamepedia.com/Heroes_by_release";
    public static final String HERO_AVATAR = "http://dota2.gamepedia.com/Heroes";
    public static final String HERO_x_LORD = "http://dota2.gamepedia.com/%1$s/Lore";
    public static final String HERO_LORD = "http://dota2.gamepedia.com/Anti-Mage/Lore";
    public static final String HERO_LORD2 = "http://dota2.gamepedia.com/Underlord/Lore";
    public static final String HERO_RESPONSE = "http://dota2.gamepedia.com/Queen_of_Pain/Responses";
    public static final String HERO_RESPONSE2 = "http://dota2.gamepedia.com/Drow_Ranger/Responses";
    public static final String HERO_RESPONSE_Crystal = "http://dota2.gamepedia.com/Crystal_Maiden/Responses";


    public static final String HERO_RESPONSE3 = "http://dota2.gamepedia.com/index.php?title=Drow_Ranger/Responses&action=edit";

    public void withGetHeroBasic_Avatar_Description() {
        new GetHeroBasic_Avatar_Description().execute();
    }

    public void withGetHeroBasic_Lord() {
        Logger.debug(TAG, ">>>" + "withGetHeroBasic_Lord");
//        new GetHeroBasic_Lord().execute("Anti-Mage");

//        getLordVoice("Anti-Mage");
        getAllLords();
    }

    public void withGetHeroBasic_Response() {
        Logger.debug(TAG, ">>>" + "withGetHeroBasic_Response");
        new GetHeroBasic_Responses().execute();
    }

    public void withGetHeroBasic_PUSHLord() {
//        pushALL();
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
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Logger.debug(TAG, ">>>" + "onSuccess 2");

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


//                Realm realm = Realm.getDefaultInstance();
//                realm.beginTransaction();
//                realm.delete(HeroBasicDto.class);
//                realm.commitTransaction();
//                for (HeroBasicDto hero : heroes) {
//                    realm.beginTransaction();
//                    realm.copyToRealm(hero);
//                    realm.commitTransaction();
//                }
//                realm.close();


            } catch (Exception e) {
                Logger.error(TAG, ">>> Error:" + e);
            }
            return null;
        }
    }

    class GetHeroBasic_Lord extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String heroId = params[0];
                String path = "http://dota2.gamepedia.com/" + heroId + "/Lore";
//                Document document = Jsoup.connect(HERO_LORD).get(); //hack antimage
                Document document = Jsoup.connect(path).get(); //hack antimage

                Elements mains = document.select("div[id=mw-content-text]").get(0).getElementsByTag("ul");
                Logger.debug(TAG, ">>>" + "mains:" + mains.size());


                HeroResponsesDto dto;
                for (Element element : mains) { //A1
                    try {
                        dto = new HeroResponsesDto();

                        Logger.debug(TAG, ">>>" + "-----");
                        Element before = element.previousElementSibling();
                        if (before.text().contains("Allies meeting")) {
                            Logger.debug(TAG, ">>>" + "***Allies meeting");
                            final Elements elementsByTag = element.getElementsByTag("li");
                            for (Element d : elementsByTag) {
                                try {
                                    Logger.debug(TAG, ">>>" + "d:" + d.ownText());
                                    String mp3 = d.select("a[class=sm2_button]").attr("href");
                                    Logger.debug(TAG, ">>>" + "mp3:" + mp3);

                                    String id = d.select("img[alt]").get(0).parent().attr("href");
                                    Logger.debug(TAG, ">>>" + "id:" + id);
                                } catch (Exception e) {
                                    Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord elementsByTag Allies meeting:" + e);
                                }

                            }


                        }
                        if (before.text().contains("Enemies killing")) {
                            Logger.debug(TAG, ">>>" + "***Enemies killing");
                            final Elements href = element.getElementsByAttribute("href");
                            final Elements elementsByTag = element.getElementsByTag("li");
                            for (Element d : elementsByTag) { //A2
                                try {
                                    Logger.debug(TAG, ">>>" + "d:" + d.ownText());
                                    String mp3 = d.select("a[class=sm2_button]").attr("href");
                                    Logger.debug(TAG, ">>>" + "mp3:" + mp3);

                                    String id = d.select("img[alt]").get(0).parent().attr("href");
                                    Logger.debug(TAG, ">>>" + "id:" + id);
                                } catch (Exception e) {
                                    Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord elementsByTag Enemies killing:" + e);
                                }


                            }


                        }
                        if (before.text().contains("Others")) {
                            Logger.debug(TAG, ">>>" + "***Others");
                            final Elements href = element.getElementsByAttribute("href");
                            final Elements elementsByTag = element.getElementsByTag("li");
                            for (Element d : elementsByTag) {
                                try {
                                    Logger.debug(TAG, ">>>" + "d:" + d.ownText());
                                    String mp3 = d.select("a[class=sm2_button]").attr("href");
                                    Logger.debug(TAG, ">>>" + "mp3:" + mp3);

                                    String id = d.select("img[alt]").get(0).parent().attr("href");
                                    Logger.debug(TAG, ">>>" + "id:" + id);
                                } catch (Exception e) {
                                    Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord elementsByTag Others:" + e);
                                }


                            }

                        }


                    } catch (Exception e) {
                        Logger.error(TAG, ">>> Error:" + "GetHeroBasic_Lord A1 :" + e);
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //http://dota2.gamepedia.com/Queen_of_Pain/Responses
    class GetHeroBasic_Responses extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            getResponseWithHtml();
//            getResponseWithJsoup();
            return null;
        }
    }

    private void getResponseWithHtml() {
        try {
            CleanerProperties props = new CleanerProperties();

            props.setTranslateSpecialEntities(true);
            props.setTransResCharsToNCR(true);
            props.setOmitComments(true);

            TagNode tagNode = new HtmlCleaner(props).clean(
                    new URL(HERO_RESPONSE_Crystal));
            String xPath = "//div[@id='mw-content-text']";
            Object[] data = tagNode.evaluateXPath(xPath);
            TagNode nodeA = (TagNode) data[0];
            List<TagNode> tagNodes = nodeA.getChildTagList();
            Logger.debug(TAG, ">>>" + "tagNodes:" + tagNodes.size());
            int i = 1;

            boolean isProcess = false;
            for (TagNode tag : tagNodes) {
//                Logger.debug(TAG, ">>>" + "tag:" + i + ":" + tag.getText()
//                        + ";hasChildren:" + tag.hasChildren()
//                        + ";getName:" + tag.getName()
//                        + ";tag.hasAttribute(\"id\"):" + tag.hasAttribute("id")
//                        + ";class:" + tag.hasAttribute("class"));
                if (isProcess) {
                    workWithImage(tag);
                    return;
                }

                if (tag.getName().contains("h2")) {
//                    Logger.debug(TAG, ">>>" + i + " h2:" + tag.getText());
                    if (tag.getText().toString().contains("Killing an enemy")) {
//                        workWithImage(tag);
                        isProcess = true;
                    }
                }

//                if (tag.getName().contains("h2")) {
//                    String group = tag.getText().toString().trim();
//                    TagNode responseGroup = tagNodes.get(i + 1);
//                    processResponse(group, responseGroup);
//                }

//                if (tag.getText().toString().contains("Killing a specific enemy")) {
//                    String group = tag.getText().toString().trim();
//                    TagNode responseGroup = tagNodes.get(i + 1);
//                    processResponseIcon(group, responseGroup);
//                }

//
//                Logger.debug(TAG, ">>>" + "tag:" + tag.getName());
//                if (tag.getName().equals("h2")) {
//                    Logger.debug(TAG, ">>>" + "h2:" + tag.getText());
//                }
//                if (tag.getName().equals("div")) {
////                    Logger.debug(TAG, ">>>" + tag.getText());
//
//                    if (tag.getText().toString().contains("Killing a specific enemy")) {
//                        String group = tag.getText().toString().trim();
//                        TagNode responseGroup = tagNodes.get(i + 1);
//                        processResponseIcon(group, responseGroup);
//                    }
//
//                }

//                Logger.debug(TAG, ">>>" + tag.getText());
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void workWithImage(TagNode tagNode) {
        try {

            Logger.debug(TAG, ">>>" + "workWithImage:" + tagNode.getChildTagList().size());
            int i = 0;
            for (TagNode tag : tagNode.getChildTagList()) {


                Logger.debug(TAG, ">>>" + "*********i:*********" + i + "*********");
                Logger.debug(TAG, ">>>" + "tag:" + tag.getName());
                if (tag.getName().contains("p")) {
                    Logger.debug(TAG, ">>>" + "text:" + tag.getText());
//                    if (tag.getText().toString().contains("Killing a specific enemy")) {
//                        getKillingEnemy(tagNode.getChildTagList().get(i + 1));
//                        return;
//                    }
                }

                i++;

            }

//            TagNode tag = tagNode.getChildTagList().get(1);
//            Logger.debug(TAG, ">>>" + "tag:" + tag. );
//            Logger.debug(TAG, ">>>" + "ok:" + tagNode.getChildTagList().get(1).getChildTagList().size());
//            for (TagNode tag : tagNode.getChildTagList()) {
//                Logger.debug(TAG, ">>>" + "tag:" + tag.getText());
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getKillingEnemy(TagNode nodeA) {

        try {
            Logger.debug(TAG, ">>>" + "getKillingEnemy:" + nodeA.getChildTagList().size());
            for (TagNode tag : nodeA.getChildTagList()) {
                String text = tag.getText().toString().replace("Play", "").replace("u ", "").trim();
                Logger.debug(TAG, ">>>" + "tag text:" + text);

//                Logger.debug(TAG, ">>>" + "size:" + tag.getChildTagList().size());
                TagNode tagMp3 = tag.getChildTagList().get(0);

                String mp3 = tagMp3.getAttributeByName("href");
                Logger.debug(TAG, ">>>" + "mp3:" + mp3);

                if (tag.getChildTagList().size() > 1) {
                    TagNode tagHero = tag.getChildTagList().get(1);
                    String rivalName = tagHero.getAttributeByName("href");
                    rivalName = rivalName.substring(0, rivalName.lastIndexOf("?version"));
                    Logger.debug(TAG, ">>>" + "toHeroName:" + rivalName);
                }


            }


        } catch (Exception e) {
            Logger.error(TAG, ">>> Error:" + "error:" + e.toString());
        }
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

    private void getAllLords() {
        getLordVoice();
//        Realm realm = Realm.getDefaultInstance();
//        final List<HeroBasicDto> dtoBasic = realm.where(HeroBasicDto.class)
//                .findAll();
//
//        final List<HeroBasicDto> heroBasicDto = realm.copyFromRealm(dtoBasic);
//        realm.close();
//
//        int i = 0;
//        for (HeroBasicDto d : heroBasicDto) {
//
//            getLordVoice(d.heroId);
//            i++;
////            if (i > 10) {
////                break;
////            }
//
//        }
    }


    //using rx, the old one new GetHeroBasic_Lord().execute("Anti-Mage")
    private void getLordVoice() {
        Logger.debug(TAG, ">>>" + "************* getLordVoice *******:");
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
        count = 0;

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

    int count = 0;

    private void push(HeroResponsesDto heroBasicDto) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

        reference.child(MsConst.TABLE_LORD_RESPONSES).push().setValue(heroBasicDto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Logger.debug(TAG, ">>>" + "onSuccess HeroLordSounds:" + heroBasicDto.getHeroId() + ";count:" + count);
                        count++;

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Logger.error(TAG, ">>> Error:" + "onFailure:" + e);
                        count++;

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
