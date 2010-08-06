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

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import efan.zz.aa.AA;
import efan.zz.aa.R;
import efan.zz.aa.android.util.AAUtil;

/*
 * Implementation decision: 
 * Only pass rx_recipe id as parameter,
 * To query the DB again: if it's slow to user, 
 * the query must be optimized anyway otherwise it's slow in AcupointDetail page already!
 */
public class AcupointDetailEdit extends Activity
{
  private EditText descView; 
  
  private String acupointId0;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.acupoint_detail_edit);
    
    init();
    
    initSaveAction();
    initCancelAction();
  }

  private void init()
  {
    final Intent intent = getIntent();
    acupointId0 = Uri.decode(intent.getDataString());

    // query DB for detail...
    final String sql = getResources().getString(R.string.SQL_QUERY_ACUPOINT_BY_ORDER_NUM);
    final Cursor cursor = AA.db.rawQuery(sql, new String[]{acupointId0});
    String name = null;
    String cnName = null;
    String code = null;
    String desc = null;
    try
    {
      cursor.moveToNext();
      name = cursor.getString(1);
      cnName = cursor.getString(2);
      code = cursor.getString(3);
      int descIdx = 6;
      if (AAUtil.isChinese())
        descIdx = 7;
      desc = cursor.getString(descIdx);
    }
    finally
    {
      cursor.close();
    }
        
    final TextView nameView = (TextView) findViewById(R.id.acupoint_name_edit);
    nameView.setText(code + ": " + name + " = " + cnName);

    descView = (EditText) findViewById(R.id.acupoint_desc_edit);
    descView.setText(desc);
  }
    
  private void initSaveAction()
  {
    Button.OnClickListener listener = new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        int confirmMsgId = R.string.acupoint_detail_edit_confirm_update;

        new AlertDialog.Builder(AcupointDetailEdit.this)
          .setTitle(R.string.alert_dialog_confirm_title)
          .setIcon(R.drawable.alert_dialog_icon)
          .setMessage(confirmMsgId)
          .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() 
          {
              public void onClick(DialogInterface dialog, int whichButton) 
              {
                saveData();
                AAUtil.showActivity(acupointId0, AcupointDetailEdit.this, AcupointDetail.class);
              }
          })
          .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() 
          {
              public void onClick(DialogInterface dialog, int whichButton) {}
          })
          .create()
          .show();
      }
    };

    Button btn = (Button) findViewById(R.id.acupointSaveBtn);
    btn.setOnClickListener(listener);
  }
  private void initCancelAction()
  {
    Button btn = (Button) findViewById(R.id.acupointCancelBtn);
    btn.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        AAUtil.showActivity(acupointId0, AcupointDetailEdit.this, AcupointDetail.class);
      }
    });
  }
  
  private void saveData()
  {
    AA.db.beginTransaction();
    try
    {
      final String desc = descView.getText().toString().trim();
      
      ContentValues recipeValues = new ContentValues();
      if (AAUtil.isChinese())
        recipeValues.put("DESCRIPTION_ZH", desc);
      else
        recipeValues.put("DESCRIPTION", desc);
      AA.db.update("ACUPOINT", recipeValues, "PK_ID=?", new String[]{acupointId0});
      
      AA.db.setTransactionSuccessful();
    }
    finally
    {
      AA.db.endTransaction();
    }
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
      
    case R.id.menu_item_relax:
      AAUtil.showActivity(null, AcupointDetailEdit.this, RestGallery.class);
      break;
    
    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }
}
