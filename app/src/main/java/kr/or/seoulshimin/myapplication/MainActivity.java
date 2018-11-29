package kr.or.seoulshimin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = (WebView) this.findViewById(R.id.webview1);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //새창 허용
        WebSettings set = web.getSettings();
        set.setSupportMultipleWindows(true);
        web.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg)
            {
                /*WebView newWebView = new WebView(MainActivity.this);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;*/

                WebView.HitTestResult result = view.getHitTestResult();
                String data = result.getExtra();
                Context context = view.getContext();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                context.startActivity(browserIntent);
                return false;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //alert  허용
                return super.onJsAlert(view, url, message, result);
            }
        });
        //캐시 삭제
        web.clearCache(true);
        web.clearHistory();

        //자바스크립트 허용
        web.getSettings().setJavaScriptEnabled(true);
        //헤더 SSCWS 기입 native 확인시 사용
        web.getSettings().setUserAgentString(
                this.web.getSettings().getUserAgentString()
                        + " "
                        + getString(R.string.user_agent_suffix)
        );

        //YTPlayer 실행 시 반드시 필요
        WebSettings settings = web.getSettings();
        settings.setDomStorageEnabled(true);

        //쿠키 동기화
        web.setWebViewClient(new WebViewClient(){

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        CookieSyncManager.getInstance().sync();
                    } else {
                        CookieManager.getInstance().flush();
                    }
                }
            }
        );

        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setSupportZoom(true);
        web.loadUrl("http://seoulshimin.or.kr/");
    }
    //뒤로가기 시 이전페이지
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }





}


