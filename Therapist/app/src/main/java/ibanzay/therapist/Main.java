package ibanzay.therapist;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;

public class Main extends ActionBarActivity implements View.OnClickListener {
    private static final String DATABASE_NAME = "diagnostic2.db";
    private static final int DB_VERSION = 1;

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;
    int startQuestion, schemaID;
    String description;
    LinearLayout mainLayout, generalSymptoms;
    ScrollView scrollContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mDatabaseHelper = new DatabaseHelper(this);
        try {
            mDatabaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Невозможно создать базу");
        }

        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        scrollContent = new ScrollView(this);
        scrollContent.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        scrollContent.setVerticalScrollBarEnabled(true);

        generalSymptoms = new LinearLayout(this);
        generalSymptoms.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        generalSymptoms.setOrientation(LinearLayout.VERTICAL);
        generalSymptoms.setPadding(16, 16, 16, 16);

        getGeneralSymptomsList();

        scrollContent.addView(generalSymptoms);
        mainLayout.addView(scrollContent);
        setContentView(mainLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.about_therapist:
                createAboutBox();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAboutBox() {
        Dialog aboutBox = new Dialog(this);
        aboutBox.setCanceledOnTouchOutside(true);
        aboutBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutBox.getWindow().setBackgroundDrawableResource(R.drawable.about_box);
        aboutBox.setContentView(R.layout.about);
        aboutBox.show();
    }

    //Устанавливаем выбранный симптом и запускаем тестирование
    public void onClick(View v){
        Intent intent = new Intent(this, QuestionBox.class);
        if(v instanceof TextView) {
            String qText = ((TextView)v).getText().toString();
            intent.putExtra("qDescription", qText);
        }
        intent.putExtra("qID", v.getId() - 50000);
        startActivity(intent);
    }

    //Возвращаем список основных симптомов из БД с первого по последний
    private void getGeneralSymptomsList(){
        mDatabaseHelper = new DatabaseHelper(this, DATABASE_NAME, null, DB_VERSION);
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.query(DatabaseHelper.DB_MAIN_TABLE, new String[]{
                        DatabaseHelper.SCHEMA_ID,
                        DatabaseHelper.SCHEMA_DESCRIPTION,
                        DatabaseHelper.START_QUESTION},
                        null, null,
                        null, null, null, null);
        cursor.moveToFirst();
        do {
            this.schemaID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SCHEMA_ID));
            this.description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SCHEMA_DESCRIPTION));
            this.startQuestion = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.START_QUESTION));
            createGeneralQuestion(this.description, this.startQuestion, this.generalSymptoms);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
    }

    //Создаем кликабельный блок с основным симптомом и добавляем его в конец лэйаута
    private void createGeneralQuestion(String description, int id, LinearLayout gSHome){
        TextView questionText = new TextView(this);
        LinearLayout.LayoutParams qtParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        qtParams.setMargins(0, 5, 0, 5);
        questionText.setLayoutParams(qtParams);
        questionText.setId(id + 50000);
        questionText.setText(description);
        questionText.setGravity(Gravity.LEFT);
        questionText.setPadding(15, 15, 15, 15);
        questionText.setTextSize(15);
        questionText.setTextColor(Color.parseColor("#000000"));
        questionText.setTypeface(Typeface.DEFAULT_BOLD);
        questionText.setClickable(true);
        questionText.setOnClickListener(this);
        questionText.setBackgroundResource(R.drawable.general_questions_button);
        gSHome.addView(questionText);
    }
}