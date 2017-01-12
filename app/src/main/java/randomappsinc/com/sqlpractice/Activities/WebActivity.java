package randomappsinc.com.sqlpractice.Activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.Misc.TutorialServer;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 6/28/16.
 */
public class WebActivity extends StandardActivity {
    public static final String IDEA_KEY = "idea";

    @Bind(R.id.parent) View parent;
    @Bind(R.id.webview) WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String idea = getIntent().getStringExtra(IDEA_KEY);
        setTitle(idea);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.loadUrl(TutorialServer.get().getUrl(idea));
    }
}
