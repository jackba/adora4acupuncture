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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import efan.zz.aa.AA;
import efan.zz.aa.R;
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
//    case R.id.menu_item_patient:
//      AAUtil.youngGirlWarning("Sorry, Patient is planned for v2.");
//      break;
    
//    case R.id.menu_item_statistics:
//      AAUtil.youngGirlWarning("Sorry, Statistics is planned for v2.");
//      break;
      
    case R.id.menu_item_about:
      showAbout();
      break;

    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }

  private void showAbout()
  {
    StringBuilder about = new StringBuilder();
    about.append("Adora AA=>\nAcupuncture Assistant 1.0.0\n")
         .append("\n") 
         .append("【Function】: Quick & easy query from hundreds of acupoints standardized by WHO. \n") 
         .append("\n") 
         .append("【Notes】: Contents come from internet (including WHO), for education / reference only, no commercial usage please. \n") 
         .append("\n") 
         .append("1/1/2010"); 
    final AlertDialog pop = new AlertDialog.Builder(this).create();
    pop.setMessage(about);
    pop.setButton(DialogInterface.BUTTON_POSITIVE, "Market", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        try
        {
          AAUtil.searchMarket(WelcomeAA.this, "pub", "efansoftware");
        } catch (Exception e)
        {
          Log.w(WelcomeAA.this.getClass().getName(), "", e);
          AAUtil.toastMessage("Sorry but Market application exception. Please check network or try again later...", true);
        }
      }
    });
    pop.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        pop.dismiss();
      }
    });
    pop.setCancelable(true);
    pop.setCanceledOnTouchOutside(true);
    pop.show();
  }
}
