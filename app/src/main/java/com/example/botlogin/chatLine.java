package com.example.botlogin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class chatLine extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private EditText etMessage;
    private ImageButton ibCamera;
    private Button btnSendMessage;
    private RecyclerView rvMessage;
    private ArrayList<String> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_line);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initObjects();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true); // Invierte el orden de los elementos
        rvMessage.setLayoutManager(layoutManager);
        btnSendMessage.setOnClickListener(this::enviarMensaje);
        ibCamera.setOnClickListener(this::tomarFoto);
        // Crear y configurar el adaptador
        MessageAdapter adapter = new MessageAdapter(messages);
        rvMessage.setAdapter(adapter);
    }

    private void enviarMensaje(View view) {
        String message = etMessage.getText().toString();
        if (!message.isEmpty()) {
            // Deshabilitar el botón de enviar temporalmente para evitar múltiples envíos
            btnSendMessage.setEnabled(false);

            // Agregar el nuevo mensaje al principio de la lista para que aparezca al final del RecyclerView
            messages.add(0, message);

            // Notificar al adaptador sobre el cambio de datos
            rvMessage.getAdapter().notifyItemInserted(0);

            // Desplazar el RecyclerView hacia abajo para mostrar el nuevo mensaje
            rvMessage.scrollToPosition(0);

            // Limpiar el EditText para el próximo mensaje
            etMessage.setText("");

            // Habilitar nuevamente el botón de enviar
            btnSendMessage.setEnabled(true);
        }
    }

    private void tomarFoto(View view) {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(chatLine.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                cameraLauncher.launch(takePictureIntent);
            }
        }
    }
    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Aquí puedes guardar la imagen y agregarla al chat
                    agregarFotoAlChat(imageBitmap);
                }
            }
    );

    private void agregarFotoAlChat(Bitmap imageBitmap) {
        // Guardar la imagen en el almacenamiento local
        String imagePath = guardarImagenEnAlmacenamientoLocal(imageBitmap);

        // Agregar la ruta de la imagen al ArrayList de mensajes
        messages.add(0,imagePath);

        // Notificar al adaptador sobre el cambio de datos
        rvMessage.getAdapter().notifyItemInserted(0);

        // Desplazar el RecyclerView hacia abajo para mostrar la imagen
        rvMessage.scrollToPosition(0);
    }

    private String guardarImagenEnAlmacenamientoLocal(Bitmap imageBitmap) {
        // Generar un nombre único para la imagen
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Crear el directorio donde se guardará la imagen
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Crear el archivo de imagen
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardar el bitmap en el archivo
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Devolver la ruta del archivo de imagen
        return imageFile.getAbsolutePath();
    }
    private void initObjects() {
        etMessage = findViewById(R.id.etMessage);
        ibCamera = findViewById(R.id.ibCamera);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        rvMessage = findViewById(R.id.rvMessage);
        messages = new ArrayList<String>();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(this)
                        .setTitle("Alerta de Permiso")
                        .setMessage("Denegaste el Permiso requerido")
                        .setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
            } else {
                Toast.makeText(this, "Permiso otorgado vuelva a ingresar a la camara.", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(this, "Este permiso es requerido para la aplicación.", Toast.LENGTH_SHORT).show();

        }

    }
}