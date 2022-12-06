package com.example.doodler;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DoodleView doodleScreen;
    private Button color, size, opacity, clear;
    private SeekBar colorBar, sizeBar, opacityBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleScreen = (DoodleView) findViewById(R.id.doodleArea);
        color = (Button) findViewById(R.id.colorBtn);
        colorBar = (SeekBar) findViewById(R.id.colorBar);
        colorBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        colorBar.getThumb().setTint(Color.BLACK);
        size = (Button) findViewById(R.id.sizeBtn);
        sizeBar = (SeekBar) findViewById(R.id.sizeBar);
        opacity = (Button) findViewById(R.id.opacityBtn);
        opacityBar = (SeekBar) findViewById(R.id.opacityBar);
        clear = (Button) findViewById(R.id.clearBtn);


        // adding a OnChangeListener which will
        // change the stroke attributes
        // as soon as the user slides the slider
        colorBar.setMax(256*7-120);
        colorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if(progress < 256){
                        b = progress;
                    } else if(progress < 256*2) {
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*3) {
                        g = 255;
                        b = progress%256;
                    } else if(progress < 256*4) {
                        r = progress%256;
                        g = 256 - progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*5) {
                        r = 255;
                        g = 0;
                        b = progress%256;
                    } else if(progress < 256*6) {
                        r = 255;
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*7) {
                        r = 255;
                        g = 255;
                        b = progress%256;
                    }

                    doodleScreen.setColor(Color.argb(255, r, g, b));
                    colorBar.getProgressDrawable().setColorFilter(Color.argb(255, r, g, b), PorterDuff.Mode.SRC_IN);
                    colorBar.getThumb().setTint(Color.argb(255, r, g, b));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) { }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) { }
        });

        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar arg0, int value, boolean arg2) { doodleScreen.setSize(value); }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) { }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) { }
        });

        opacityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar arg0, int value, boolean arg2) { doodleScreen.setOpacity(value); }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) { }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) { }
        });

        // pass the height and width of the custom view
        // to the init method of the DrawView object
        ViewTreeObserver vto = doodleScreen.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                doodleScreen.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = doodleScreen.getMeasuredWidth();
                int height = doodleScreen.getMeasuredHeight();
                doodleScreen.init(height, width);
            }
        });
    }

    public void clear(View view){
        doodleScreen.clear();
    }

    public void undo(View view){doodleScreen.undo();}

    public void redo(View view){doodleScreen.redo();}

    //toggles color slider
    public void changeColor(View view){
        if (colorBar.getVisibility() == View.VISIBLE)
            colorBar.setVisibility(View.GONE);
        else
            colorBar.setVisibility(View.VISIBLE);
            sizeBar.setVisibility(View.GONE);
            opacityBar.setVisibility(View.GONE);
    }

    //toggles size slider
    public void changeSize(View view){
        if (sizeBar.getVisibility() == View.VISIBLE)
            sizeBar.setVisibility(View.GONE);
        else
            sizeBar.setVisibility(View.VISIBLE);
            colorBar.setVisibility(View.GONE);
            opacityBar.setVisibility(View.GONE);
    }

    //toggles opacity slider
    public void changeOpacity(View view){
        if (opacityBar.getVisibility() == View.VISIBLE)
            opacityBar.setVisibility(View.GONE);
        else
            opacityBar.setVisibility(View.VISIBLE);
            colorBar.setVisibility(View.GONE);
            sizeBar.setVisibility(View.GONE);
    }
}