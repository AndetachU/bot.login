package com.example.botlogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.botlogin.entity.User;
import com.example.botlogin.model.UserDao;

import java.security.MessageDigest;

public class inicio extends AppCompatActivity {

    //Elementos del activity
    private EditText etEmail;
    private  EditText etPassword;
    private Button button_login;
    private  Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initObject();
        button_register.setOnClickListener(this::registrarUsuario);
        button_login.setOnClickListener(this::loginUser);
    }

    //metodo para iniciar sesión
    private void loginUser(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // se hashea la contraseña antes de hacer la consulta.
        String hashedPassword = hashPassword(password);
        UserDao dao = new UserDao(this, view);
        User user = dao.getUserByEmailAndPassword(email, hashedPassword);

        // si encuentra el usuario permite el acceso
        if (user != null) {
            // indica que el usuario esta logeado
            UserDao.setLoggedIn(true);
            // carga activity home
            Intent intent = new Intent(this, home.class);
            startActivity(intent);
        } else {
            // Display an error message if no match is found
            Toast.makeText(this, "Email Invalido o contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }

    }
    //metodo para hashear la contraseña
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            Log.i("Pass",""+ ex);
            throw new RuntimeException(ex);
        }
    }

    //inicia registrar usuario
    private void registrarUsuario(View view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    //inicializa objetos
    private void initObject() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
    }

}