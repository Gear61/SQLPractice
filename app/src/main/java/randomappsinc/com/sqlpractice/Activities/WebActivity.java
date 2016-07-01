package randomappsinc.com.sqlpractice.Activities;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.Misc.TutorialServer;
import randomappsinc.com.sqlpractice.Misc.Utils;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 6/28/16.
 */
public class WebActivity extends StandardActivity {
    public static final String IDEA_KEY = "idea";

    @Bind(R.id.parent) View parent;
    @Bind(R.id.webview) WebView webView;

    private MaterialDialog loadingPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String idea = getIntent().getStringExtra(IDEA_KEY);
        setTitle(idea);

        loadingPage = new MaterialDialog.Builder(this)
                .title(R.string.page_load_progress)
                .content(R.string.loading_material)
                .progress(false, 100, true)
                .show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                loadingPage.setProgress(progress);
                if (progress == 100 && loadingPage.isShowing()) {
                    loadingPage.dismiss();
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Utils.showSnackbar(parent, getString(R.string.webpage_fail));
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        webView.loadUrl(TutorialServer.get().getUrl(idea));
    }
}
