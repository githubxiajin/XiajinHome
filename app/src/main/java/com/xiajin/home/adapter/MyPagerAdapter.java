package com.xiajin.home.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
	private List<ImageView> imageViews;

	public MyPagerAdapter(List<ImageView> imageViews) {

		this.imageViews = imageViews;

	}

	// 返回总条数
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	/**
	 * ' 和getView()功能类似
	 *
	 * @param container
	 *            ：ViewPager容器
	 * @param position
	 *            位置
	 * @return 实例化图片返回结果
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 得到图片
		ImageView view = imageViews.get(position % imageViews.size());
		// 添加到容器中
		container.addView(view);
		// 返回实例化对象，最好直接返回实例
		return view;
	}

	/**
	 * 判断View是否是上面instantiateItem方法的返回值
	 *
	 * @param view
	 *            显示中的某一个页面
	 * @param object
	 *            上面instantiateItem方法的返回值
	 * @return
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		// if(view==object){
		// return true;
		// }else{
		// return false;
		// }
		return (view == object);
	}

	/**
	 * 销毁某条数据 刚开始一进来创建两个页面 最多能创建3个页面
	 *
	 * @param container
	 * @param position
	 * @param object
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// 销毁某一个View
		container.removeView((View) object);
		// super.destroyItem(container, position, object);
	}

}
