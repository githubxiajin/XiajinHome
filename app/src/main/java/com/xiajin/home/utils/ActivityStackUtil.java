package com.xiajin.home.utils;

import java.util.Stack;

import android.app.Activity;
import android.util.Log;

public class ActivityStackUtil {
	public static Stack<Activity> ACTIVITY_STACK = new Stack<Activity>();// �����ACTIVITYջ

	public static void clearStack() {
		while (!ActivityStackUtil.ACTIVITY_STACK.isEmpty()) {
			Activity top = ActivityStackUtil.ACTIVITY_STACK.peek();
			if (top != null) {
				top.finish();
				ActivityStackUtil.ACTIVITY_STACK.pop();
				Log.i("移除", top.toString());
			} else {
				ActivityStackUtil.ACTIVITY_STACK.pop();
				Log.i("移除", top.toString());
			}
		}
	}

}
