package com.example.botlogin.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.example.botlogin.entity.User;
import com.google.android.material.snackbar.Snackbar;

public class UserDao {
    private ManagerDataBase managerDataBase;
    Context context;
    View view;
    private User user;
    private static boolean isLoggedIn = false;

    public UserDao(Context context, View view) {
        this.context = context;
        this.view = view;
        managerDataBase = new ManagerDataBase(this.context);

    }
    //Metodo creación de usuarios
    public void insertUser(User user){
        try {
            SQLiteDatabase db = managerDataBase.getWritableDatabase();
            if(db != null){
                ContentValues values = new ContentValues();
                values.put("use_document", user.getDocument());
                values.put("use_email",user.getEmail());
                values.put("use_names",user.getNames());
                values.put("use_lastNames",user.getLastNames());
                values.put("use_password",user.getPassword());
                values.put("use_status","1");
                long cod = db.insert("users",null,values);
                Snackbar.make(this.view,"Se ha registrado el usuario:" + cod,Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(this.view,"No se ha registrado el usuario",Snackbar.LENGTH_LONG).show();
            }

        }catch (SQLException e){
            Log.i("BD",""+e);
        }

    }
    public User getUserByEmailAndPassword(String email, String hashedPassword) {

        String[] selectionArgs = { email, hashedPassword };

        SQLiteDatabase db = managerDataBase.getReadableDatabase();
        String query = "select * from users where use_email = ? AND use_password = ?;";
        Cursor cursor = db.rawQuery(query,selectionArgs);

        if (cursor.moveToFirst()) {
            int documentColumnIndex = cursor.getColumnIndex("use_document");
            int nombresColumnIndex = cursor.getColumnIndex("use_names");
            int apellidosColumnIndex = cursor.getColumnIndex("use_lastNames");
            int emailColumnIndex = cursor.getColumnIndex("use_email");
            int passwordColumnIndex = cursor.getColumnIndex("use_password");
            int statusColumnIndex = cursor.getColumnIndex("use_status");
            int document = cursor.getInt(documentColumnIndex);
            String nombres = cursor.getString(nombresColumnIndex);
            String apellidos = cursor.getString(apellidosColumnIndex);
            email = cursor.getString(emailColumnIndex);
            String password = cursor.getString(passwordColumnIndex);
            String status = cursor.getString(statusColumnIndex);
            if(!status.equals("1")) return null;
            else {
                return new User(document, nombres, apellidos, email, password);
            }
        } else {
            return null;
        }
    }
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
