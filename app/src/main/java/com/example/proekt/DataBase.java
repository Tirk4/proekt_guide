package com.example.proekt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

;

public class DataBase extends SQLiteOpenHelper {
    static String name = "database.sqlite";
    static int version = 1;


    private static DataBase instance;

    public static DataBase getInstance() {
        return instance;
    }

    public static void createInstance(Context context) {
        instance = new DataBase(context);
    }

    private DataBase(Context context) {
        super(context, name, null, version);

        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists Notes (id integer primary key autoincrement," +
                "rate real, title text, theme text, description text, coordX real, coordY real, date text" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(sqLiteDatabase);
    }

    public void add(Note note) {
        getWritableDatabase().execSQL("insert into Notes values (" + note.getId() + "," + note.rate + ",'" + note.title + "','" +
                note.theme + "','" + note.text + "'," + note.coordX + "," + note.coordY + ",'" + note.date + "')");

    }


    public List<Note> getAll() {
        List<Note> notes = new ArrayList<>();
        Cursor resultSet=getReadableDatabase().rawQuery("Select * From Notes",null);

        while (resultSet.moveToNext()) {
            notes.add(
                    new Note(
                            resultSet.getInt(0),
                            resultSet.getFloat(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(7),
                            resultSet.getDouble(5),
                            resultSet.getDouble(6)
                    )
            );
        }

        resultSet.close();


         return notes.stream().sorted(
                Comparator.comparing((Note o) -> {
                    try {
                        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(o.date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })).collect(Collectors.toList()
        );
    }

    public void delete(int id) {
        getWritableDatabase().execSQL("delete from Notes where id="+id);
    }

}
