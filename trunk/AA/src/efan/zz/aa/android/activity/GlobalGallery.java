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

import efan.zz.aa.android.AA;
import efan.zz.aa.android.R;
import efan.zz.aa.android.customize.GlobalImageAdapter;
import efan.zz.aa.android.util.AAUtil;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GlobalGallery extends Activity
{
  private Integer[] mImageIds = 
  { 
      R.drawable.global1_woman_front,
      R.drawable.global2_woman_back, 
      R.drawable.global3_woman_side,
      R.drawable.global4_man_front,
      R.drawable.global5_man_back,
      R.drawable.global6_man_side
  };
  
  private Bitmap[] drawableImg = new Bitmap[mImageIds.length];
  
  Gallery g;
  TextView tipText;
  ImageView img0, img;
  
  private int selectedPos = -1;
  
  private int centerX = 0, centerY = 0;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.global_gallery);

    g = (Gallery) findViewById(R.id.global_gallery);
    GlobalImageAdapter globalImgAdapter = new GlobalImageAdapter(AA.ctx);
    g.setAdapter(globalImgAdapter);
    
    tipText = (TextView) findViewById(R.id.global_image_tip);
    tipText.setText(R.string.global_image_tip0);
    tipText.setTextColor(Color.GREEN);

    img0 = (ImageView) findViewById(R.id.global_image0);
    img = (ImageView) findViewById(R.id.global_image);
    img.setLongClickable(false);
    img0.setDrawingCacheEnabled(false);
    img.setDrawingCacheEnabled(false);
    
    g.setOnItemClickListener(new OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id)
      {
        if (selectedPos != -1)
        {
          drawableImg[selectedPos].recycle();
          drawableImg[selectedPos] = null;
        }
        
        drawableImg[position] = BitmapFactory.decodeResource(getResources(), mImageIds[position]);
        img0.setImageBitmap(drawableImg[position]);
        img.setImageBitmap(drawableImg[position]);
        img.setLongClickable(false);
        tipText.setText(R.string.global_image_tip0);
        tipText.setTextColor(Color.GREEN);
        selectedPos = position;
      }
    });

    img0.setOnTouchListener(new OnTouchListener()
    {
      public boolean onTouch(View view, MotionEvent motion)
      {
        if (centerX == 0)
        {
          centerX = img0.getWidth()/2;
          centerY = img0.getHeight()/2;
        }

        int h = img.getHeight();
        int w = img.getWidth();
        int h0 = img0.getHeight();
        int w0 = w * h0/h;
        
        int x = (int) (motion.getX() - (view.getWidth()-w0)/2) * w/w0 - centerX;
        int y = (int) motion.getY() * h/h0 - centerY;
        x = (x > 0)? x : 0;
        x = (x < w)? x : w;
        y = (y > 0)? y : 0;
        y = (y < h)? y : h;

        tipText.setText(R.string.global_image_tip);
        tipText.setTextColor(Color.RED);
        img0.setImageBitmap(null);
        img.scrollTo(x, y);
        
        return false;
      }
    });
  }
  
  @Override
  public void onResume()
  {
    super.onResume();
    
    if (selectedPos == -1)
      selectedPos = 0;
    
    drawableImg[selectedPos] = BitmapFactory.decodeResource(getResources(), mImageIds[selectedPos]);
    img0.setImageBitmap(drawableImg[selectedPos]);
    img.setImageBitmap(drawableImg[selectedPos]);
  }
  
  @Override
  public void onPause()
  {
    super.onPause();
    
    if (selectedPos != -1)
    {
      drawableImg[selectedPos].recycle();
      drawableImg[selectedPos] = null;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.global_picture, menu);

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
      
//    case R.id.menu_item_global_web:
//      AAUtil.showActivity(null, GlobalGallery.this, AcuMapper.class);
//      break;
      
    case R.id.menu_item_relax:
      AAUtil.showActivity(null, GlobalGallery.this, RestGallery.class);
      break;
    
    default:
      AAUtil.youngGirlWarning(null);
      break;
    }

    return true;
  }
}
