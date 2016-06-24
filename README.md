## GuideHelper 实现Android新手引导页
可以在提示也绘制实际的view，在view的上下左右位置添加提示信息的图片  
  
**gradle导入**   
  compile 'com.shizhefei:GuideHelper:1.0.0'  
#代码如下：  

            GuideHelper guideHelper = new GuideHelper(MainActivity.this);

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

            guideHelper.show();
            
#效果如下：  
![image](https://github.com/LuckyJayce/GuideHelper/blob/master/raw/g.gif)  

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
