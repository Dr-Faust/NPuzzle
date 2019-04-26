package com.alex_podolian.npuzzle.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.alex_podolian.npuzzle.R;
import com.alex_podolian.npuzzle.view.MainActivity;
import com.alex_podolian.npuzzle.view.custom_views.PuzzleBoardView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class StepsPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ArrayList<Integer>> listMaps;
    private int puzzleSize;
    private int textSize;

    public StepsPagerAdapter(Context context, ArrayList<ArrayList<Integer>> listMaps,
                             int puzzleSize, int textSize) {
        this.context = context;
        this.listMaps = listMaps;
        this.puzzleSize = puzzleSize;
        this.textSize = textSize;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);

        TextView titleText = new TextView(context);
        titleText.setText(getPageTitle(position));
        titleText.setTextSize(32);
        titleText.setTextColor(context.getResources().getColor(R.color.primaryColor));
        titleText.setPadding(20, 23, 20, 27);
        titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        container.addView(titleText);
        PuzzleBoardView puzzleBoardView = new PuzzleBoardView(context, puzzleSize, textSize,
            listMaps.get(position), null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        layoutParams.setMargins(37, 0, 37, 0);
        puzzleBoardView.setLayoutParams(layoutParams);
        container.addView(puzzleBoardView);

        if (position == listMaps.size() - 1) {
            MaterialButton againBtn = new MaterialButton(context);
            againBtn.setText("New game");
            LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(300,
                LinearLayout.LayoutParams.WRAP_CONTENT);
            btnLayoutParams.setMargins(0, 0, 0, 30);
            btnLayoutParams.gravity = Gravity.CENTER;
            againBtn.setLayoutParams(btnLayoutParams);

            againBtn.setOnClickListener(view -> {
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
            });
            container.addView(againBtn);
        }
        collection.addView(container);

        return container;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return listMaps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Step " + (position + 1);
    }
}
