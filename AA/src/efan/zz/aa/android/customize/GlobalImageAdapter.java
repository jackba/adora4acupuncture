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

import efan.zz.aa.AA;
import efan.zz.aa.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GlobalImageAdapter extends BaseAdapter
{
  private Context   mContext;

  private Integer[] mImageIds = 
  { 
      R.drawable.global1_woman_front_icon,
      R.drawable.global2_woman_back_icon, 
      R.drawable.global3_woman_side_icon,
      R.drawable.global4_man_front_icon,
      R.drawable.global5_man_back_icon,
      R.drawable.global6_man_side_icon
  };

//  private ImageView[] imageViews = new ImageView[mImageIds.length];
  
  public GlobalImageAdapter(Context c)
  {
    mContext = c;
  }

  public int getCount()
  {
    return mImageIds.length;
  }

  public Object getItem(int position)
  {
    return position;
  }

  public long getItemId(int position)
  {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent)
  {
//    if (imageViews[position] == null)
//    {
//      ImageView i = new ImageView(AA.ctx);
//      i.setImageResource(mImageIds[position]);
//      imageViews[position] = i;
//    }
//    return imageViews[position];
    ImageView i = new ImageView(mContext);
    i.setImageResource(mImageIds[position]);
    return i;
  }
  
}
