package com.example.proekt;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    private static boolean firstLaunch=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBase.createInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (firstLaunch) {
            Toast.makeText(MainActivity.this, getString(R.string.firstTimeMap), Toast.LENGTH_LONG).show();
            MainActivity.firstLaunch=false;
        }

        ImageButton imgButCreateNote = findViewById(R.id.mainCreateNote);
        CreateNoteFragment createNoteFragment = new CreateNoteFragment();

        fragmentManager = getSupportFragmentManager();

        imgButCreateNote.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.mainActivityFrameLayout, createNoteFragment)
                    .commit();


        });
        for (int i = DataBase.getInstance().getAll().size() - 1; i >= 0; i--) {
            Note note = DataBase.getInstance().getAll().get(i);
            String str1, str2;

            if (note.getTitle().length() > 15) {
                str1 = note.getTitle().substring(0, 15);
                str1+="...";
            } else {
                str1 = note.getTitle();
            }
            if (note.getText().length() > 20) {
                str2 = note.getText().substring(0, 20);
                str2+="...";
            } else {
                str2 = note.getText();
            }
            makeTabOnMainActivity(str1, str2, note.getDate(),note);
        }
    }

    public void makeTabOnMainActivity(String title, String description, String date,Note note) {
        LinearLayout linearLayout = findViewById(R.id.linearMain);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        153, // Высота 153dp
                        getResources().getDisplayMetrics()
                )
        );
        layoutParams.setMargins(20, 50, 20, 0);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackgroundColor(Color.parseColor("#E1AFEA"));

        TextView titleTextView = new TextView(this);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleParams.setMargins(1, 1, 0, 0);
        titleTextView.setLayoutParams(titleParams);
        titleTextView.setText(title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        relativeLayout.addView(titleTextView);

        TextView descriptionTextView = new TextView(this);
        RelativeLayout.LayoutParams descriptionParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        descriptionParams.addRule(RelativeLayout.BELOW, titleTextView.getId());
        descriptionParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        descriptionParams.setMargins(9, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                30,
                getResources().getDisplayMetrics()
        ), 0, 0);
        descriptionTextView.setLayoutParams(descriptionParams);
        descriptionTextView.setText(description);
        descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        relativeLayout.addView(descriptionTextView);

        TextView dateTextView = new TextView(this);
        RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        dateParams.addRule(RelativeLayout.BELOW, descriptionTextView.getId());
        dateParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        dateParams.setMargins((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                218,
                getResources().getDisplayMetrics()
        ), (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                30,
                getResources().getDisplayMetrics()
        ), 0, 0);
        dateTextView.setLayoutParams(dateParams);
        dateTextView.setText(date);
        dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        relativeLayout.addView(dateTextView);

        Button invisibleButton = new Button(this);
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        invisibleButton.setLayoutParams(buttonParams);
        invisibleButton.setAlpha(0f);
        relativeLayout.addView(invisibleButton);


        invisibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteFragment noteFragment = new NoteFragment();
                Bundle args = new Bundle();
                args.putSerializable("note", note);
                noteFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainActivityFrameLayout, noteFragment)
                        .addToBackStack(null)
                        .commit();

            }

        });

        linearLayout.addView(relativeLayout);
    }
}
