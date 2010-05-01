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

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
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
import efan.zz.aa.android.customize.IdentifiedStringAdapter;
import efan.zz.aa.android.util.AAUtil;
import efan.zz.customize.IdentifiedString;

public class WelcomeAA extends Activity
{
  private IdentifiedStringAdapter selectedAcupointAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    if (isDataInstalled())
    {
      init();
    }
    else
      promptInstallAAData();
  }

  private void init()
  {
    setContentView(R.layout.welcome_aa);

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
        if (selectedAcupointAdapter == null)
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
    String[] aboutMsgs = getResources().getStringArray(R.array.about_msg);
    StringBuilder about = new StringBuilder();
    for (String msg : aboutMsgs)
      about.append(msg).append("\n");
    final AlertDialog pop = new AlertDialog.Builder(this).create();
    pop.setMessage(about);
    pop.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.button_market), 
            new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog, int which)
              {
                  AAUtil.searchMarket(WelcomeAA.this, "pub", "efansoftware");
              }
            });
    pop.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.button_ok), 
            new DialogInterface.OnClickListener()
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


  private boolean isDataInstalled()
  {
    File dataDir = Environment.getExternalStorageDirectory();
    dataDir = new File(dataDir, AA_DATA_DIR);
    if (dataDir.exists())
      return true;       // Data installed. Perfect!
    else
    return false;
  }

  private void promptInstallAAData()
  {
    StringBuilder msg = new StringBuilder();
    msg.append(getResources().getText(R.string.prompt_install_aa_data)).append("\n");
    AlertDialog dialog = new AlertDialog.Builder(this).create();
    dialog.setMessage(msg);
    String btnLabel = getResources().getString(R.string.btn_label_install_aa_data);
    dialog.setButton(DialogInterface.BUTTON_POSITIVE, btnLabel, 
      new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {
          AAUtil.searchMarket(WelcomeAA.this, "pub", "efansoftware");
        }
      });
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
  }

  private static final String   AA_DATA_DIR = "data/efan.zz/aa/data/drawable";
}
