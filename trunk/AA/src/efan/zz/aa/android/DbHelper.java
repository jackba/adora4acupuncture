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
package efan.zz.aa.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import efan.zz.aa.R;

public class DbHelper extends SQLiteOpenHelper
{
  private static final float VERSION = 1.20f;
  private static final int DB_VERSION = (int) (VERSION*100);
  private static final String DB_PATH = "AA.db";
  
  private static DbHelper dbHelper;
  
  private Context ctx;
  
  private DbHelper(Context ctx)
  {
    super(ctx, DB_PATH, null, DB_VERSION);
    
    this.ctx = ctx;
  }
  
  public static DbHelper getInstance(Context ctx)
  {
    if (dbHelper == null)
    {
      dbHelper = new DbHelper(ctx);
    }
    
    return dbHelper;
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    try
    {
      db.beginTransaction();
      
      // create db AA
      applySQLs(db, R.raw.db_create);

      // Initialise / load db AA
      applySQLs(db, R.raw.db_init);
      db.setTransactionSuccessful();
    }
    catch(Exception e)
    {
      Log.e(this.getClass().getName(), "", e);
      throw new RuntimeException("Database create error! Please contact the support or developer.", e);
    }
    finally
    {
      db.endTransaction();
    }
  }
  
  private void applySQLs(SQLiteDatabase db, int sqlResourceId) throws IOException
  {
    InputStream tmpIS = ctx.getResources().openRawResource(sqlResourceId);
    InputStreamReader tmpReader = new InputStreamReader(tmpIS);
    BufferedReader tmpBuf = new BufferedReader(tmpReader);

    StringBuffer sql = new StringBuffer();
    String tmpStr = null;
    while((tmpStr = tmpBuf.readLine()) != null)
    {
      sql.append(tmpStr);
      sql.append('\n');
      if (tmpStr.trim().endsWith(";"))
      {
        db.execSQL(sql.toString());
        sql = new StringBuffer();
      }
    }

    tmpBuf.close();
    tmpReader.close();
    tmpIS.close();
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    updateDb(db, oldVersion, newVersion);
  }
  
  private void updateDb(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    try
    {
      db.beginTransaction();
      
      /* since V1.2.0, no more Edit, so no need upgrade 
      switch(oldVersion)
      {
      case 1:   // 1.0
        applySQLs(db, R.raw.db_update_1_1_0);
      case 11:  // 0.11*100
        applySQLs(db, R.raw.db_update_1_1_2);
      case 112: // 1.12*100
        // TODO
      }
      */
      applySQLs(db, R.raw.db_clean_1_2_0);
      onCreate(db);
      
      db.setTransactionSuccessful();
    }
    catch(Exception e)
    {
      Log.e(this.getClass().getName(), "", e);
      throw new RuntimeException("Database update error! Please contact the support or developer.", e);
    }
    finally
    {
      db.endTransaction();
    }
  }

}
