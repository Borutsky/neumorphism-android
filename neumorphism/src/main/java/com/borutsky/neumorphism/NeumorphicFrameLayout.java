package com.borutsky.neumorphism;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class NeumorphicFrameLayout extends FrameLayout {

    private Shape shape = Shape.RECTANGLE;
    private State state = State.FLAT;
    private int level;
    private Path pathBase;
    private Path pathBright;
    private Path pathDim;
    private Paint paintBase;
    private Paint paintBright;
    private Paint paintDim;
    private int bgColor;
    private int brightColor;
    private int dimColor;
    private float w;
    private float h;
    private float cornerRadius;
    private float offset = DimensionsUtil.convertDpToPixel(10f, getContext());

    public NeumorphicFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public NeumorphicFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NeumorphicFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public NeumorphicFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NeumorphicFrameLayout);
            String type = a.getString(R.styleable.NeumorphicFrameLayout_shape);
            if (type != null) {
                switch (type) {
                    case "0":
                        shape = Shape.RECTANGLE;
                        break;
                    case "1":
                        shape = Shape.CIRCLE;
                }
            }
            String stateType = a.getString(R.styleable.NeumorphicFrameLayout_state);
            if (stateType != null) {
                switch (stateType) {
                    case "0":
                        state = State.FLAT;
                        break;
                    case "1":
                        state = State.CONCAVE;
                        break;
                    case "2":
                        state = State.CONVEX;
                        break;
                    case "3":
                        state = State.PRESSED;
                        break;
                }
            }
            bgColor = a.getColor(R.styleable.NeumorphicFrameLayout_background_color, Color.WHITE);
            brightColor = manipulateColor(bgColor, 1.1f);
            dimColor = manipulateColor(bgColor, 0.9f);
            cornerRadius = a.getDimensionPixelSize(R.styleable.NeumorphicFrameLayout_corner_radius, 0);
            a.recycle();
        }
        level = calculateLevel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = getMeasuredWidth();
        h = getMeasuredHeight();
        reset();
    }

    private void reset() {
        pathBase = new Path();
        pathBright = new Path();
        pathDim = new Path();
        paintBase = new Paint(ANTI_ALIAS_FLAG);
        paintBright = new Paint(ANTI_ALIAS_FLAG);
        paintDim = new Paint(ANTI_ALIAS_FLAG);
        resetShapes();
        initPaints();
    }

    private void resetShapes() {
        pathBase.reset();
        pathBright.reset();
        pathDim.reset();
        if (shape == Shape.RECTANGLE) {
            pathBase.addRoundRect(0f, 0f, w, h, cornerRadius, cornerRadius, Path.Direction.CW);
            pathBright.addRoundRect(0f, 0f, w, h, cornerRadius, cornerRadius, Path.Direction.CW);
            pathDim.addRoundRect(0f, 0f, w, h, cornerRadius, cornerRadius, Path.Direction.CW);
        } else {
            float radius = h < w ? h / 2 : w / 2;
            pathBase.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
            pathBright.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
            pathDim.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
        }
        if (state == State.PRESSED) {
            if (!pathBright.isInverseFillType()) {
                pathBright.toggleInverseFillType();
            }
            if (!pathDim.isInverseFillType()) {
                pathDim.toggleInverseFillType();
            }
        }
        pathBase.close();
        pathBright.close();
        pathDim.close();
    }

    private void initPaints() {
        float shadowRadius = DimensionsUtil.convertDpToPixel(20, getContext());
        float amplifier = level * (shadowRadius / 10) * (state == State.PRESSED ? -1 : 1);
        if (state == State.PRESSED || state == State.FLAT) {
            paintBase.setColor(bgColor);
            paintBright.setColor(bgColor);
            paintDim.setColor(bgColor);
        } else if (state == State.CONCAVE) {
            LinearGradient linearGradient = new LinearGradient(0f, 0f, w, h, dimColor, brightColor, Shader.TileMode.CLAMP);
            paintBase.setShader(linearGradient);
            paintBright.setColor(bgColor);
            paintDim.setColor(bgColor);
        } else {
            LinearGradient linearGradient = new LinearGradient(0f, 0f, w, h, brightColor, dimColor, Shader.TileMode.CLAMP);
            paintBase.setShader(linearGradient);
            paintBright.setColor(bgColor);
            paintDim.setColor(bgColor);
        }
        shadowRadius += amplifier;
        paintBright.setShadowLayer(shadowRadius, -offset, -offset, brightColor);
        paintDim.setShadowLayer(shadowRadius, offset, offset, dimColor);
    }

    private int calculateLevel() {
        ViewParent parent = getParent();
        NeumorphicFrameLayout neoParent = null;
        while (parent != null) {
            if (parent instanceof NeumorphicFrameLayout) {
                neoParent = (NeumorphicFrameLayout) parent;
                break;
            }
            parent = parent.getParent();
        }
        if (neoParent == null) {
            return levelFromState();
        } else {
            return neoParent.getLevel() + levelFromState();
        }
    }

    private int levelFromState() {
        if (state == State.PRESSED) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (state == State.PRESSED) {
            canvas.clipPath(pathBase);
            canvas.drawPath(pathBase, paintBase);
            canvas.drawPath(pathBright, paintBright);
            canvas.drawPath(pathDim, paintDim);
        } else {
            canvas.drawPath(pathBright, paintBright);
            canvas.drawPath(pathDim, paintDim);
            canvas.drawPath(pathBase, paintBase);
        }
        super.dispatchDraw(canvas);
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        reset();
        invalidate();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        reset();
        invalidate();
    }

    public int getLevel() {
        return level;
    }

    private static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public enum Shape {
        CIRCLE, RECTANGLE
    }

    public enum State {
        FLAT, CONCAVE, CONVEX, PRESSED
    }
}
