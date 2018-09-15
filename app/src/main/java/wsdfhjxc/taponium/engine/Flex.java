package wsdfhjxc.taponium.engine;

import android.graphics.*;

public class Flex {
    private final FlexConfig flexConfig;
    private final PointF position;
    private final PointF size;
    private final Point positionOffset;
    private final boolean absolutePosition;
    private final boolean absoluteSize;

    private final Rect realRect;
    private final Point realPosition;
    private final Point realSize;

    public Flex(PointF position, boolean absolutePosition, PointF size, boolean absoluteSize,
                Point positionOffset, FlexConfig flexConfig) {
        this.flexConfig = flexConfig;
        this.position = position;
        this.size = size;
        this.positionOffset = positionOffset;
        this.absolutePosition = absolutePosition;
        this.absoluteSize = absoluteSize;

        realRect = new Rect();
        realPosition = new Point();
        realSize = new Point();

        recalculate();
    }

    private void recalculate() {
        if (absolutePosition) {
            realRect.left = (int) (position.x * flexConfig.getScale());
            realRect.top = (int) (position.y * flexConfig.getScale());
        } else {
            realRect.left = (int) (position.x * flexConfig.getDisplayWidth());
            realRect.top = (int) (position.y * flexConfig.getDisplayHeight());
        }

        realRect.left += positionOffset.x * flexConfig.getScale();
        realRect.top += positionOffset.y * flexConfig.getScale();

        realPosition.x = realRect.left;
        realPosition.y = realRect.top;

        if (absoluteSize) {
            realRect.right = (int) (size.x * flexConfig.getScale());
            realRect.bottom = (int) (size.y * flexConfig.getScale());
        } else {
            realRect.right = (int) (size.x * flexConfig.getDisplayWidth());
            realRect.bottom = (int) (size.y * flexConfig.getDisplayHeight());
        }

        realSize.x = realRect.right;
        realSize.y = realRect.bottom;

        realRect.right += realRect.left;
        realRect.bottom += realRect.top;
    }

    public Point getPosition() {
        return realPosition;
    }

    public Point getSize() {
        return realSize;
    }

    public Rect getRect() {
        return realRect;
    }
}