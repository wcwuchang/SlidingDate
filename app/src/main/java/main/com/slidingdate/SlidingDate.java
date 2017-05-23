package main.com.slidingdate;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 吴昶 on 2017-5-22.
 */
public class SlidingDate extends LinearLayout {

    //GridView的适配器
    private CalendarAdapter adapter;
    private GridView mgvDate;

    private String begintime;
    private String endtime;
    private int mintYear;//年份
    private int mintMonth;//月份
    private int mintDay;//本月几号
    private int mintDayOfWeek;//今天周几
    private int mintDaysOfMonth;//本月天数
    private int mintDaysOfLastMonth;//上月天数
    //滑动坐标记录
    private float mfbeginl;
    private float mfend;
    private int mintClickDay;//选中的天的日期
    private List<Integer> mdaylist;
    private Calendar cale;
    private String url="";
    private boolean isSlideLeft=true;//手势滑动，默认为向左滑，用于区分载入动画

    private DateChangedListener listener;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy:MM:dd");

    public SlidingDate(Context context) {
        super(context);
    }

    public void setListener(DateChangedListener listener) {
        this.listener = listener;
    }

    public SlidingDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_sliding_date,this);
        mgvDate=(GridView)findViewById(R.id.gv_calendar);

        adapter=new CalendarAdapter(context);
        mdaylist=new ArrayList<Integer>();

        mgvDate.setAdapter(adapter);
        mgvDate.setOnTouchListener(gvListener);
        mgvDate.setLayoutAnimation(getAnimationController());

        getCurrent();//获取当前时间
        showCalendar(mintDay,mintMonth,mintYear,8);//初始化数据

        mgvDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dayDetailClick(i);
            }
        });
    }

    public SlidingDate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void getCurrent(){
        //获取当前年月日周信息
        if(cale==null) cale=Calendar.getInstance();
        mintYear=cale.get(Calendar.YEAR);
        mintMonth=cale.get(Calendar.MONTH)+1;//0到11表示1到12月，此处+1表示从1到12与月份对应
        mintDay=cale.get(Calendar.DAY_OF_MONTH);
        mintDayOfWeek=cale.get(Calendar.DAY_OF_WEEK)-1;//1到7为周一到周日，此处-1表示从0到6表示周日到周六
        if(listener!=null) {
            listener.currentDate(mintYear,mintMonth,mintDay);
        }
    }

    /**
     * 恢复到当前日期
     */
    public void revertToCurrent(){
        getCurrent();
        showCalendar(mintDay,mintMonth,mintYear,8);
    }

    /**
     * 获取当前日期
     * @return
     */
    public String getCurrentDate(){
        return mintYear+"年"+mintMonth+"月"+mintDay+"日";
    }

    /**
     * 根据年月日，以及是否选中对界面更新
     * @param day
     * @param month
     * @param year
     * @param click 默认 8 为未选中状态
     *
     */
    private void showCalendar(int day,int month,int year,int click){
        if(mdaylist!=null) {
            mdaylist.clear();
        }
        mintDaysOfMonth=TimeUtil.getDaysOfMonth(year,month);
        for(int i=0;i<7;i++){
            if(day+i-mintDayOfWeek>mintDaysOfMonth){
                mdaylist.add(i, day+i-mintDayOfWeek-mintDaysOfMonth);
            }else if(day+i-mintDayOfWeek<=0){
                mintDaysOfLastMonth=getDaysOfLastMonth(year,month);
                mdaylist.add(i, day+i-mintDayOfWeek+mintDaysOfLastMonth);
            }else{
                mdaylist.add(i, day+i-mintDayOfWeek);
            }
            Log.d("ss", ""+mdaylist.get(i)+"/"+day);
        }
        mgvDate.setLayoutAnimation(getAnimationController());
        adapter.setData(mdaylist,click);
        showDayDetailOnScrenn(day,month,year);
    }

    private void showDayDetailOnScrenn(int day,int month,int year){
        if(day<=mdaylist.get(0)&&day<=mdaylist.get(6)){
            if(month>1){
                begintime=year+"年"+(month-1 )+"月"+mdaylist.get(0)+"日";
            }else{
                begintime=(year-1)+"年"+12+"月"+mdaylist.get(0)+"日";
            }
            endtime=year+"年"+month+"-"+mdaylist.get(6);
        }else if(day>=mdaylist.get(0)&&day>=mdaylist.get(6)){
            begintime=year+"年"+(month)+"月"+mdaylist.get(0)+"日";
            if(month<12){
                endtime=year+"年"+(month+1)+"月"+mdaylist.get(6)+"日";
            }else{
                endtime=(year+1)+"年"+1+"月"+mdaylist.get(6)+"日";
            }
        }else if(day>=mdaylist.get(0)&&day<=mdaylist.get(6)){
            begintime=year+"年"+month+"月"+mdaylist.get(0)+"日";
            endtime=year+"年"+month+"月"+mdaylist.get(6)+"日";
        }

        if(listener!=null) {
            listener.timeInterval(begintime,endtime);
            listener.ymw(mintYear,mintMonth,day,TimeUtil.getFewOfWeek(mintDayOfWeek));
        }
    }

    /**
     * 获取对应年月的上一月天数
     * @param year
     * @param month
     * @return
     */
    private int getDaysOfLastMonth(int year,int month){
        return TimeUtil.getDaysOfMonth(year,month-1);
    }


    /**
     * 根据手势的滑动设置不同的动画效果
     * 右滑  从上方出现
     * 左滑  从下方出现
     * @return
     */
    private LayoutAnimationController getAnimationController(){
        int duration=100;
        float isLeft=-1.0f;
        if(isSlideLeft){
            isLeft=1.0f;
        }
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                isLeft, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }


    /**
     * 为gridview设置的滑动监听
     * 记录滑动的起始位置以及最终位置
     */
    private OnTouchListener gvListener=new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mfbeginl=event.getX();//滑动的起始坐标x值
                    break;
                case MotionEvent.ACTION_UP:
                    mfend=event.getX();//滑动的结束坐标x值
                    showCalendarChanger(mfbeginl,mfend);
                    break;
                default:
                    break;
            }
            return false;
        }
    };


    /**
     * 手势滑动判断
     * 设置为左右滑动超过40则更新界面
     * @param down
     * @param up
     */
    private void showCalendarChanger(float down,float up){
        if(down-up<40&&down-up>-40) return;
        if(down-up>40){
            isSlideLeft=true;//左滑
            mintDay+=7;
            if(mintDay>mintDaysOfMonth){
                mintDay=mintDay-mintDaysOfMonth;
                if(mintMonth==12){
                    mintMonth=1;
                    mintYear++;
                }else {
                    mintMonth++;
                }
            }
        }else if(down-up<-40){
            isSlideLeft=false;//右滑
            mintDay-=7;
            if(mintDay<=0){
                if(mintMonth==1){
                    mintMonth=12;
                    mintDay=31+mintDay;
                    mintYear--;
                }else{
                    mintMonth--;
                    mintDaysOfMonth=TimeUtil.getDaysOfMonth(mintYear, mintMonth);
                    mintDay=mintDaysOfMonth+mintDay;
                }
            }
        }
        showCalendar(mintDay,mintMonth,mintYear,8);//更新界面
    }

    /**
     * 给每个gridview中的每个格子增加点击事件
     * @param position
     */
    public void dayDetailClick(int position){
        mintClickDay=mdaylist.get(position);
        if(mintClickDay>mdaylist.get(6)){
            if(mintMonth==1){
                begintime=(mintYear-1)+"年"+12+"月"+mintClickDay+"日";
            }else{
                begintime=mintYear+"年"+(mintMonth-1)+"月"+mintClickDay+"日";
            }
        }else{
            begintime=mintYear+"年"+mintMonth+"月"+mintClickDay+"日";
        }
        endtime=begintime;
        adapter.setData(mdaylist,position);
        if(listener!=null) {
            listener.selectDate(begintime);
        }
    }



    public interface DateChangedListener{
        public void currentDate(int year,int month,int day);
        public void selectDate(String date);
        public void timeInterval(String date0,String date1);
        public void ymw(int year,int month,int day,String week);
    }

}
