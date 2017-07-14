package com.peter.zensleepfree.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.peter.zensleepfree.Activity.ForumActivity;
import com.peter.zensleepfree.Activity.HelpActivity;
import com.peter.zensleepfree.Activity.MoreActivity;
import com.peter.zensleepfree.Activity.UpgradeActivity;
import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch switchTips, switchDrive;
    private RelativeLayout btnFbLike, btnFollowTwitter, btnFollowInstagram, btnFeedback, btnSocialShare, btnHelp, btnUnlock, btnRestore, btnForum, btnMoreApp;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {

        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchTips = (Switch) view.findViewById(R.id.switch_settings_tips);
        btnFbLike = (RelativeLayout) view.findViewById(R.id.btn_settings_fb_like);
        btnFollowTwitter = (RelativeLayout) view.findViewById(R.id.btn_settings_twitter_follow);
        btnFollowInstagram = (RelativeLayout) view.findViewById(R.id.btn_settings_instagram_follow);
        btnFeedback = (RelativeLayout) view.findViewById(R.id.btn_settings_feedback);
        btnSocialShare = (RelativeLayout) view.findViewById(R.id.btn_settings_social_sharing);
        btnHelp = (RelativeLayout) view.findViewById(R.id.btn_settings_help);
        btnUnlock = (RelativeLayout) view.findViewById(R.id.btn_settings_unlock);
//        btnRestore = (RelativeLayout) view.findViewById(R.id.btn_settings_restore);
        btnForum = (RelativeLayout) view.findViewById(R.id.btn_settings_forums);
        btnMoreApp = (RelativeLayout) view.findViewById(R.id.btn_settings_more_app);
//        switchDrive = (Switch) view.findViewById(R.id.switch_settings_drive_sync);

        btnFbLike.setOnClickListener(this);
        btnFollowTwitter.setOnClickListener(this);
        btnFollowInstagram.setOnClickListener(this);
        btnFeedback.setOnClickListener(this);
        btnSocialShare.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        btnUnlock.setOnClickListener(this);
//        btnRestore.setOnClickListener(this);
        btnForum.setOnClickListener(this);
        btnMoreApp.setOnClickListener(this);

        switchTips.setOnCheckedChangeListener(this);
//        switchDrive.setOnCheckedChangeListener(this);
//        switchDrive.setChecked(Utils.getDriveSync(getActivity()));

        iniUI();

        return view;
    }

    private void iniUI() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_settings_fb_like:
                showPrefieldPopUp();
                break;
            case R.id.btn_settings_twitter_follow:
                followUsOnTwitter();
                break;
            case R.id.btn_settings_instagram_follow:
                followUsOnInstagram();
                break;
            case R.id.btn_settings_feedback:
                sendFeedback();
                break;
            case R.id.btn_settings_social_sharing:
                share();
                break;
            case R.id.btn_settings_help:
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_settings_unlock:
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
                break;
//            case R.id.btn_settings_restore:
//                break;
            case R.id.btn_settings_forums:
                startActivity(new Intent(getActivity(), ForumActivity.class));
                break;
            case R.id.btn_settings_more_app:
                if (Utils.hasInternetConnection(getContext())) {
                    startActivity(new Intent(getActivity(), MoreActivity.class));
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getResources().getString(R.string.no_intertnet_connections))
                            .setMessage(getResources().getString(R.string.pls_check_intertnet_connections))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (buttonView == switchDrive) {
//            Utils.setDriveSync(getActivity(), isChecked);
//            if (!isChecked) {
//                GoogleDriveClient.getDriveClient(getActivity()).disconnectDrive();
//            } else {
//                GoogleDriveClient.getDriveClient(getActivity()).connectDrive();
//            }
//        } else {
//
//        }
    }

    private void showPrefieldPopUp() {

        Intent intent;

        try {
            getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + Constant.kFacebookId));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.kFacebookLink));
        }

        getActivity().startActivity(intent);
    }

    private void followUsOnTwitter()    {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=" + Constant.kTwitterId));
            startActivity(intent);

        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constant.kTwitterLink)));
        }
    }

    private void followUsOnInstagram()  {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (getContext().getPackageManager().getPackageInfo("com.instagram.android", 0) != null) {
                intent.setData(Uri.parse("http://instagram.com/_u/" + Constant.kInstagramId));
                intent.setPackage("com.instagram.android");
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            intent.setData(Uri.parse(Constant.kInstagramLink));
        }

        startActivity(intent);
    }

    private void sendFeedback() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_app)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_feedback));

        PackageInfo pInfo = null;
        String versionName = "";
        int versionCode = 0;
        try {
            pInfo = getContext().getPackageManager().getPackageInfo("com.zenlabs.sleepsounds", 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String text = getResources().getString(R.string.feedback_app_name) + "\n" + getResources().getString(R.string.app_version) + " " + versionCode + "/" + versionName + "\n" + getResources().getString(R.string.device_model) + " " + Build.MANUFACTURER.toUpperCase() + ", " + Build.MODEL + "\n" + getResources().getString(R.string.os_version) + " " + Build.VERSION.RELEASE;

        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.send_email)));
    }

    private void share()    {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//                    sharingIntent.setType("message/rfc822");
//                    sharingIntent.setType("text/html");
        String shareBody = getResources().getString(R.string.share_text);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
        sharingIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, );

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
