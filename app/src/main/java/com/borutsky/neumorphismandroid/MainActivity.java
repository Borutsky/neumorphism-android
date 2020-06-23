package com.borutsky.neumorphismandroid;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.borutsky.neumorphism.NeumorphicFrameLayout;

public class MainActivity extends AppCompatActivity {

    NeumorphicFrameLayout.State[] states = new NeumorphicFrameLayout.State[]{
            NeumorphicFrameLayout.State.FLAT,
            NeumorphicFrameLayout.State.CONCAVE,
            NeumorphicFrameLayout.State.CONVEX,
            NeumorphicFrameLayout.State.PRESSED
    };

    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NeumorphicFrameLayout block = findViewById(R.id.firstBlock);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = current < 3 ? current + 1 : 0;
                ((NeumorphicFrameLayout) v).setState(states[current]);
            }
        });
    }

}