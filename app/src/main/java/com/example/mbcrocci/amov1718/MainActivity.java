package com.example.mbcrocci.amov1718;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playAgainstAI(View view) {
        Intent intent = new Intent(this, PlayAgainstAIActivity.class);
        startActivity(intent);
    }

    public void playAgainstPlayerLocal(View view) {
        Intent intent = new Intent(this, PlayAgainstPlayerLocalActivity.class);
        startActivity(intent);
    }

    public void playAgainstPlayerOnline(View view) {
        //Intent intent = new Intent(this, PlayGameOnline.class);
        //startActivity(intent);
    }
}
