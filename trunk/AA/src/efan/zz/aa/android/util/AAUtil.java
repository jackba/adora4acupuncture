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
package efan.zz.aa.android.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import efan.zz.aa.android.AA;
import efan.zz.aa.android.activity.WelcomeAA;

public class AAUtil
{
  public static void doWait(Object syncObj)
  {
    synchronized(syncObj)
    {
      try
      {
        syncObj.wait();
      } catch (InterruptedException e)
      {
        Log.d("", "", e);
      }
    }
  }
  
  public static void doNotifyAll(Object syncObj)
  {
    synchronized(syncObj)
    {
      syncObj.notifyAll();
    }
  }
  
  public static void goHome(Activity caller)
  {
    Intent nextAct = new Intent();
    nextAct.setAction(Intent.ACTION_MAIN);
    nextAct.setClass(AA.ctx, WelcomeAA.class);
    caller.startActivity(nextAct);
  }

  public static void showActivity(String data, Activity caller, Class<? extends Activity> targetActivityClass)
  {
    Intent nextAct = new Intent();
    if (data != null)
    {
      Uri dataUri = Uri.parse(Uri.encode(data));
      nextAct.setData(dataUri);
    }
    nextAct.setAction(Intent.ACTION_VIEW);
    nextAct.setClass(AA.ctx, targetActivityClass);
    caller.startActivity(nextAct);
  }
  
  public static void youngGirlWarning(String msg)
  {
    if (msg == null)
      msg = "Hi, I am still growing! Try me later. Sorry lah...";
    Toast t = Toast.makeText(AA.ctx, msg, Toast.LENGTH_LONG);
    t.show();
  }

}
