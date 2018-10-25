package com.example.leiyu.musicvisualizer;

import android.content.Context;

public class ViewUtil {
  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  @Deprecated
  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
