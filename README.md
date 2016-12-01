## GuideHelper 实现Android新手引导页
可以在提示也绘制实际的view，在view的上下左右位置添加提示信息的图片  
  
**gradle导入**   
  compile 'com.shizhefei:GuideHelper:1.0.5'  
#代码如下：  

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
            
#效果如下：  
![image](https://github.com/LuckyJayce/GuideHelper/blob/master/raw/g1.gif)  

##主力类库##

**1.https://github.com/LuckyJayce/ViewPagerIndicator**  
Indicator 取代 tabhost，实现网易顶部tab，新浪微博主页底部tab，引导页，无限轮播banner等效果，高度自定义tab和特效

**2.https://github.com/LuckyJayce/MVCHelper**  
实现下拉刷新，滚动底部自动加载更多，分页加载，自动切换显示网络失败布局，暂无数据布局，支持任意view，支持切换主流下拉刷新框架。

**3.https://github.com/LuckyJayce/MultiTypeView**  
简化RecyclerView的多种type的adapter，Fragment可以动态添加到RecyclerView上，实现复杂的界面分多个模块开发

**4.https://github.com/LuckyJayce/EventBus**  
事件总线，通过动态代理接口的形式发布,接收事件。定义一个接口把事件发给注册并实现接口的类

**5.https://github.com/LuckyJayce/LargeImage**  
大图加载，可供学习

**6.https://github.com/LuckyJayce/GuideHelper**  
新手引导页，轻松的实现对应的view上面的显示提示信息和展示功能给用户  

**7.https://github.com/LuckyJayce/HVScrollView**  
可以双向滚动的ScrollView，支持嵌套ScrollView联级滑动，支持设置支持的滚动方向

**8.https://github.com/LuckyJayce/CoolRefreshView**  
  下拉刷新RefreshView，支持任意View的刷新 ，支持自定义Header，支持NestedScrollingParent,NestedScrollingChild的事件分发，嵌套ViewPager不会有事件冲突 

有了这些类库，让你6的飞起

# 联系方式和问题建议

* 微博:http://weibo.com/u/3181073384
* QQ 群: 开源项目使用交流，问题解答: 549284336（开源盛世） 

License
=======

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
