package com.example.botlogin;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private ArrayList<String> messages;

    public MessageAdapter(ArrayList<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String message = messages.get(position);
        if (isImage(message)) {
            holder.bindImage(message);
        } else {
            holder.bindText(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;
        private ImageView ivImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bindText(String message) {
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);
            ivImage.setVisibility(View.GONE);
        }

        public void bindImage(String imagePath) {
            // Cargar la imagen desde la ruta y mostrarla en ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivImage.setImageBitmap(bitmap);
            ivImage.setVisibility(View.VISIBLE);
            tvMessage.setVisibility(View.GONE);
        }
    }
    private boolean isImage(String message) {
        // Verificar si el mensaje es una ruta de imagen
        return message != null && message.toLowerCase().endsWith(".jpg"); // Por ejemplo, aquí asumimos que las imágenes tienen extensión ".jpg"
    }
}