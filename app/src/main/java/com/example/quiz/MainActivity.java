package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ=1;
    int highscore;
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String KEY_HIGHSCORE="keyHighscore";
    private  TextView textViewHighscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         textViewHighscore= findViewById(R.id.text_view_highscore);
         loadHighscore();
        Button buttonStartQuiz= findViewById(R.id.button_start_quiz);

        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }
    private void startQuiz()
    {
        Intent intent=new Intent(MainActivity.this,secondactivity.class);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
         super.onActivityResult(requestCode,resultCode,data);
         if(requestCode==REQUEST_CODE_QUIZ)
         {
             if(resultCode==RESULT_OK)
             {
                 int score= data.getIntExtra(secondactivity.EXTRA_SCORE,  0);
                 {
                     if(score > highscore )
                     {
                         updateHighscore(score);
                     }
                 }
             }
         }

    }
    private void loadHighscore()
    {
        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore= prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("HighScore: " + highscore);

    }
    private void updateHighscore(int highscoreNew)
    {
        highscore=highscoreNew;
        textViewHighscore.setText("HighScore: " + highscore);

        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}
