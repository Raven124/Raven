package ibanzay.therapist;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//Класс для работы с базой данных
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns{

    public static String DATABASE_PATH = "data/data/ibanzay.therapist/databases/"; //Путь к файлу БД
    public static final String DATABASE_NAME = "diagnostic2.db"; //Имя файла БД
    public static final int DATABASE_VERSION = 1; //Версия БД

    public static final String DB_MAIN_TABLE = "general_symptoms"; //Имя основной таблицы в БД
    public static final String SCHEMA_ID = "schemaID";
    public static final String SCHEMA_DESCRIPTION = "description";
    public static final String START_QUESTION = "startQuestion";

    public static final String DATABASE_TABLE = "symptoms"; //Имя таблицы с симптомами в БД
    public static final String SYMPTOM_ID = "symptomID";
    public static final String SYMPTOM_DESCRIPTION = "symptomDescription";
    public static final String SYMPTOM_YES = "symptomYes";
    public static final String SYMPTOM_NO = "symptomNo";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    //Формируем строку запроса для создания таблицы в базе данных
    public static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + SYMPTOM_ID
            + " integer, " + SYMPTOM_DESCRIPTION + " text not null, "
            + SYMPTOM_YES + " integer, " + SYMPTOM_NO + " integer);";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version){
        super(context, name, factory, version);
        this.myContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_MAIN_TABLE);
        onCreate(db);
    }

    //Создаем базу
    public void createDataBase()throws IOException{
        boolean dbExist = checkDataBase();
        if(dbExist){
            //Если база существует то ничего не делать
        }else {
            this.getReadableDatabase();
            try{
                copyDataBase();
            }catch (IOException e){
                throw new Error("Ошибка при копировании базы");
            }
        }
    }

    //Проверяем существование БД с указанным именем по указанному пути
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    //Копируем базу из файла установки
    private void copyDataBase() throws IOException{
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME); //Входящая БД из файла установки
        String outFileName = DATABASE_PATH + DATABASE_NAME; //Исходящая БД с именем и путем установки
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws IOException{
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close(){
        if (myDataBase != null){
            myDataBase.close();
        }
        super.close();
    }
}