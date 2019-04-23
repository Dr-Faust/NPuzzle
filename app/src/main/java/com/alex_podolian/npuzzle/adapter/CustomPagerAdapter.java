//package com.alex_podolian.npuzzle.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//
//import androidx.viewpager.widget.PagerAdapter;
//
//import java.util.ArrayList;
//
///**
// * Created by gilad on 12/19/15.
// */
//public class CustomPagerAdapter extends PagerAdapter {
//
//    private Context mContext;
//    private ArrayList<Board> boards;
//
//    public CustomPagerAdapter(Context context, ArrayList<Board> bs) {
//        mContext = context;
//        boards = bs;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup collection, int position) {
//        LinearLayout layout = new LinearLayout(mContext);
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        layout.setLayoutParams(layoutParams);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setBackgroundColor(Color.parseColor("#2196F3"));
//
//        TextView titleText = new TextView(mContext);
//        titleText.setText(getPageTitle(position));
//        titleText.setTextSize(32);
//        titleText.setTextColor(Color.WHITE);
//        titleText.setPadding(0, 20, 0, 0);
//        titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        LayoutParams titleTextLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        titleText.setLayoutParams(titleTextLayoutParams);
//
//        TableLayout tableLayout = new TableLayout(mContext);
//        LayoutParams tableParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        tableLayout.setLayoutParams(tableParams);
//        tableLayout.setGravity(Gravity.CENTER);
//        tableLayout.setPadding(50, 50, 50, 50);
//        tableLayout.setOrientation(LinearLayout.VERTICAL);
//        for (int i = 0; i < boards.get(position).size(); i++ ){
//            TableRow row = new TableRow(mContext);
//            row.setGravity(Gravity.CENTER);
//            for (int j = 0; j < boards.get(position).size(); j++) {
//                TextView text = new TextView(mContext);
//                text.setTextSize(40);
//                text.setTextColor(Color.WHITE);
//                text.setPadding(20, 20, 20, 20);
//                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                text.setGravity(Gravity.CENTER);
//                int num = boards.get(position).getValue(i, j);
//                if (num == 0) {
//                    text.setText(" ");
//                } else {
//                    text.setText("" + boards.get(position).getValue(i, j));
//                }
//                row.addView(text);
//            }
//            tableLayout.addView(row);
//        }
//
//        layout.addView(titleText);
//        layout.addView(tableLayout);
//
//        if (position == boards.size()-1) {
//            Button againBtn = new Button(mContext);
//            againBtn.setText("Solve New Puzzle");
//            againBtn.setTextSize(24);
//            againBtn.setBackgroundColor(Color.parseColor("#B6B6B6"));
//            againBtn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            againBtn.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
//            LinearLayout.LayoutParams againBtnLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            againBtnLayoutParams.setMargins(200, 100, 200, 0);
//            againBtn.setLayoutParams(againBtnLayoutParams);
//
//            againBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(mContext, MenuActivity.class);
//                    mContext.startActivity(i);
//                }
//            });
//
//            layout.addView(againBtn);
//        }
//
//
//
//        collection.addView(layout);
//        return layout;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup collection, int position, Object view) {
//        collection.removeView((View) view);
//    }
//
//    @Override
//    public int getCount() {
//        return boards.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return "Step " + (position + 1);
//    }
//
//}
