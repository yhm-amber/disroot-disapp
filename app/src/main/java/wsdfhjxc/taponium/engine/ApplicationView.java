package wsdfhjxc.taponium.engine;

import android.content.*;
import android.graphics.*;
import android.view.*;

public class ApplicationView extends View {
    private final SceneKeeper sceneKeeper;
    private final UpdateHandler updateHandler;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ApplicationView(Context context, SceneKeeper sceneKeeper, UpdateHandler updateHandler) {
        super(context);
        this.sceneKeeper = sceneKeeper;
        this.updateHandler = updateHandler;
    }

    private void clearCanvas(Canvas canvas) {
        // clear the entire screen with a black color filling
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        clearCanvas(canvas);

        // synchronized as this might be automatically called by OS from non-engine thread
        synchronized(sceneKeeper) {
            // always get an updated alpha value for proper interpolation
            double alpha = updateHandler.getAlpha();
            for (Scene scene : sceneKeeper.scenes) {
                scene.handleRender(canvas, paint, alpha);
            }
        }
    }

    public void update() {
        postInvalidate();
    }
}