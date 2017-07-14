package com.peter.zensleepfree.Fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment implements View.OnClickListener {

    private Button btnFbConnect, btnTwitterConnect, btnInstaConnect, btnLike, btnTFollow, btnIFollow, btnShareTipThanks, btnShareTipNever;
    private EditText edtComment;
    private ImageView btnFinish;
    private Typeface typeface;
    private RelativeLayout tipContainer;
    private LinearLayout shareContainer;

    private static SleepDBModel sleepData;

    private static CustomButtonClickListener callBack;

    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance(CustomButtonClickListener buttonCallback, SleepDBModel data) {

        ShareFragment fragment = new ShareFragment();
        callBack = buttonCallback;
        sleepData = data;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        btnFbConnect = (Button) view.findViewById(R.id.btn_share_fb_connect);
        btnTwitterConnect = (Button) view.findViewById(R.id.btn_share_twitter_connect);
        btnInstaConnect = (Button) view.findViewById(R.id.btn_share_insta_connect);
        btnLike = (Button) view.findViewById(R.id.btn_share_fb_like);
        btnTFollow = (Button) view.findViewById(R.id.btn_share_follow_twitter);
        btnIFollow = (Button) view.findViewById(R.id.btn_share_follow_insta);
        btnShareTipNever = (Button) view.findViewById(R.id.btn_share_tip_never);
        btnShareTipThanks = (Button) view.findViewById(R.id.btn_share_tip_thanks);

        edtComment = (EditText) view.findViewById(R.id.edt_share_comment);
        btnFinish = (ImageView) view.findViewById(R.id.btn_finish_share);

        tipContainer = (RelativeLayout) view.findViewById(R.id.tip_container);
        shareContainer = (LinearLayout) view.findViewById(R.id.share_container);

        tipContainer.setVisibility(View.GONE);
        btnFinish.setOnClickListener(this);
        btnTwitterConnect.setOnClickListener(this);
        btnIFollow.setOnClickListener(this);
        btnTFollow.setOnClickListener(this);
        btnFbConnect.setOnClickListener(this);
        btnInstaConnect.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnShareTipNever.setOnClickListener(this);
        btnShareTipThanks.setOnClickListener(this);

        initUI();
        return view;
    }

    private void initUI() {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Light.ttf");
        btnLike.setTypeface(typeface);
        btnTwitterConnect.setTypeface(typeface);
        btnIFollow.setTypeface(typeface);
        btnTFollow.setTypeface(typeface);
        btnFbConnect.setTypeface(typeface);
        btnInstaConnect.setTypeface(typeface);
        edtComment.setTypeface(typeface);
        btnShareTipNever.setTypeface(typeface);
        btnShareTipThanks.setTypeface(typeface);

        saveBitmap(renderImage());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_fb_connect:
                if (!Utils.getShowShareTip(getActivity())) {
                    showTip();
                } else {
                    share();
                }
                break;
            case R.id.btn_share_twitter_connect:
                share();
                break;
            case R.id.btn_share_insta_connect:
                share();
                break;
            case R.id.btn_share_fb_like:
                showPrefieldPopUp();
                break;
            case R.id.btn_share_follow_twitter:
                followUsOnTwitter();
                break;
            case R.id.btn_share_follow_insta:
                followUsOnInstagram();
                break;
            case R.id.btn_finish_share:
                callBack.onFinishShareClicked(sleepData);
                break;
            case R.id.btn_share_tip_thanks:
                tipContainer.setVisibility(View.GONE);
                shareContainer.setVisibility(View.VISIBLE);
                share();
                break;
            case R.id.btn_share_tip_never:
                tipContainer.setVisibility(View.GONE);
                shareContainer.setVisibility(View.VISIBLE);
                Utils.setShowShareTip(getActivity(), true);
                share();
                break;
        }
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

    private Bitmap renderImage() {
        Bitmap bitmap;
        Bitmap rBitmap;

        bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.insta);
        Bitmap.Config config = bitmap.getConfig();
        rBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);

        Canvas canvas = new Canvas(rBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint paint = new Paint();
        paint.setTextSize(64);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#80ffffff"));
        paint.setTypeface(typeface);
        String duration = Utils.getHoursMinutes(sleepData.getElapsedSec());
        String effi = String.valueOf((int)(sleepData.getEfficiency()*10000)/100) + "%";
        float textWidth = paint.measureText(duration);
        canvas.drawText(duration, bitmap.getWidth()/2 - textWidth/2, 256, paint);
        paint.setTextSize(84);
        textWidth = paint.measureText(effi);
        canvas.drawText(effi, bitmap.getWidth()/2 - textWidth/2, bitmap.getHeight()/2 + 242, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(64);
        paint.setColor(Color.parseColor("#80ffffff"));

        canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2 + 200, bitmap.getWidth() * 0.2f, paint);
        if (sleepData.getEfficiency() >= 0.9) {
            paint.setColor(Color.parseColor("#39be2b"));
        } else if (sleepData.getEfficiency() >= 0.8) {
            paint.setColor(Color.parseColor("#20b199"));
        } else if (sleepData.getEfficiency() >= 0.7) {
            paint.setColor(Color.parseColor("#fdb914"));
        } else {
            paint.setColor(Color.parseColor("#f04b5f"));
        }
        final RectF oval = new RectF();
        oval.set(bitmap.getWidth()/2 - bitmap.getWidth() * 0.2f, bitmap.getHeight()/2 + 200 - bitmap.getWidth() * 0.2f, bitmap.getWidth()/2 + bitmap.getWidth() * 0.2f, bitmap.getHeight()/2 + 200 + bitmap.getWidth() * 0.2f);
        int endAngle = (int) (360 * sleepData.getEfficiency());
        canvas.drawArc(oval, 270, endAngle, false, paint);
        return rBitmap;
    }

    private void saveBitmap(Bitmap bitmap) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(extStorageDirectory + "/share.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void share() {
        File image = new File(Environment.getExternalStorageDirectory().toString() + "/share.png");
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = edtComment.getText().toString();

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("comment", shareBody);
        clipboard.setPrimaryClip(clip);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
        sharingIntent.setType("image/*");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void showTip() {
        shareContainer.setVisibility(View.INVISIBLE);
        tipContainer.setVisibility(View.VISIBLE);
    }
}
