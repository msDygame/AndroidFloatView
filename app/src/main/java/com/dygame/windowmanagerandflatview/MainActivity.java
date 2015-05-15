package com.dygame.windowmanagerandflatview;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener
{
    protected static String TAG = "" ;
    protected WindowManager windowManager = null;
    protected FloatView floatView = null;
    protected Button pButtonQuit ;
    protected Button pButtonFlee ;
    protected boolean IsRemoveView = true ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消標題欄 //need called before super.call
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //crash handle
        MyCrashHandler pCrashHandler = MyCrashHandler.getInstance();
        pCrashHandler.init(getApplicationContext());
        TAG = pCrashHandler.getTag() ;
        //
        pButtonQuit = (Button)findViewById(R.id.buttonQuit) ;
        pButtonFlee = (Button)findViewById(R.id.buttonFlee) ;
        pButtonQuit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                windowManager.removeView(floatView);//立即
                IsRemoveView = false ;//remove2次會出現error
                finish() ;
            }
        });
        pButtonFlee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                IsRemoveView = false ;
                finish() ;
            }
        });
        //create view
        floatView = new FloatView(getApplicationContext());
        floatView.setOnClickListener(this);
        floatView.setTag(TAG) ;
        floatView.setImageResource(R.mipmap.ic_launcher);
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        floatView.SetWindowParams(windowManager) ;
    }
    @Override
    public void onClick(View v)
    {
        Toast.makeText(this, "FLOAT VIEW", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (IsRemoveView) windowManager.removeView(floatView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
