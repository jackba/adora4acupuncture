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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import efan.zz.aa.android.AA;
import efan.zz.aa.android.R;
import efan.zz.customize.IdentifiedString;
import efan.zz.aa.android.customize.IdentifiedStringAdapter;
import efan.zz.aa.android.util.AAUtil;

public class WelcomeAA extends Activity
{
  private IdentifiedStringAdapter selectedAcupointAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.welcome_aa);

    init();
  }

  private void init()
  {
    final IdentifiedStringAdapter channelAdapter = new IdentifiedStringAdapter(AA.ctx,
            android.R.layout.simple_spinner_item);
    String sql = getResources().getString(R.string.SQL_LOAD_MERIDIAN_CHANNEL_FOR_ADAPTER);
    channelAdapter.loadNameOrKeyAutoCompAdapter(sql);
    channelAdapter.insert(new IdentifiedString("------", -1), 0);

    final IdentifiedStringAdapter allAcupointAdapter = new IdentifiedStringAdapter(AA.ctx,
            android.R.layout.simple_dropdown_item_1line);
    sql = getResources().getString(R.string.SQL_LOAD_ACUPOINT_FOR_ADAPTER);
    allAcupointAdapter.preLoadNameOrKeyAutoCompAdapter(sql);
    selectedAcupointAdapter = allAcupointAdapter;

    final Spinner channelText = (Spinner) findViewById(R.id.meridian_type);
    final AutoCompleteTextView acupointText = (AutoCompleteTextView) findViewById(R.id.acupoint_name);
    final Button queryBtn = (Button) findViewById(R.id.queryButton);

    channelText.setAdapter(channelAdapter);
    channelText.setOnItemSelectedListener(new OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        IdentifiedString selectedChannel = channelAdapter.getItem(position);
        if (selectedChannel.id == -1)
          return;
        
        String sql = getResources().getString(R.string.SQL_LOAD_ACUPOINT_FOR_ADAPTER_BY_CHANNEL);
        sql = sql.replace("?", ""+selectedChannel.id);

        IdentifiedStringAdapter partAcupointAdapter = new IdentifiedStringAdapter(AA.ctx,
            android.R.layout.simple_dropdown_item_1line);
        partAcupointAdapter.loadNameOrKeyAutoCompAdapter(sql);
        selectedAcupointAdapter = partAcupointAdapter;
        acupointText.setAdapter(selectedAcupointAdapter);
        acupointText.showDropDown();
      }

      public void onNothingSelected(AdapterView<?> parent)
      {}
    });
    
    acupointText.setAdapter(selectedAcupointAdapter);
    acupointText.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
        IdentifiedString selectedAcupoint = selectedAcupointAdapter.getItem(position);

        AAUtil.showActivity("" + selectedAcupoint.id, WelcomeAA.this, AcupointDetail.class);
      }
    });

    queryBtn.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        selectedAcupointAdapter = allAcupointAdapter;
        acupointText.setText("");
        acupointText.setAdapter(selectedAcupointAdapter);
        acupointText.showDropDown();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.home, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case R.id.menu_item_full_picture:
      AAUtil.showActivity(null, WelcomeAA.this, GlobalGallery.class);
      break;

//    case R.id.menu_item_patient:
//      AAUtil.youngGirlWarning("Sorry, Patient is planned for v2.");
//      break;
    
//    case R.id.menu_item_statistics:
//      AAUtil.youngGirlWarning("Sorry, Statistics is planned for v2.");
//      break;
      
    case R.id.menu_item_chinese_version:
      AAUtil.youngGirlWarning("Sorry but non English version is out the scope of ADC2. 抱歉ADC2不考虑中文版.");
      break;
    
    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }
}
