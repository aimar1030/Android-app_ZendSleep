package com.peter.zensleepfree.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peter.zensleepfree.CustomView.StrechVideoView;
import com.peter.zensleepfree.Model.App;
import com.peter.zensleepfree.Plistparser.Array;
import com.peter.zensleepfree.Plistparser.Dict;
import com.peter.zensleepfree.Plistparser.MyString;
import com.peter.zensleepfree.Plistparser.PList;
import com.peter.zensleepfree.Plistparser.PListXMLHandler;
import com.peter.zensleepfree.Plistparser.PListXMLParser;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import me.kiip.sdk.Kiip;

public class SplashActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private App featuredApp = new App();
    private String tip = "", quote = "";

    private ArrayList<App> apps;

    private boolean isTipsDownloaded = false;
    private boolean isAnimationFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        StrechVideoView introVideo = (StrechVideoView) this.findViewById(R.id.videoView);
        introVideo.setOnCompletionListener(this);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.opener;
        introVideo.setVideoURI(Uri.parse(path));
        introVideo.start();

        if (Utils.getMoreApps(this).equals("")) {
            new GetListOfTipsAsyncTask().execute();
        } else {
            isTipsDownloaded = true;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isAnimationFinished = true;
        goToTheNextScreen();
    }

    public class GetListOfTipsAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {

            try {
                PListXMLParser parser = new PListXMLParser();
                PListXMLHandler handler = new PListXMLHandler();
                parser.setHandler(handler);

                String plist_url = Constant.PLIST_URLS[Constant.STORE];
                String s = Utils.readFile(plist_url);
                parser.parse(s);

                String moreApps = "";

                if (Constant.STORE == Constant.AMAZON_STORE) {
                    s = Utils.readFile(Constant.AMAZON_PLIST);
                } else if (Constant.STORE == Constant.GOOGLE_STORE) {
                    s = Utils.readFile(Constant.GOOGLE_PLIST);
                }

                moreApps = Utils.readFile(Constant.PLIST_URLS[Constant.STORE]);
//                LogService.Log("GetListOfTipsAsyncTask", "moreApps: " + moreApps);
                Utils.setMoreApps(getBaseContext(), moreApps);
                parser.parse(moreApps);


                PList actualPList = ((PListXMLHandler) parser.getHandler())
                        .getPlist();

                Array featured_app_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("featured");

                featuredApp.setName(((Dict) featured_app_array.get(0))
                        .getConfiguration("name").getValue());

                featuredApp.setUrl(((Dict) featured_app_array.get(0))
                        .getConfiguration("url").getValue());

                featuredApp.setDesc(((Dict) featured_app_array.get(0))
                        .getConfiguration("description").getValue());

                featuredApp.setImage(((Dict) featured_app_array.get(0))
                        .getConfiguration("icon").getValue());

                Random rand = new Random();

                Array apps_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("apps");

//                LogService.Log("GetListOfTipsAsyncTask", "apps_array: " + apps_array);

                Array tips_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("tips");

//                LogService.Log("GetListOfTipsAsyncTask", "tips: " + tips_array);

                int rand_tip = rand.nextInt(tips_array.size());

                tip = ((MyString) tips_array.get(rand_tip)).getValue();

                Array quotes_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("quotes");

//                LogService.Log("GetListOfTipsAsyncTask", "quotes_array: " + quotes_array);

                int rand_quote = rand.nextInt(quotes_array.size());

                quote = ((MyString) quotes_array.get(rand_quote)).getValue();

                apps = new ArrayList<App>();


                for (int i = 0; i < apps_array.size(); i++) {

                    App app = new App();

                    app.setName(((Dict) apps_array.get(i)).getConfiguration(
                            "name").getValue());
                    app.setUrl(((Dict) apps_array.get(i)).getConfiguration(
                            "url").getValue());

                    app.setImage(((Dict) apps_array.get(i)).getConfiguration(
                            "icon").getValue());

                    apps.add(app);
                }

//                LogService.Log("GetListOfTipsAsyncTask","apps: "+apps.toString());

                return true;

            } catch (Throwable t) {
//                LogService.Log("GetListOfTipsAsyncTask", "Throwable: " + t.toString());
                t.printStackTrace();
                return false;
            }

        }

        protected void onPostExecute(Boolean result) {
            if (result) {

                Type arrayListType = new TypeToken<ArrayList<App>>() {
                }.getType();
                Gson tmp = new Gson();

                SharedPreferences preferences = getSharedPreferences(Utils.PREFERENCE_NAME, 1);

                preferences.edit().putString("tip", tip).commit();
                preferences.edit().putString("quote", quote).commit();

//                LogService.Log("TAG", "apps size " + apps.size());
                preferences.edit()
                        .putString("apps", tmp.toJson(apps, arrayListType))
                        .commit();

            }
            isTipsDownloaded = true;
            goToTheNextScreen();

        }
    }

    private void goToTheNextScreen() {
        if (isAnimationFinished && isTipsDownloaded) {
            Intent intent = new Intent(this, TabActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
