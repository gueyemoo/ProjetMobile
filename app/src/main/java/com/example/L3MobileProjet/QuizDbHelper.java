package com.example.L3MobileProjet;

import com.example.L3MobileProjet.model.QuestionQuiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.L3MobileProjet.QuizContract.*;

import java.util.ArrayList;


public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quiz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //créé la table contenant les questions et les réponses
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    // rajouter des questions à la table avec les réponses possibles

    private void fillQuestionsTable() {
        QuestionQuiz q1 = new QuestionQuiz("Donner la racine carrée de 25", "12", "2", "5", 3);
        addQuestion(q1);
        QuestionQuiz q2 = new QuestionQuiz("Quelle est la capitale du Portugal", "Lisbonne", "Barcelone", "Madrid", 1);
        addQuestion(q2);
        QuestionQuiz q3 = new QuestionQuiz("Traduire ce verbe : 'To hide '", "Grandir", "Se cacher", "Avoir une idée", 2);
        addQuestion(q3);
        QuestionQuiz q4 = new QuestionQuiz("Choisir la bonne orthographe :", "Appeler", "Apeller", "Appeller", 1);
        addQuestion(q4);
        QuestionQuiz q5 = new QuestionQuiz("Quel pays fait partie de l'UE ? ", "Hongrie", "Norvège", "Suisse", 1);
        addQuestion(q5);
        QuestionQuiz q6 = new QuestionQuiz("A quel animal l'adjectif 'hippique' se rapporte-t-il ? ", "Chien", "Cheval", "Canard", 2);
        addQuestion(q6);
        QuestionQuiz q7 = new QuestionQuiz(" Combien de voleurs accompagnaient Ali Baba ? ", "450", "2", "40", 3);
        addQuestion(q7);
        QuestionQuiz q8 = new QuestionQuiz(" La somme des angles d'un triangle est égale à ? (en degrés) ", "180", "124.5", "78", 1);
        addQuestion(q8);
        QuestionQuiz q9 = new QuestionQuiz(" Trouvez l’intrus qui s’est glissé dans les pluriels des mots en ou ? ", "Chou", "Caillou", "Bisou", 3);
        addQuestion(q9);
        QuestionQuiz q10 = new QuestionQuiz(" Comment conjugue-t-on au futur le verbe connaître à la troisième personne du singulier ? ", "Connaîtrait", "Connaîtra", "Aurez connu", 2);
        addQuestion(q10);

    }

    private void addQuestion(QuestionQuiz questionQuiz) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, questionQuiz.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, questionQuiz.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, questionQuiz.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, questionQuiz.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, questionQuiz.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<QuestionQuiz> getAllQuestions() {
        ArrayList<QuestionQuiz> questionQuizList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                QuestionQuiz questionQuiz = new QuestionQuiz();
                questionQuiz.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                questionQuiz.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                questionQuiz.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                questionQuiz.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                questionQuiz.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionQuizList.add(questionQuiz);
            } while (c.moveToNext());
        }

        c.close();
        return questionQuizList;
    }
}
