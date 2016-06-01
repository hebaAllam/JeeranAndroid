package apps.gn4me.com.jeeran.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import apps.gn4me.com.jeeran.activity.FoodAndBeveragesService;
import apps.gn4me.com.jeeran.activity.Services;

/**
 * Created by acer on 5/30/2016.
 */
public class RecycleTouchListenerFood  {
//    Context context;
//    private GestureDetector gestureDetector;
//    private FoodAndBeveragesService.ClickListener clickListener;
//    public RecycleTouchListenerFood(Context context,final RecyclerView recyclerView, final FoodAndBeveragesService.ClickListener clickListener) {
//        this.context=context;
//        this.clickListener=clickListener;
//        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return true;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                if (child != null && clickListener != null) {
//                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
//                }
//            }
//        });
//    }
//
//
//
//
//    @Override
//    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//        View child = rv.findChildViewUnder(e.getX(), e.getY());
//        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
//            clickListener.onClick(child, rv.getChildPosition(child));
//        }
//        return false;
//    }
//
//    @Override
//    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//
//
//    }
//
//    @Override
//    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//    }
}
