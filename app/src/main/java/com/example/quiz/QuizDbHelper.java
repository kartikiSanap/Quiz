package com.example.quiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.example.quiz.QuizContract.*;
import android.content.ContentValues;
import java.util.*;
import android.database.Cursor;

public class QuizDbHelper extends SQLiteOpenHelper {
     private static final String  DATABASE_NAME= "MyAwesomeQuiz.db";
     private static final int DATABASE_VERSION=1;
     private SQLiteDatabase db;
    public QuizDbHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        final String  SQL_CREATE_QUESTIONS_TABLE= "CREATE TABLE  " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

         db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
         fillQuestionsTable();



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);


    }
    private void fillQuestionsTable(){
        Question q1= new Question("Which is a valid keyword ?", "null","new","instanceof",2);
        addQuestion(q1);
        Question q2= new Question("Which of the following is known as a subclass ?", "Inner Class","Nested Class","Derived Class",3);
        addQuestion(q2);
        Question q3= new Question("Which is Correct ?", "Character Streams are supported in Java ","Both Byte and Character Streams are supported in Java","Byte Streams are supported in java",2);
        addQuestion(q3);
        Question q4= new Question("Which is helpful for garbage collection ?", "final","finalize","finally",2);
        addQuestion(q4);
        Question q5= new Question("Which is correct ?", "An abstract class cannot be final","An abstract class can be final","An abstract class should implement atleast one interface",1);
        addQuestion(q5);
        Question q6= new Question("Which is the member of runnable interface ?", "yield","stop","run",3);
        addQuestion(q6);
        Question q7= new Question("Which one of these passes parameter values to an applet ?", "XML","HTML","JAVASCRIPT",2);
        addQuestion(q7);
        Question q8= new Question("Which of the following is not a class access modifier ?", "public","protected","final",3);
        addQuestion(q8);
        Question q9= new Question("Which is not true about static variables and methods ?", "They are not tied to any particular instance of a class ","Static methods can directly access non-static members","No class instances are needed in order to use static members of a class",2);
        addQuestion(q9);
        Question q10= new Question("Which of the following is correct?", "A extends B is correct if A is an interface and B is a class","A extends B is correct if A and B both are interfaces ","A extends B is correct if A is class and  B is an interface",2);
        addQuestion(q10);






    }
    private void addQuestion(Question question)
    {
        ContentValues cv= new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null,cv);

    }
    public List<Question> getAllQuestions()
    {
        List<Question> questionList= new ArrayList<>();
        db= getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null );
        if(c.moveToFirst())
        {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while(c.moveToNext());
        }

       c.close();
        return questionList;

    }

}
