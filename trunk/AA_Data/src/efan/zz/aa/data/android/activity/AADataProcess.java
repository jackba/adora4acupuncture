/*
 * Copyright 2010 eFANsoftware
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
package efan.zz.aa.data.android.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import efan.zz.aa.data.R;

public class AADataProcess extends Activity
{
  private static final String   DATA_DIR_BASE = "data/efan.zz/aa/data/";
  private static final String[] DATA_DIRS     = { "drawable" };
  private static final String   DATA_VERSION_FILE = "AADATAVERSION";

  private static final String   PROGRESS_KEY = "progress";
  private static final String   PROGRESS_MAX_KEY = "MAX";

  ProgressThread    progressThread;
  ProgressDialog    progressDialog;
  
  AlertDialog   finishDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    init();
  }

  void init()
  {
      progressDialog = new ProgressDialog(this);
      progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      progressDialog.setMessage(getResources().getText(R.string.progress_msg));
      progressThread = new ProgressThread(handler);
      progressThread.start();
      progressDialog.show();

      StringBuilder finishMsg = new StringBuilder();
      finishMsg.append(getResources().getText(R.string.finished_msg)) 
           .append("\n");
      finishDialog = new AlertDialog.Builder(this).create();
      finishDialog.setMessage(finishMsg);
      String btnLabel = getResources().getString(R.string.btn_label0);
      finishDialog.setButton(DialogInterface.BUTTON_POSITIVE, btnLabel, 
        new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
            startAA();
          }
        });
      btnLabel = getResources().getString(R.string.btn_label1);
      finishDialog.setButton(DialogInterface.BUTTON_NEUTRAL, btnLabel, 
        new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
            searchMarket(AADataProcess.this, "pub", "efansoftware");
          }
        });
      finishDialog.setCancelable(true);
      finishDialog.setCanceledOnTouchOutside(false);
  }

  // Define the Handler that receives messages from the thread and update the progress
  final Handler handler = 
    new Handler()
    {
      public void handleMessage(Message msg)
      {
        int progress = msg.getData().getInt(PROGRESS_KEY);
        int max = msg.getData().getInt(PROGRESS_MAX_KEY);
        if (progress != 0 && max != 0)
        {
          progressDialog.setMax(max);
          progressDialog.setProgress(progress);
        } else
        {
          progressDialog.dismiss();
          try
          {
            finishDialog.show();
          } catch (Exception e)
          {
            Log.d(this.getClass().getName(), "", e);
            finishDialog.dismiss();
            AADataProcess.this.finish();
          }
        }
      }
    };
    
  private void startAA()
  {
    Intent intent = new Intent();
    intent.setClassName("efan.zz.aa", "efan.zz.aa.android.activity.WelcomeAA");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    this.startActivity(intent);
  }

  // http://developer.android.com/guide/publishing/publishing.html#marketintent
  public static void searchMarket(Activity caller, String paramtype, String value)
  {
    Uri uri = Uri.parse("market://search?q=" + paramtype + ":" + value);
    Intent intent = new Intent();
    intent.setData(uri);
    intent.setAction(Intent.ACTION_VIEW);
    caller.startActivity(intent);
  }

  /* Nested class that performs progress calculations (counting) */
  private class ProgressThread extends Thread
  {
    Handler          mHandler;
    final static int STATE_DONE    = 0;
    final static int STATE_RUNNING = 1;
    int              mState;

    ProgressThread(Handler h)
    {
      mHandler = h;
    }

    public void run()
    {
      mState = STATE_RUNNING;

      try
      {
        // Create data directory
        File dataDirBase = Environment.getExternalStorageDirectory();
        dataDirBase = new File(dataDirBase, DATA_DIR_BASE);
        dataDirBase.mkdirs();

        // Copy version file first
        copyFileToSD(DATA_VERSION_FILE);

        // Copy data files
        for (int i = 0; i < DATA_DIRS.length; i++)
        {
          new File(dataDirBase, DATA_DIRS[i]).mkdir();
          String[] dataFiles = getResources().getAssets().list(DATA_DIRS[i]);
          for (int j = 0; j < dataFiles.length; j++)
          {
            copyFileToSD(DATA_DIRS[i] + "/" + dataFiles[j]);
            
            updateProgress(dataFiles.length, j+1);
          }
        }
        
        updateProgress(0, 0);
      } catch (Throwable e)
      {
        Log.e(this.getClass().getName(), "", e);
        throw new RuntimeException(e);
      }
    }
    
    private void updateProgress(int max, int progress)
    {
      Message msg = mHandler.obtainMessage();
      Bundle b = new Bundle();
      b.putInt(PROGRESS_KEY, progress);
      b.putInt(PROGRESS_MAX_KEY, max);
      msg.setData(b);
      mHandler.sendMessage(msg);
    }

    /*
     * sets the current state for the thread, used to stop the thread
     */
    public void setState(int state)
    {
      mState = state;
    }

    private void copyFileToSD(String srcFile) throws IOException
    {
      BufferedInputStream input = new BufferedInputStream(getResources().getAssets().open(srcFile), BUF_SIZE);
      String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DATA_DIR_BASE + srcFile;
      BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File(destFile)), BUF_SIZE);
      try
      {
        int b = 0;
        while ((b = input.read()) != -1)
        {
          output.write(b);
        }
      } finally
      {
        if (output != null)
          output.close();
        if (input != null)
          input.close();
      }
    }
  }

  private static final int BUF_SIZE = 8192; 

}
