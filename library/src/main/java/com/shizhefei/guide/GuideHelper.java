/*
Copyright 2016 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.shizhefei.guide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.LinkedList;

public class GuideHelper {

    private Activity activity;

    private LinkedList<TipData[]> pages = new LinkedList<TipData[]>();

    private Dialog baseDialog;

    public GuideHelper(Activity activity) {
        super();
        this.activity = activity;
    }

    public GuideHelper addPage(TipData... tipDatas) {
        pages.add(tipDatas);
        return this;
    }

    public OnDismissListener onDismissListener;

    private FrameLayout layout;

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public GuideHelper show() {
        show(true);
        return this;
    }

    private boolean autoPlay;

    /**
     *
     * @param autoPlay 是否自动播放提示
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public GuideHelper show(boolean autoPlay) {
        this.autoPlay = autoPlay;
        //关闭dialog，移除handler消息
        dismiss();
        handler.removeCallbacksAndMessages(null);

        //创建dialog
        layout = new FrameLayout(activity);
        baseDialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
        baseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x66000000));

        //设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = baseDialog.getWindow().getAttributes();
            localLayoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            localLayoutParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        }

        baseDialog.setContentView(layout);
        //设置dialog的窗口大小全屏
        baseDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //dialog关闭的时候移除所有消息
        baseDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacksAndMessages(null);
                if (onDismissListener != null) {
                    baseDialog.setOnDismissListener(onDismissListener);
                }
            }
        });
        //显示
        baseDialog.show();
        //显示提示界面，这里用view去post，是为了保证view已经显示，才能获取到view的界面画在提示界面上
        pages.get(0)[0].views[0].post(new Runnable() {

            @Override
            public void run() {
                send();
            }
        });
        return this;
    }

    private static final int MIN = 2000;
    private static final int MAX = 6000;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (!pages.isEmpty()) {
                send();
            } else {
                dismiss();
            }
        }
    };

    private void send() {
        TipData[] tipDatas = pages.poll();
        showIm(layout, tipDatas);
        if (autoPlay) {
            int d = tipDatas.length * 1500;
            if (d < MIN) {
                d = 2000;
            } else if (d > MAX) {
                d = MAX;
            }
            handler.sendEmptyMessageDelayed(1, d);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showIm(final FrameLayout layout, TipData... tipDatas) {
        Resources resources = layout.getResources();
        //移除掉之前所有的viwe
        layout.removeAllViews();
        //获取layout在屏幕上的位置
        int layoutOffset[] = new int[2];
        layout.getLocationOnScreen(layoutOffset);
        //循环提示的数据列表
        for (TipData data : tipDatas) {

            //循环需要提示的view
            View[] views = data.views;
            int[] location = new int[2];
            Rect rect = null;
            for (View view : views) {
                if (view.getVisibility() != View.VISIBLE) {
                    continue;
                }

                //获取view在屏幕的位置，后面会用这个位置绘制到提示的dialog。确保位置完美覆盖在view上
                view.getLocationOnScreen(location);
                //这里避免dialog不是全屏，导致view的绘制位置不对应
                location[1] -= layoutOffset[1];

                //获取view的宽高
                int vWidth = view.getMeasuredWidth();
                int vHeight = view.getMeasuredHeight();

                //如果宽高都小于等于0，再measure试下获取
                if (vWidth <= 0 || vHeight <= 0) {
                    LayoutParams layoutParams = view.getLayoutParams();
                    view.measure(layoutParams.width, layoutParams.height);
                    vWidth = view.getMeasuredWidth();
                    vHeight = view.getMeasuredHeight();
                }

                if (vWidth <= 0 || vHeight <= 0) {
                    continue;
                }

                if (data.needDrawView) {
                    //通过getDrawingCache的方式获取view的视图缓存
                    view.setDrawingCacheEnabled(true);
                    view.buildDrawingCache();
                    Bitmap bitmap = view.getDrawingCache();
                    if (bitmap != null) {
                        bitmap = Bitmap.createBitmap(bitmap);
                    } else {
                        //如果获取不到，则用创建一个view宽高一样的bitmap用canvas把view绘制上去
                        bitmap = Bitmap.createBitmap(vWidth, vHeight, Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);
                    }
                    //释放试图的缓存
                    view.setDrawingCacheEnabled(false);
                    view.destroyDrawingCache();

                    //把需要提示的view的视图设置到imageView上显示
                    ImageView imageView = new ImageView(activity);
                    imageView.setScaleType(ScaleType.CENTER_INSIDE);
                    imageView.setImageBitmap(bitmap);

                    //如果使用者还配置了提示view的背景颜色，那么也设置显示
                    if (data.viewBg != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            imageView.setBackground(data.viewBg);
                        } else {
                            imageView.setBackgroundDrawable(data.viewBg);
                        }
                    }
                    // imageView.setBackgroundColor(Color.GRAY);

                    if (data.onClickListener != null)
                        imageView.setOnClickListener(data.onClickListener);


                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.leftMargin = location[0];
                    params.topMargin = location[1];
                    params.gravity = Gravity.LEFT | Gravity.TOP;
                    layout.addView(imageView, params);
                }

                if (rect == null) {
                    rect = new Rect(location[0], location[1], location[0] + vWidth, location[1] + vHeight);
                } else {
                    if (rect.left > location[0]) {
                        rect.left = location[0];
                    }
                    if (rect.right < location[0] + vWidth) {
                        rect.right = location[0] + vWidth;
                    }
                    if (rect.top > location[1]) {
                        rect.top = location[1];
                    }
                    if (rect.bottom < location[1] + vHeight) {
                        rect.bottom = location[1] + vHeight;
                    }
                }
            }
            if (rect == null) {
                continue;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(resources, data.resourceId);

            ImageView tip = new ImageView(activity);
            // tip.setScaleType(ScaleType.CENTER_INSIDE);
            tip.setImageBitmap(bitmap);

            int imageH = bitmap.getHeight();
            int imageW = bitmap.getWidth();

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            int x = 0;
            switch (data.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    x = (rect.left + rect.right) / 2 - imageW / 2;
                    break;
                case Gravity.RIGHT:
                    x = rect.right;
                    break;
                case Gravity.LEFT:
                default:
                    x = rect.left - imageW;
            }
            int y = 0;
            switch (data.gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.CENTER_VERTICAL:
                    y = (rect.top + rect.bottom) / 2 - imageH / 2;
                    break;
                case Gravity.BOTTOM:
                    y = rect.bottom;
                    break;
                case Gravity.TOP:
                default:
                    y = rect.top - imageH;
            }

            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

            x += data.offsetX;
            y += data.offsetY;

            if (x < 0) {
                x = 0;
            } else if (x + imageW > displayMetrics.widthPixels) {
                x = displayMetrics.widthPixels - imageW;
            }
            if (y < 0) {
                y = 0;
            } else if (y + imageH > displayMetrics.heightPixels) {
                y = displayMetrics.heightPixels - imageH;
            }

            params.leftMargin = x;
            params.topMargin = y;
            params.gravity = Gravity.LEFT | Gravity.TOP;
            layout.addView(tip, params);
        }

        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pages.isEmpty()) {
                    dismiss();
                } else {
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    public void dismiss() {
        if (baseDialog != null)
            baseDialog.dismiss();
        baseDialog = null;
    }

    public static class TipData {
        View[] views;
        int resourceId;
        int gravity = DEFAULT_GRAVITY;
        boolean needDrawView = true;
        private static final int DEFAULT_GRAVITY = Gravity.BOTTOM | Gravity.CENTER;

        OnClickListener onClickListener;
        private int offsetX;
        private int offsetY;
        private Drawable viewBg;

        public TipData(int resourceId, View... views) {
            this(resourceId, DEFAULT_GRAVITY, views);
        }

        public TipData(int resourceId, int gravity, View... views) {
            super();
            this.views = views;
            this.gravity = gravity;
            this.resourceId = resourceId;
        }

        public TipData setLocation(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public TipData setLocation(int gravity, int offsetX, int offsetY) {
            this.gravity = gravity;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            return this;
        }

        public TipData setLocation(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            return this;
        }

        public TipData setViewBg(Drawable viewBg) {
            this.viewBg = viewBg;
            return this;
        }

        public TipData setNeedDrawView(boolean needDrawView) {
            this.needDrawView = needDrawView;
            return this;
        }

        public TipData setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }
    }

}
