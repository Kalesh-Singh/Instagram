package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Intent intent = getIntent();
        final String activeUsername = intent.getStringExtra("username");

        setTitle(activeUsername + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<>("Image");
        query.whereEqualTo("username", activeUsername);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.i("Feed", "Retrieved");
                    if (objects.size() > 0) {
                        Log.i("Retrieved", Integer.toString(objects.size()) + " objects");
                        for (final ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {
                                        Bitmap bitmap =
                                                BitmapFactory.decodeByteArray(data, 0, data.length);

                                        ImageView imageView =
                                                new ImageView(getApplicationContext());

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));

//                                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                                        imageView.setImageBitmap(bitmap);


                                        TextView textViewPoster = new TextView(getApplicationContext());
                                        textViewPoster.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));


                                        String poster = "Posted by: " + activeUsername;
                                        textViewPoster.setText(poster);

                                        textViewPoster.setTextSize(17);
                                        textViewPoster.setTextColor(Color.parseColor("#000000"));


                                        TextView textViewDate = new TextView(getApplicationContext());
                                        textViewDate.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));

                                        Date date = object.getCreatedAt();
                                        SimpleDateFormat simpleDateFormat =
                                                new SimpleDateFormat(
                                                        "E MM/dd/yyyy 'at' hh:mm:ss a zzz",
                                                        Locale.getDefault()
                                                );

                                        String datePosted = "On: " + simpleDateFormat.format(date);
                                        textViewDate.setText(datePosted);

                                        textViewDate.setTextSize(14);
                                        textViewDate.setTextColor(Color.parseColor("#00bfff"));


                                        TextView textViewSpace = new TextView(getApplicationContext());
                                        textViewSpace.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        textViewSpace.setTextSize(17);
                                        textViewSpace.setText(" \n \n");

                                        linearLayout.addView(textViewPoster);
                                        linearLayout.addView(textViewDate);
                                        linearLayout.addView(imageView);
                                        linearLayout.addView(textViewSpace);

                                        Log.i("Image", "Added to image view");

                                    }
                                }
                            });
                        }
                    } else {
                        Log.i("Objects retirved", "0");
                        Toast.makeText(UserFeedActivity.this, "This feed is currently empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("Feed", "Failed to retrieve");
                    Toast.makeText(UserFeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
