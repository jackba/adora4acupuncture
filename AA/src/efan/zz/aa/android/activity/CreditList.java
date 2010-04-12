/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package efan.zz.aa.android.activity;

import efan.zz.aa.R;
import efan.zz.aa.android.util.AAUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CreditList extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.credits);

    ListView list = (ListView) findViewById(R.id.credit_list);
    list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings));
  }

  private String[] mStrings = 
  { 
          "WHO.ORG", 
          "ACUPUNCTURE.COM", 
          "TCMADVISORY.COM", 
          "TCM888.COM/tupu/jingluo/",
          "ROOTDOWN.US : AcuMapper",

          "images.google.com" 
  };


  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.credits, menu);

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
      AAUtil.showActivity(null, CreditList.this, RestGallery.class);
      break;
    
    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }
}
