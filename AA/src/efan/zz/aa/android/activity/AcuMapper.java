/*
 * Copyright 2009 eFANsoftware
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package efan.zz.aa.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import efan.zz.aa.R;
import efan.zz.aa.android.util.AAUtil;

public class AcuMapper extends Activity
{
  private WebView webview;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.global_web);

    webview = (WebView) findViewById(R.id.global_webview);
    webview.setWebViewClient(new MyWebViewClient());
    webview.getSettings().setBuiltInZoomControls(true);
    webview.getSettings().setSupportZoom(true);
    webview.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    webview.loadUrl("http://www.rootdown.us/Points/PointMapPage.aspx");  
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {
    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack())
    {
      webview.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.global_web, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case R.id.menu_item_home:
      AAUtil.goHome(this);
      break;
      
    case R.id.menu_item_relax:
      AAUtil.showActivity(null, AcuMapper.this, RestGallery.class);
      break;
    
    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }


  private class MyWebViewClient extends WebViewClient
  {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
      view.loadUrl(url);
      return false;
    }
  }
}
