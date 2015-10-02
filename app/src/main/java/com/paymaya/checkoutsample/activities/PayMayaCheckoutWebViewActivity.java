package com.paymaya.checkoutsample.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ClientCertRequest;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.paymaya.checkoutsample.R;

/**
 * Created by samfrancisco on 10/1/15.
 */
public class PayMayaCheckoutWebViewActivity extends Activity {

  public static final String KEY_REDIRECT_URL = "redirectUrl";
  public static final String KEY_SUCCESS_URL = "successUrl";
  public static final String KEY_FAILURE_URL = "failureUrl";
  public static final String KEY_CANCEL_URL = "cancelUrl";
  public static final int RESULT_FAILURE = 4321;

  private static final String TAG = PayMayaCheckoutWebViewActivity.class.getSimpleName();

  private WebView checkoutWebView;
  private String redirectUrl = "";
  private String successUrl = "";
  private String failureUrl = "";
  private String cancelUrl = "";

  private CheckoutWebViewClient checkoutWebViewClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_paymaya_checkout_webview);

    Intent passedIntent = getIntent();
    if (null != passedIntent) {
      /**
       * Catch the passed URLs from the MainActivity
       */
      redirectUrl = passedIntent.getStringExtra(KEY_REDIRECT_URL);
      successUrl = passedIntent.getStringExtra(KEY_SUCCESS_URL);
      failureUrl = passedIntent.getStringExtra(KEY_FAILURE_URL);
      cancelUrl = passedIntent.getStringExtra(KEY_CANCEL_URL);

      Log.d(TAG, "redirectUrl : " + redirectUrl);
      Log.d(TAG, "successUrl : " + successUrl);
      Log.d(TAG, "failureUrl : " + failureUrl);
      Log.d(TAG, "cancelUrl : " + cancelUrl);
    }

    /**
     * Initialize our custom WebViewClient that will detect
     * the URLs that are being loaded in the WebView
     */
    checkoutWebViewClient = new CheckoutWebViewClient();
    initializeViews();
  }

  private void initializeViews() {
    checkoutWebView = (WebView) findViewById(R.id.activity_paymaya_checkout_webview);

    /**
     * Inject our custom WebViewClient to the WebView
     */
    checkoutWebView.setWebViewClient(checkoutWebViewClient);

    /**
     * Allow File Access and Javascript
     */
    checkoutWebView.getSettings().setAllowFileAccess(true);
    checkoutWebView.getSettings().setJavaScriptEnabled(true);

    /**
     * Load the Checkout Page
     */
    checkoutWebView.loadUrl(redirectUrl);
  }

  private class CheckoutWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
      Log.i(TAG, "shouldOverrideUrlLoading");
      Log.d(TAG, "urlString : " + urlString);

      /**
       * URL loaded is the Success URL
       */
      if (urlString.startsWith(successUrl)) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        PayMayaCheckoutWebViewActivity.this.finish();
      }

      /**
       * URL loaded is the Cancel URL
       */
      if (urlString.startsWith(cancelUrl)) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        PayMayaCheckoutWebViewActivity.this.finish();
      }

      /**
       * URL loaded is the Failure URL
       */
      if (urlString.startsWith(failureUrl)) {
        Intent intent = new Intent();
        setResult(RESULT_FAILURE, intent);
        PayMayaCheckoutWebViewActivity.this.finish();
      }

      /**
       * Everything else just pass-through
       */
      return super.shouldOverrideUrlLoading(view, urlString);
    }

  }

}
