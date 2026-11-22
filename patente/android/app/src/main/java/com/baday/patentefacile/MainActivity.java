package com.baday.patentefacile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.webkit.WebViewAssetLoader;

import java.util.Locale;

/**
 * Hosts the web app (assets/www) in a WebView, fully offline.
 * The page is served from https://appassets.androidplatform.net so it runs in a secure context
 * (localStorage, crypto.subtle). Text-to-speech uses Android's engine through the "AndroidApp" bridge,
 * because WebView has no speechSynthesis.
 */
public class MainActivity extends ComponentActivity {

    private static final String START_URL = "https://appassets.androidplatform.net/assets/www/index.html";
    private static final String ASSET_HOST = "appassets.androidplatform.net";

    private WebView web;
    private TextToSpeech tts;
    private volatile boolean ttsReady = false;
    private ValueCallback<Uri[]> fileCallback;
    private final ActivityResultLauncher<Intent> pickFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (fileCallback != null) {
                    fileCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(result.getResultCode(), result.getData()));
                    fileCallback = null;
                }
            });
    private final int[] insets = new int[4]; // left, top, right, bottom in px

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        web = new WebView(this);
        web.setBackgroundColor(getColor(R.color.page_bg));
        setContentView(web);

        boolean night = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES;
        WindowInsetsControllerCompat bars = WindowCompat.getInsetsController(getWindow(), web);
        bars.setAppearanceLightStatusBars(false);       // white icons on the blue header
        bars.setAppearanceLightNavigationBars(!night);  // dark icons on the light tab bar

        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setTextZoom(100);
        s.setSupportZoom(false);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return loader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (ASSET_HOST.equals(uri.getHost())) return false;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (ActivityNotFoundException ignored) {
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pushInsets();
            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                try {
                    pickFile.launch(params.createIntent());
                } catch (ActivityNotFoundException e) {
                    fileCallback = null;
                    return false;
                }
                return true;
            }
        });

        web.addJavascriptInterface(new Bridge(), "AndroidApp");

        ViewCompat.setOnApplyWindowInsetsListener(web, (v, windowInsets) -> {
            Insets b = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            insets[0] = b.left;
            insets[1] = b.top;
            insets[2] = b.right;
            insets[3] = b.bottom;
            pushInsets();
            return windowInsets;
        });

        tts = new TextToSpeech(this, status -> {
            ttsReady = status == TextToSpeech.SUCCESS;
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override public void onStart(String id) { }
            @Override public void onDone(String id) { notifyDone(id); }
            @Override public void onError(String id) { notifyDone(id); }
            @Override public void onStop(String id, boolean interrupted) { }
        });

        // Back goes through the page's history (its screens are #hash routes), then leaves the app.
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (web != null && web.canGoBack()) {
                    web.goBack();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        if (savedInstanceState != null) web.restoreState(savedInstanceState);
        else web.loadUrl(START_URL);
    }

    /** Safe-area insets go to the page as CSS variables (--sal, --sat, --sar, --sab). */
    private void pushInsets() {
        if (web == null) return;
        float d = getResources().getDisplayMetrics().density;
        String js = String.format(Locale.US,
                "(function(r){r.setProperty('--sal','%.1fpx');r.setProperty('--sat','%.1fpx');"
                        + "r.setProperty('--sar','%.1fpx');r.setProperty('--sab','%.1fpx');})(document.documentElement.style)",
                insets[0] / d, insets[1] / d, insets[2] / d, insets[3] / d);
        web.evaluateJavascript(js, null);
    }

    private void notifyDone(String id) {
        final String safe = id.replaceAll("[^A-Za-z0-9_]", "");
        runOnUiThread(() -> {
            if (web != null) web.evaluateJavascript("window.__ttsDone&&window.__ttsDone('" + safe + "')", null);
        });
    }

    private static Locale localeFor(String lang) {
        return "ar".equals(lang) ? Locale.forLanguageTag("ar") : Locale.ITALY;
    }

    /** Methods called from the page as window.AndroidApp.* */
    private class Bridge {
        @JavascriptInterface
        public boolean ttsAvailable(String lang) {
            if (!ttsReady) return false;
            int r = tts.isLanguageAvailable(localeFor(lang));
            return r >= TextToSpeech.LANG_AVAILABLE;
        }

        @JavascriptInterface
        public void speak(String text, String lang, String id, float rate) {
            if (!ttsReady) {
                notifyDone(id);
                return;
            }
            int r = tts.setLanguage(localeFor(lang));
            if (r < TextToSpeech.LANG_AVAILABLE) {
                notifyDone(id);
                return;
            }
            tts.setSpeechRate(Math.max(0.5f, Math.min(1.5f, rate)));
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, id);
        }

        @JavascriptInterface
        public void stop() {
            if (ttsReady) tts.stop();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        web.saveState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ttsReady) tts.stop();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) tts.shutdown();
        if (web != null) {
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }
}
