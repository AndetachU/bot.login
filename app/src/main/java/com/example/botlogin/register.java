package com.example.botlogin;

import android.content.Context;
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

public class register extends AppCompatActivity {
    //Elementos del activity
    private EditText etDocumento;
    private  EditText etNombres;
    private  EditText etApellidos;
    private EditText etEmail;
    private EditText etPassword;
    private Button button_register;
    private Context context;
    private int documento;
    String email;
    String nombres;
    String apellidos;
    String Contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initObject();
        context = this;
        button_register.setOnClickListener(this::crearUsuario);
    }
    private void crearUsuario(View view) {
        getData();
        String hashedPassword = hashPassword(Contra);
        User user = new User(documento,nombres,apellidos,email,hashedPassword);
        UserDao dao = new UserDao(context,view);
        dao.insertUser(user);
        ventanaLogin();
    }
// metodo para abrir activity inicio
    private void ventanaLogin() {
        Intent intent = new Intent(this, inicio.class);
        startActivity(intent);
    }

    private void getData() {
        documento = Integer.parseInt(etDocumento.getText().toString());
        email = etEmail.getText().toString();
        nombres = etNombres.getText().toString();
        apellidos = etApellidos.getText().toString();
        Contra = etPassword.getText().toString();
        //validación datos expresiones regulares
        String documentoPattern = "\\d+";
        if(!etDocumento.getText().toString().matches(documentoPattern)) {
            Toast.makeText(this, "El documento debe ser un número entero", Toast.LENGTH_SHORT).show();
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$";
        if (email.matches(emailPattern)) {

            Toast.makeText(this, "El correo no es valido.", Toast.LENGTH_SHORT).show();
        }
        String nombresApellidosPattern = "^[a-zA-Z\\s]+$";
        if (!nombres.matches(nombresApellidosPattern)) {

            Toast.makeText(this, "Los nombres solo pueden contener letras y espacios.", Toast.LENGTH_SHORT).show();
        }
        if (!apellidos.matches(nombresApellidosPattern)) {

            Toast.makeText(this, "Los apellidos solo pueden contener letras y espacios.", Toast.LENGTH_SHORT).show();
        }
        String contraPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        if (!Contra.matches(contraPattern)) {
            Toast.makeText(this, "La contraseña debe tener al menos una letra mayúscula, una letra minúscula, un número y 8 caracteres.", Toast.LENGTH_SHORT).show();
        }
    }
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
    private void initObject() {
        etDocumento = findViewById(R.id.etDocumento);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        button_register = findViewById(R.id.button_register);
    }
}