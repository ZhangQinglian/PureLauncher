/*******************************************************************************
 *    Copyright 2017-present, PureLauncher Contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package com.zql.android.purelauncher.presentation.ui.customview.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;

import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zqlite.android.logly.Logly;

import java.util.Random;

/**
 * @author qinglian.zhang, created on 2017/4/17.
 */
public class CircleDrawable extends Drawable {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float padding ;
    private final int[] colors = {
            R.color.random_c_1,
            R.color.random_c_2,
            R.color.random_c_3,
            R.color.random_c_4,
            R.color.random_c_5,

    };

    public CircleDrawable(){
        Random random = new Random();
        int index = random.nextInt(5);
        Logly.d("index : " + index);
        paint.setColor(LauncherApplication.own().getResources().getColor(colors[index]));
        padding =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, LauncherApplication.own().getResources().getDisplayMetrics());
    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        canvas.drawCircle(w/2,h/2,w/2 - padding,paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
