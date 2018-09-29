package ibanzay.therapist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

public class QuestionBox extends AppCompatActivity implements View.OnClickListener {
    private static final String DATABASE_NAME = "diagnostic.db";
    private static final int DB_VERSION = 1;

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;
    int sID, sY, sN, qID;
    String sDescription, qDescription;
    LinearLayout questions, startQ, qHome;
    ScrollView scrollContent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnostic);

        mDatabaseHelper = new DatabaseHelper(this, DATABASE_NAME, null, DB_VERSION);
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Intent intent = getIntent();

        qID = intent.getExtras().getInt("qID"); //Получаем номер вопроса из главного экрана
        qDescription = intent.getExtras().getString("qDescription"); //Получаем заголовок вопроса

        getAnswer(qID); //получаем строку из БД соответствующую номеру вопроса

        questions = (LinearLayout) findViewById(R.id.questions); //Основной вертикальный LinearLayout

        scrollContent = new ScrollView(this);
        scrollContent.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        scrollContent.setVerticalScrollBarEnabled(false);

        startQ = new LinearLayout(this);
        startQ.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        startQ.setOrientation(LinearLayout.VERTICAL);

        generateQuestion(sID, sDescription, sY, sN, startQ);

        scrollContent.addView(startQ);
        createGeneralQuestion(qDescription, questions);
        questions.addView(scrollContent);
        setContentView(questions);
    }

    @Override
    public void onClick(View v) {
        v.setBackgroundResource(R.drawable.rounded_push_buttons);

        View parent = (View)v.getParent().getParent();
        qHome = (LinearLayout)findViewById(parent.getId()); //Находим контейнер-родитель нажатой кнопки по ID

        TextView noV;
        getAnswer(qHome.getId() - 20000); //Получаем данные о компонентах содержащихся в контейнере-родителе
        if(v.getId()-(qHome.getId() - 20000)*100 == sY){ //Определяем по ID нажата ли кнопка "Да"
            if(sN!=0) { //При этом если у кнопки "Нет" имеется направление на следующий вопрос
                noV = (TextView) findViewById(sN + (qHome.getId()-20000)*100);
                noV.setBackgroundResource(R.drawable.rounded_buttons);
            }
        }else{
            noV = (TextView)findViewById(sY + (qHome.getId()-20000)*100);
            noV.setBackgroundResource(R.drawable.rounded_buttons);
        }

        try {
            qHome.removeView((LinearLayout)findViewById(sY+20000));
            qHome.removeView((LinearLayout)findViewById(sN+20000));
        } catch (AssertionError e){
        }

        getAnswer(v.getId() - (qHome.getId()-20000)*100); //Запрашиваем строку из БД соответствующую идентификатор нажатай кнопки

        generateQuestion(sID, sDescription, sY, sN, qHome); //Показываем вопрос на экране

        final ViewTreeObserver observer = startQ.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollContent.smoothScrollTo(0, startQ.getHeight());
                observer.removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void generateQuestion(int id, String description, int yes, int no, LinearLayout qHome){

        LinearLayout layerLayout = new LinearLayout(this);

        layerLayout.setId(id + 20000); //ID родительского лэйаута
        layerLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        layerLayout.setOrientation(LinearLayout.VERTICAL);

        TextView questionText = new TextView(this);
        LinearLayout.LayoutParams qtParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        qtParams.setMargins(0, 5, 0, 5);
        questionText.setLayoutParams(qtParams);
        questionText.setText(description);
        questionText.setGravity(Gravity.CENTER);
        questionText.setPadding(0, 5, 0, 5);
        questionText.setTextSize(15);
        questionText.setTextColor(Color.parseColor("#000000"));
        questionText.setTypeface(Typeface.DEFAULT_BOLD);

        layerLayout.addView(questionText);

        if(yes!=0){

            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setPadding(10,0,10,0);

            TextView buttonYes = new TextView(this);
            TextView buttonNo = new TextView(this);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            buttonParams.setMargins(65,5,65,25);
            buttonParams.weight = 1;

            buttonYes.setLayoutParams(buttonParams);
            buttonYes.setText("Да");
            buttonYes.setTextColor(Color.parseColor("#000000"));
            buttonYes.setPadding(0, 8, 0, 8);
            buttonYes.setWidth(300);
            buttonYes.setGravity(Gravity.CENTER);
            buttonYes.setBackgroundResource(R.drawable.rounded_buttons);
            buttonYes.setId(yes + id*100); //ID кнопки да
            buttonYes.setClickable(true);
            buttonYes.setOnClickListener(this);
            buttonLayout.addView(buttonYes);

            if(no!=0) {
                buttonNo.setLayoutParams(buttonParams);
                buttonNo.setText("Нет");
                buttonNo.setTextColor(Color.parseColor("#000000"));
                buttonNo.setPadding(0, 8, 0, 8);
                buttonNo.setWidth(300);
                buttonNo.setGravity(Gravity.CENTER);
                buttonNo.setBackgroundResource(R.drawable.rounded_buttons);
                buttonNo.setId(no + id*100); //ID кнопки нет
                buttonNo.setClickable(true);
                buttonNo.setOnClickListener(this);
                buttonLayout.addView(buttonNo);
            } else {
                questionText.setBackgroundResource(R.drawable.rounded_diagnos);
                questionText.setTextColor(Color.parseColor("#cc2323"));
                questionText.setTextSize(20);
                questionText.setPadding(10, 10, 10, 10);
                buttonYes.setText("Продолжить диагностирование?");
                buttonLayout.setPadding(10, 10, 10, 0);
            }
            layerLayout.addView(buttonLayout);
        } else {
            questionText.setBackgroundResource(R.drawable.rounded_diagnos);
            questionText.setTextColor(Color.parseColor("#cc2323"));
            questionText.setTextSize(20);
            questionText.setPadding(10, 10, 10, 10);
        }
        qHome.addView(layerLayout);
    }

    //Метод для обращения к БД
    private void getAnswer(int questionNumber){
        //Создаем переменную для обращения к базе
        Cursor cursor = mSQLiteDatabase.query("symptoms", new String[]{
                        DatabaseHelper.SYMPTOM_ID,
                        DatabaseHelper.SYMPTOM_DESCRIPTION,
                        DatabaseHelper.SYMPTOM_YES,
                        DatabaseHelper.SYMPTOM_NO},
                null, null,
                null, null, null, null);
        //Определяем начальную позицию для запроса
        cursor.moveToPosition(questionNumber - 1);
        //Считываем информацию с текущей позиции
        this.sID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SYMPTOM_ID));
        this.sDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SYMPTOM_DESCRIPTION));
        this.sY = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SYMPTOM_YES));
        this.sN = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SYMPTOM_NO));
    }

    //Заголовок вопроса
    private void createGeneralQuestion(String description, LinearLayout gSHome){
        TextView questionText = new TextView(this);
        LinearLayout.LayoutParams qtParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        qtParams.setMargins(0, 5, 0, 5);
        questionText.setLayoutParams(qtParams);
        questionText.setText(description);
        questionText.setGravity(Gravity.CENTER);
        questionText.setPadding(15, 15, 15, 15);
        questionText.setTextSize(15);
        questionText.setTextColor(Color.parseColor("#000000"));
        questionText.setTypeface(Typeface.DEFAULT_BOLD);
        questionText.setBackgroundResource(R.drawable.general_questions_button);
        gSHome.addView(questionText);
    }
}