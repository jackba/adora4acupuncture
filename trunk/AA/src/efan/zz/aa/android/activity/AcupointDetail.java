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

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import efan.zz.aa.android.AA;
import efan.zz.aa.android.R;
import efan.zz.aa.android.util.AAUtil;

public class AcupointDetail extends TabActivity
{
  private String acupointId;
  
  private TabHost mTabHost;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.acupoint_detail);
    
    init();
    
//    editRecipeAction();
  }

  private void init()
  {
    // Init Tab layout
    mTabHost = getTabHost();
    String descTab = getResources().getString(R.string.acupoint_detail_tab_desc_indicator);
    String imgTab = getResources().getString(R.string.acupoint_detail_tab_img_indicator);
    String imgTabChannel = getResources().getString(R.string.acupoint_detail_tab_img_indicator_channel);
    Drawable descTabIcon = getResources().getDrawable(R.drawable.aa_icon_tab_desc);
    Drawable imgTabIcon = getResources().getDrawable(R.drawable.aa_icon_tab_img);
    Drawable imgTabIconChannel = getResources().getDrawable(R.drawable.aa_icon_tab_img_channel);
    mTabHost.addTab(mTabHost.newTabSpec("tab_desc").setIndicator(descTab, descTabIcon).setContent(R.id.acupoint_desc_tab_view));
    mTabHost.addTab(mTabHost.newTabSpec("tab_img").setIndicator(imgTab, imgTabIcon).setContent(R.id.acupoint_image_tab_view));
    mTabHost.addTab(mTabHost.newTabSpec("tab_img_channel").setIndicator(imgTabChannel, imgTabIconChannel).setContent(R.id.acupoint_image_tab_view_channel));
    mTabHost.setCurrentTab(0);
    
    // query DB for detail by acupoint-id ...
    final Intent intent = getIntent();
    this.acupointId = Uri.decode(intent.getDataString());
    final String sql = getResources().getString(R.string.SQL_QUERY_ACUPOINT_BY_ID);
    final Cursor cursor = AA.db.rawQuery(sql, new String[]{acupointId});
    String imgFileId = null;
    String imgFileIdChannel = null;
    String name = null;
    String cnName = null;
    String code = null;
    String alias = null;
    String desc = null;
    try
    {
      cursor.moveToNext();
      imgFileId = cursor.getString(0);
      name = cursor.getString(1);
      cnName = cursor.getString(2);
      code = cursor.getString(3);
      alias = cursor.getString(4);
      desc = cursor.getString(5);
      imgFileIdChannel = cursor.getString(6);
      
      if (imgFileId == null)
        imgFileId = "";
      if (imgFileIdChannel == null)
        imgFileIdChannel = "";
    }
    finally
    {
      cursor.close();
    }
    
    // Apply DB data to view
    ImageView imgView = (ImageView) findViewById(R.id.acupoint_image_tab_view);
    ImageView imgViewChannel = (ImageView) findViewById(R.id.acupoint_image_tab_view_channel);
    int drawableId = getResources().getIdentifier(imgFileId, "drawable", "efan.zz.aa.android");
    int drawableIdChannel = getResources().getIdentifier(imgFileIdChannel, "drawable", "efan.zz.aa.android");
    imgView.setImageResource(drawableId);
    imgViewChannel.setImageResource(drawableIdChannel);
    
    final TextView nameView = (TextView) findViewById(R.id.acupoint_name);
    nameView.setText(code + ": " + name + " = " + cnName);

    final TextView descView = (TextView) findViewById(R.id.acupoint_desc);
    /// descView.setText("Alias: " + alias + "\n\n" + desc.replace("\\n", "\n "));
    descView.setText("[Alias]: " + alias + "\n\n" + desc);
    
    editAcupointAction();
  }

  private void editAcupointAction()
  {
    final Button btn = (Button) findViewById(R.id.acupointEditBtn);
    btn.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        Uri data = Uri.parse(Uri.encode(""+acupointId));
        Intent nextAct = new Intent();
        nextAct.setData(data);
        nextAct.setAction(Intent.ACTION_EDIT);
        nextAct.setClass(AcupointDetail.this, AcupointDetailEdit.class);
        startActivity(nextAct);
      }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.acupoint, menu);

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
      
    case R.id.menu_item_full_picture:
      AAUtil.showActivity(null, AcupointDetail.this, GlobalGallery.class);
      break;
    
    case R.id.menu_item_relax:
      AAUtil.showActivity(null, AcupointDetail.this, RestGallery.class);
      break;
    
    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }
}
