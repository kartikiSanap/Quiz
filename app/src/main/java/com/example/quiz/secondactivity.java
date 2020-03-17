package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;
import android.content.res.ColorStateList;
import android.widget.Toast;
import android.graphics.Color;
import java.util.*;
import android.content.Intent;
import android.os.CountDownTimer;

public class secondactivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extrascore";
    private static final long COUNTDOWN_IN_MILLIS=30000;
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private  TextView textViewCountDown;
    private TextView rightwrong;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;
    private List<Question> questionList;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private  long timeLeftInMillis;

    private ColorStateList  textColorDefaultRb;
    private int questionCounter=0;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private  long backPressedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondactivity);
        textViewQuestion= findViewById(R.id.text_view_question);
        textViewScore= findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown= findViewById(R.id.text_view_countdown);
        rightwrong= findViewById(R.id.text_view_rightwrong);
        rbGroup= findViewById(R.id.radio_group);
        rb1= findViewById(R.id.radio_button1);
        rb2= findViewById(R.id.radio_button2);
        rb3= findViewById(R.id.radio_button3);
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);

        rbGroup.clearCheck();
        buttonConfirmNext= findViewById(R.id.button_confirm_next);

         textColorDefaultCd= textViewCountDown.getTextColors();
       QuizDbHelper dbHelper = new QuizDbHelper(this);
       questionList= dbHelper.getAllQuestions();
       questionCountTotal = questionList.size();
       Collections.shuffle(questionList);
       showNextQuestion();
       buttonConfirmNext.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               if(!answered){
                   if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked())
                   {
                         checkAnswer();
                   }
                   else
                   {
                       Toast.makeText(secondactivity.this, "Please select an answer" , Toast.LENGTH_SHORT).show();
                   }
               }
               else
               {

                   showNextQuestion();
               }
           }
       });
    }
    private void showNextQuestion()
    {
        rightwrong.setText("Think..");
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rbGroup.clearCheck();
        if(questionCounter < questionCountTotal)
        {

            currentQuestion=questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);

            answered = false;
            buttonConfirmNext.setText("Confirm");

             timeLeftInMillis=COUNTDOWN_IN_MILLIS;
             startCountDown();

        } else
        {
            finishQuiz();
        }



    }
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
        private void updateCountDownText()
        {
            if(timeLeftInMillis==0 && answered==false)
            {
                Toast.makeText(this,"Time Over!", Toast.LENGTH_SHORT).show();

            }
            int minutes=(int)(timeLeftInMillis/1000)/60;
            int seconds=(int) (timeLeftInMillis/1000)%60;
            String timeFormated= String.format(Locale.getDefault(), "%02d:%02d", minutes,seconds);
            textViewCountDown.setText(timeFormated);
            if(timeLeftInMillis<10000)
            {
                textViewCountDown.setTextColor(Color.RED);
            }
            else
            {
                textViewCountDown.setTextColor(Color.BLACK);
            }
        }

    private void checkAnswer()
    {
         answered=true;
         countDownTimer.cancel();
         RadioButton rbSelected= findViewById(rbGroup.getCheckedRadioButtonId());
         int answerNr= rbGroup.indexOfChild(rbSelected)+1;
         if(answerNr == currentQuestion.getAnswerNr()){
             score++;
             rightwrong.setText("Congrats, Right Answer!");

             textViewScore.setText("Score: " + score);
         }
         else
         {

             rightwrong.setText("Oops, Wrong Answer!");


         }
         showSolution();
    }
    private void showSolution()
    {
        if(timeLeftInMillis==0)
        {
            rightwrong.setText("Think...");
        }
        switch(currentQuestion.getAnswerNr())
        {
            case 1:
                rb1.setTextColor(Color.GREEN);

                textViewQuestion.setText("Correct Answer:"+" "+currentQuestion.getOption1());


                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Correct Answer:"+" "+ currentQuestion.getOption2());

                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Correct Answer:"+" "+ currentQuestion.getOption3());

                break;

        }

        if(questionCounter < questionCountTotal)
        {
            buttonConfirmNext.setText("Next");

        }
        else
        {
            buttonConfirmNext.setText("Finish");
        }

    }
    private void finishQuiz(){
        Intent resultIntent= new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    @Override
    public void onBackPressed()
    {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }
        else
        {
            Toast.makeText(this,"Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime=System.currentTimeMillis();
        super.onDestroy();
        if(countDownTimer!=null) {
            countDownTimer.cancel();
        }
    }
}

