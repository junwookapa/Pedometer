package com.threabba.android.mylibrary;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by jun on 16. 12. 3.
 */

public class StepService2 extends Service{
    private TextView tv;											//항상 보이게 할 뷰
    int i = 0;
    private String DEFALUT="Count : ";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
    // http://isntyet.tistory.com/97 오버레이 권한 6.xx 버전에서 문제 생김 주의
    // http://blog.daum.net/mailss/20 출저
    // http://egloos.zum.com/louienine9/v/9453346 안드로이드 화면 온오프 받아오기
    // http://www.jksii.or.kr/upload/1/845_1.pdf 스텝수 검출 알고리즘
    // http://arabiannight.tistory.com/entry/180 컨텐츠 프로바이더
    // https://github.com/bagilevi/android-pedometer 구글 페도미터
    // http://www.findbestopensource.com/tagged/pedometer 페도미터 오픈소스

    // http://blog.naver.com/cestlavie_01/40188369345 죽지않는 서비스
    // https://github.com/greenrobot/greenDAO 그린다오
    // https://github.com/JakeWharton/butterknife 빠다 나이프
    @Override
    public void onCreate() {
        super.onCreate();

        tv = new TextView(this);		//뷰 생성
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(Color.RED);
        tv.setText(DEFALUT+i);
        //최상위 윈도우에 넣기 위한 설정
        /*WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,					//항상 최 상위에 있게
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,		//터치 인식, 나중에 기능 추가를 위해 일단 넣어둠
                PixelFormat.TRANSLUCENT);													//투명*/

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,//항상 최 상위. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  //포커스를 가지지 않음
                PixelFormat.TRANSLUCENT);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);	//윈도우 매니저 불러옴.
        wm.addView(tv, params);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                }
                return true;
            }
        });

        isRunning = true;
        updatedNumBer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tv != null)		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(tv);
            tv = null;
        }
    }
    Thread thread;
    final Handler handler = new Handler();
    boolean isRunning = false;
    private void updatedNumBer(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(DEFALUT + (++i));
                        }
                    });
                    SystemClock.sleep(1000);
                }
            }
        });
        thread.start();
    }
}
