package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.BuildConfig;
import mobi.cangol.mobile.uiframe.demo.R;

public class WebFragment extends BaseContentFragment {
    private WebView webView;
    private String mUrl;
    private String mTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mUrl = bundle.getString("url");
            mTitle = bundle.getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_web, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    protected void findViews(View view) {
        this.setTitle(this.getClass().getSimpleName());
        webView = view.findViewById(R.id.webView);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        android.webkit.WebSettings webSettings = webView.getSettings();
        webSettings.setTextZoom(100);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //支持使用屏幕上的缩放控件和手势进行缩放
        webSettings.setSupportZoom(true);
        //是否使用内置的缩放机制,是否显示缩放按钮
        webSettings.setBuiltInZoomControls(false);
        //自适应屏幕大小
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //允许执行JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        //允许支持插件
        webSettings.setPluginState(android.webkit.WebSettings.PluginState.ON);
        //不保存密码
        webSettings.setSavePassword(false);
        /**
         //让JavaScript自动打开窗口,适用于JavaScript方法window.open()
         //webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
         //允许在WebView中访问内容URL
         //webSettings.setAllowContentAccess(true);
         //允许访问文件
         //webSettings.setAllowFileAccess(true);
         **/

        //应用缓存API可用
        webSettings.setAppCacheEnabled(true);
        //DOM存储API可用
        webSettings.setDomStorageEnabled(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        /**
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
         webSettings.setAllowFileAccessFromFileURLs(BuildConfig.DEBUG ? true : false);
         }
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
         webSettings.setAllowUniversalAccessFromFileURLs(BuildConfig.DEBUG ? true : false);
         }**/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //添加图片不显示解决方案
            webView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //设置WebView的用户代理字符串
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " fdzq");

        webView.requestFocus();
    }

}
