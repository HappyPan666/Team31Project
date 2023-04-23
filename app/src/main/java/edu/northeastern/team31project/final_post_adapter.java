package edu.northeastern.team31project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class final_post_adapter extends ArrayAdapter<final_single_post> {
    Map<String, final_user> final_userMap;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Map<String, Bitmap> imageMap;
    String UID;

    public final_post_adapter(@NonNull Context context, List<final_single_post> final_single_posts, Map<String, final_user> final_userMap, String UID) {
        super(context, 0, final_single_posts);
        this.final_userMap = final_userMap;
        this.UID = UID;
        this.imageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final_single_post final_single_post = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_final_item_post, parent, false);
        }

        TextView name = convertView.findViewById(R.id.syx_post_name);
        TextView userName = convertView.findViewById(R.id.syx_post_username);
        TextView time = convertView.findViewById(R.id.syx_post_time);
        ImageView image = convertView.findViewById(R.id.syx_post_content_Info);

        final_user final_user = final_userMap.get(final_single_post.getPosterUID());
        name.setText(final_user.getFirstName() + " " + final_user.getLastName());
        userName.setText(final_user.getUserName());
        time.setText(simpleDateFormat.format(final_single_post.getPostdate()));
        if (!final_single_post.getPostImage().isEmpty()) {
            if (imageMap.get(final_single_post.getPostImage()) == null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child(final_single_post.getPostImage())
                        .getBytes(5 * 1024 * 1024).addOnSuccessListener(v -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(v, 0, v.length);
                            image.setImageBitmap(bitmap);
                            imageMap.put(final_single_post.getPostImage(), bitmap);
                        });
            } else {
                image.setImageBitmap(imageMap.get(final_single_post.getPostImage()));
            }
        }else {
            image.setImageBitmap(null);
        }

        return convertView;
    }
}
