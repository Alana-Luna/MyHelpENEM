package br.edu.ifpe.tads.pdm.myhelpenem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class QuestaoDataBaseAdapter {

    static final String DATABASE_NAME = "questoes.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;

    static final String DATABASE_CREATE = "create table "+"QUESTOES"+
            "( " +"ID"+" integer primary key autoincrement,"+ "PERGUNTA  text,RESPOSTA text,ALTERNATIVA1 text, ALTERNATIVA2 text, ALTERNATIVA3 text, ALTERNATIVA4 text); ";

    public  SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;

    public  QuestaoDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public  QuestaoDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String pergunta,String resposta, String alter1, String alter2, String alter3, String alter4)
    {
        ContentValues newValues = new ContentValues();

        newValues.put("PERGUNTA", pergunta);
        newValues.put("RESPOSTA", resposta);
        newValues.put("ALTERNATIVA1", alter1);
        newValues.put("ALTERNATIVA2", alter2);
        newValues.put("ALTERNATIVA3", alter3);
        newValues.put("ALTERNATIVA4", alter4);

        // Insert the row into your table
        db.insert("QUESTOES", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }


}
