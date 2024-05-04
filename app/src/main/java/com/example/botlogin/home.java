package com.example.botlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.botlogin.model.UserDao;

public class home extends AppCompatActivity {
    private Button btnCerrarSesion;
    private Button btnChatLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (!UserDao.isLoggedIn()) {
            // El usuario no ha iniciado sesión, redirige al usuario a la pantalla de inicio de sesión
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        initObject();
        btnCerrarSesion.setOnClickListener(this::cerrarSesion);
        btnChatLine.setOnClickListener(this::chatLine);
    }

    private void chatLine(View view) {
        // Redirige al usuario a la pantalla de chat
        Intent intent = new Intent(this, chatLine.class);
        startActivity(intent);
    }

    private void cerrarSesion(View view) {
        UserDao.setLoggedIn(false);
        // Redirige al usuario a la pantalla de inicio
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initObject() {
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnChatLine = findViewById(R.id.btnChatLine);
    }
}