package com.shizhefei.guide.demo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.shizhefei.guide.GuideHelper;
import com.shizhefei.guide.GuideHelper.TipData;
import com.shizhefei.util.DisplayUtils;


public class MainActivity extends AppCompatActivity {


    private View iconView;
    private View citysView;
    private View infoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        iconView = findViewById(R.id.icon);
        citysView = findViewById(R.id.citys);
        infoLayout = findViewById(R.id.infoLayout);

        View button = findViewById(R.id.button1);

        button.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            final GuideHelper guideHelper = new GuideHelper(MainActivity.this);

            TipData tipData1 = new TipData(R.drawable.tip1, Gravity.RIGHT | Gravity.BOTTOM, iconView);
            tipData1.setLocation(0, -DisplayUtils.dipToPix(v.getContext(), 50));
            guideHelper.addPage(tipData1);
            //
            TipData tipData2 = new TipData(R.drawable.tip2, citysView);
            guideHelper.addPage(tipData2);
            //

            TipData tipData3 = new TipData(R.drawable.tip3, infoLayout);
            guideHelper.addPage(tipData3);

            guideHelper.addPage(tipData1, tipData2, tipData3);

            //add custom view
            LayoutInflater ll = LayoutInflater.from(MainActivity.this);
            View testView = ll.inflate(R.layout.custom_view,null);
            TipData tipDataCustom= new TipData(Gravity.CENTER,new Rect(),testView);
            testView.findViewById(R.id.guide_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guideHelper.dismiss();
                }
            });
            guideHelper.setAutoDismiss(false);//一般不设置，默认是true
            guideHelper.addPage(tipDataCustom);

            guideHelper.show(false);
//            guideHelper.show(true);

        }
    };
}
