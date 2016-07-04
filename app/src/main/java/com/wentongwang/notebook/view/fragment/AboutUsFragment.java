package com.wentongwang.notebook.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.wentongwang.notebook.R;

/**
 * 这里尝试下webview
 * Created by Wentong WANG on 2016/7/1.
 */
public class AboutUsFragment extends Fragment{

    private WebView myWeb;
    private TextView tvTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.about_us_fragment_layout, container, false);
        myWeb = (WebView) root.findViewById(R.id.my_web);
        tvTitle = (TextView) root.findViewById(R.id.title);
        initWeb();
        return root;
    }

    private void initWeb() {
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvTitle.setText(title);
            }

        };
        // 设置setWebChromeClient对象
        myWeb.setWebChromeClient(wvcc);

        myWeb.loadUrl("file:///android_asset/about_us.html");
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
