package com.xiaojia.daniujia.ui.views.imageview.RoundRectangImageView;

import android.content.Context;
import android.util.AttributeSet;

import com.xiaojia.daniujia.ui.views.imageview.BubberImageView.ShaderHelper;
import com.xiaojia.daniujia.ui.views.imageview.BubberImageView.ShaderImageView;

public class RoundRectangImageView extends ShaderImageView {
    private RoundedShader shader;
    public RoundRectangImageView(Context context) {
        super(context);
    }

    public RoundRectangImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundRectangImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new RoundedShader();
        return shader;
    }

    public final int getRadius() {
        if(shader != null) {
            return shader.getRadius();
        }
        return 0;
    }

    public final void setRadius(final int radius) {
        if(shader != null) {
            shader.setRadius(radius);
            invalidate();
        }
    }

}
