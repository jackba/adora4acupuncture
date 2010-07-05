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

import efan.zz.aa.AA;
import efan.zz.aa.R;
import efan.zz.aa.android.customize.RestImageAdapter;
import efan.zz.aa.android.util.AAUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class RestGallery extends Activity
{
  private Integer[] mImageIds = 
  { 
      R.drawable.rest0,
      R.drawable.rest1, 
      R.drawable.rest2,
      R.drawable.rest9 
  };
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.rest_gallery);

    final ImageView img = (ImageView) findViewById(R.id.rest_image);
    img.setImageResource(mImageIds[0]);
    
    final Gallery g = (Gallery) findViewById(R.id.rest_gallery);
    RestImageAdapter restImgAdapter = new RestImageAdapter(AA.ctx);
    g.setAdapter(restImgAdapter);

    g.setOnItemClickListener(new OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id)
      {
        img.setImageResource(mImageIds[position]);
      }
    });
    
    AAUtil.addAd(this, R.id.ad_layout);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.relax, menu);

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
      
    case R.id.menu_item_credits:
      AAUtil.showActivity(null, RestGallery.this, CreditList.class);
      break;
    
    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }
}
