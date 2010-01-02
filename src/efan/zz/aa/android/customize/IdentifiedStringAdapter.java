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
package efan.zz.aa.android.customize;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;
import efan.zz.aa.AA;
import efan.zz.customize.IdentifiedString;
import efan.zz.aa.android.util.AAUtil;

public class IdentifiedStringAdapter extends ArrayAdapter<IdentifiedString>
{
  private boolean isReady = false;
  
  public IdentifiedStringAdapter(Context context, int textViewResourceId)
  {
    super(context, textViewResourceId);
  }

  public boolean isReady()
  {
    return isReady;
  }

  public void setReady(boolean isReady)
  {
    this.isReady = isReady;
    
    if (isReady)
      AAUtil.doNotifyAll(this);
  }

  public void preLoadNameOrKeyAutoCompAdapter(final String sql)
  {
    // Create thread to load AutoComplete data for selection...
    Thread loadThread = new Thread()
    {
      public void run()
      {
        loadNameOrKeyAutoCompAdapter(sql);
      }
    };
    loadThread.start();
  }
  
  public synchronized void loadNameOrKeyAutoCompAdapter(final String sql)
  {
    this.setReady(false);
    
    Cursor cursor = AA.db.rawQuery(sql, null);
    try
    {
      while (cursor.moveToNext())
      {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String chName = cursor.getString(2);
        String code = cursor.getString(3);
        this.add(new IdentifiedString(code + ": " + name + " = " + chName, id));
      }

      cursor.moveToPosition(-1);
      while (cursor.moveToNext())
      {
        String alias = cursor.getString(4);
        if (alias == null || alias.trim().length() == 0)
          continue;
        
        int id = cursor.getInt(0);
        String chName = cursor.getString(2);
        String code = cursor.getString(3);
        String alia[] = alias.split(",");
        int n = alia.length;
        for (int i=0; i<n; i++)
        {
          if (alia[i].trim().length() > 0)
            this.add(new IdentifiedString(code + ": " + alia[i].trim() + " = " + chName, id));
        }
      }
    }
    catch (Throwable t)
    {
      Log.e(this.getClass().getName(), t.getLocalizedMessage(), t);
      try { Thread.sleep(3600000);} catch (InterruptedException e1){}
      throw new RuntimeException(t.toString(), t);
    }
    finally
    {
      cursor.close();
    }
    
    this.setReady(true);
  }
  
  public int findPosition(final int id)
  {
    int n = this.getCount();
    for (int i=0; i<n; i++)
    {
      if (this.getItem(i).id == id)
        return i;
    }
    
    return -1;
  }

  public int findPosition(final String value)
  {
    int n = this.getCount();
    for (int i=0; i<n; i++)
    {
      if (this.getItem(i).value.equals(value))
        return i;
    }
    
    return -1;
  }
}
