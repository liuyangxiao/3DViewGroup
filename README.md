# 3DViewGroup
 
	
   ![img](https://github.com/liuyangxiao/3DViewGroup/blob/master/3d.gif)

    #项目引用
      implementation 'com.icefordog:3dviewGroup:2.0.0'
   # 使用方式如下



      <com.burning.foethedog.Rota3DSwithView
           android:id="@+id/_test_Rota3DVSwithView"
                   android:layout_width="400dp"
                   android:layout_height="300dp"
                   app:autoscroll="true"
                   app:heightRatio="0.8"

                   app:rotateV="true"
                   app:rotation="40"
                   app:widthRatio="0.6">

              #是否自动滚动      app:autoscroll="true"
              #子控件对于改viegroup 中的宽高比例 默认无为0.7     app:heightRatio  app:widthRatio
              #垂直或者水平滑动    app:rotateV="true"
              #偏转角度      app:rotation="40"
              #旋转速度 -----app:speed
              setR3DPagechange   监听滑动页面正面页的变化

#<com.burning.foethedog.Rota3DSwithViewList/>
        添加list模式
         mRota3DSwithViewList.setAdapter(new RecyclerView.Adapter() {})
         借用的是RecyclerView.Adapter--其他基本一致
