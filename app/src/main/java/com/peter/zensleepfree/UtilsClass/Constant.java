package com.peter.zensleepfree.UtilsClass;

/**
 * Created by peter on 6/20/16.
 */
public class Constant {

    public static final String[] ALARM_SOUNDS = { "Awake", "Chimes", "Morning", "Peace", "Tranquil" };
    public static final String[] ALARM_PAID_SOUNDS = { "Bells", "Blue", "Breathing", "Clean",
                                    "Coffee", "Cool", "Dance", "Ding", "Distant", "Dreaming", "Drifter","Easy","Flow", "Glass", "Guitar",
                                    "Happy", "Mellow", "Nice", "Piano", "Pleasant", "Ready", "Serene", "Shine", "Slow", "Smooth", "Space",
                                    "String", "Sunshine", "Water", "Wooden" };

    public static final int BUFFER_INTERVAL_SECONDS = 60 * 6;

    // Sleep conditions
    public static final int SLEEP_STATUS_NONE = 0;
    public static final int SLEEP_STATUS_WORKED_OUT = 1;
    public static final int SLEEP_STATUS_STRESS = 2;
    public static final int SLEEP_STATUS_NOTMYBED = 4;
    public static final int SLEEP_STATUS_CAFFEINE = 8;
    public static final int SLEEP_STATUS_MISSEDMEAL = 16;
    public static final int SLEEP_STATUS_ALCOHOL = 32;

    //Sleep moods
    public static final int SLEEP_MOOD_1 = 1;
    public static final int SLEEP_MOOD_2 = 2;
    public static final int SLEEP_MOOD_3 = 3;
    public static final int SLEEP_MOOD_4 = 4;
    public static final int SLEEP_MOOD_5 = 5;

    //Sleep dreams
    public static final int SLEEP_DREAM_NONE = 0;
    public static final int SLEEP_DREAM_BAD = 1;
    public static final int SLEEP_DREAM_NEUTRAL = 2;
    public static final int SLEEP_DREAM_PLEASANT =3;


    public static final float[][][] TIME_COLOR_TABLE = {
            {{  2,  12,  17}, { 43,  55,  62}},     //  0  O'clock
            {{  2,  12,  17}, { 43,  55,  62}},     //  5  O'clock
            {{ 42,  81, 129}, {176,  94,  21}},     //  8  O'clock
            {{  1, 104, 142}, {120, 112,  97}},     //  10 O'clock
            {{  1, 104, 142}, {120, 112,  97}},     //  17 O'clock
            {{  2,  12,  17}, { 43,  55,  62}},     //  20 O'clock
            {{  2,  12,  17}, { 43,  55,  62}},     //  24 O'clock
        };

    public static final float TIME_INDEX_TABLE[] =
            {
                    0,
                    5,
                    8,
                    10,
                    17,
                    20,
                    24
            };

    public static final int NUM_TIME_INDEX = 7;

    public static final String kFacebookId = "343577675683891";
    public static final String kFacebookLink = "https://www.facebook.com/zenlabsfitness";
    public static final String kTwitterLink = "https://twitter.com/zenlabsfitness";
    public static final String kTwitterId = "zenlabsfitness";
    public static final String kInstagramLink = "https://www.instagram.com/zenlabsfitness";
    public static final String kInstagramId = "zenlabsfitness";

    public static final int STORE = 0;
    public static final int GOOGLE_STORE = 0;
    public static final int AMAZON_STORE = 1;

    public static final String GOOGLE_PLIST = "http://www.c25kfree.com/config/WSLMoreAppsData_C25K_Google.plist";
    public static final String AMAZON_PLIST = "http://www.c25kfree.com/config/WSLMoreAppsData_C25K_Amazon.plist";
    public static final String[] PLIST_URLS = new String[]{GOOGLE_PLIST,
            AMAZON_PLIST};

    public static final String jID = "id";
    public static final String jSTATUS = "status";
    public static final String jSTART_TIME = "start_time";
    public static final String jELAPSED_TIME = "elapsed_time";
    public static final String jSTAGES_LOT = "stages_lot";
    public static final String jMOOD = "mood";
    public static final String jDREAM = "dream";
    public static final String jNOTE = "note";
    public static final String jSTAGES = "stages";
    public static final String jSTAGE = "stage";

    public static final String SHARED_PREFERENCES_KIIP_REWARDS = "SHARED_PREFERENCES_KIIP_REWARDS";
    public static final String KIIP_MOMENT_FORUM_VIEWED = "forum_screen_opened";
    public static final String KIIP_MOMENT_NOTE_VIEWED = "note_screen_opened";
}
