package com.example.firebaseimagesliderappjava;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageSlider = findViewById(R.id.image_slider);

        final List<SlideModel> slideModelList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("teachers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            slideModelList.add(new SlideModel(dataSnapshot.child("turl").getValue().toString(),dataSnapshot.child("name").getValue().toString(), ScaleTypes.CENTER_CROP));
                        }
                        imageSlider.setImageList(slideModelList,ScaleTypes.CENTER_CROP);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Toast.makeText(MainActivity.this, "Image Click: "+slideModelList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doubleClick(int position) {
                Toast.makeText(MainActivity.this, "Image Long Click: "+slideModelList.get(position).getImageUrl(), Toast.LENGTH_SHORT).show();
            }
        });

        imageSlider.setItemChangeListener(new ItemChangeListener() {
            @Override
            public void onItemChanged(int i) {
//                Toast.makeText(MainActivity.this, "Image Change: "+slideModelList.get(i).getImageUrl(), Toast.LENGTH_SHORT).show();
            }
        });


        imageSlider.setSlideAnimation(AnimationTypes.CUBE_OUT);

    }
}