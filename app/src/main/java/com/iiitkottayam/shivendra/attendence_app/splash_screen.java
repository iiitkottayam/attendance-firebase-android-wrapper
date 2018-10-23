package com.iiitkottayam.shivendra.attendence_app;

import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class splash_screen extends AppCompatActivity {

    Thread splashTread;

    private static final int LOCK_REQUEST_CODE = 221;
    private static final int SECURITY_SETTING_REQUEST_CODE = 233;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
// Hide the status bar.

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_splash_screen);

        authenticate();

//        splashTread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    int waited = 0;
//                    // Splash screen pause time
//                    while (waited < 500) {              //rechange to 1500
//                        sleep(100);
//                        waited += 100;
//                    }
//                    Intent intent = new Intent(splash_screen.this,
//                            MainActivity.class);
//
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent);
//                    splash_screen.this.finish();
//                } catch (InterruptedException e) {
//                    // do nothing
//                } finally {
//                    splash_screen.this.finish();
//                }
//            }
//
//
//        };
//
//        splashTread.start();
    }

    private void authenticate(){
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //Check if the device version is greater than or equal to Lollipop(21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Create an intent to open device screen lock screen to authenticate
            //Pass the Screen Lock screen Title and Description
            Intent i = keyguardManager.createConfirmDeviceCredentialIntent(getResources().getString(R.string.unlock), getResources().getString(R.string.confirm_pattern));
            try {
                //Start activity for result
                startActivityForResult(i, LOCK_REQUEST_CODE);
            } catch (Exception e) {

                //If some exception occurs means Screen lock is not set up please set screen lock
                //Open Security screen directly to enable patter lock
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                try {

                    //Start activity for result
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE);
                } catch (Exception ex) {

                    //If app is unable to find any Security settings then user has to set screen lock manually
                    Toast.makeText(getApplicationContext(), "No Security measures are setup, Please setup screen lock to use this app", Toast.LENGTH_LONG).show();
                    splash_screen.this.finish();
                }
            }
//            webview = (WebView) findViewById(R.id.webView);
//
//            webview.setWebViewClient(new WebViewClient());
//            webview.getSettings().setJavaScriptEnabled(true);
//            webview.getSettings().setDomStorageEnabled(true);
//            webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
//            webview.loadUrl("http://iiitkottayam.gitlab.io/attendance/index.html");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCK_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(splash_screen.this,
                            MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    splash_screen.this.finish();

                }
                break;
            case SECURITY_SETTING_REQUEST_CODE:
                //When user is enabled Security settings then we don't get any kind of RESULT_OK
                //So we need to check whether device has enabled screen lock or not
                if (isDeviceSecure()) {
                    //If screen lock enabled show toast and start intent to authenticate user
                    Toast.makeText(this, getResources().getString(R.string.device_is_secure), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(splash_screen.this,
                            MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    splash_screen.this.finish();
                } else {
                    //If screen lock is not enabled just update text
                    Toast.makeText(getApplicationContext(), "No Security measures are setup, Please setup screen lock to use this app", Toast.LENGTH_LONG).show();
                    splash_screen.this.finish();
                }

                break;
        }
    }
    /**
     * method to return whether device has screen lock enabled or not
     **/
    private boolean isDeviceSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //this method only work whose api level is greater than or equal to Jelly_Bean (16)
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && keyguardManager.isKeyguardSecure();

        //You can also use keyguardManager.isDeviceSecure(); but it requires API Level 23

    }

}
