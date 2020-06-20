package stefanserkhir.randomuserapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Transformation;

public class AvatarTransformation implements Transformation {

    private static Paint mMaskingPaint = new Paint();
    private Context mContext;
    private int mMaskId;

    static {
        mMaskingPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    public AvatarTransformation(Context context, int maskId) {
        mContext = context;
        mMaskId = maskId;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Drawable mask = Utils.getMaskDrawable(mContext, mMaskId);

        Canvas canvas = new Canvas(result);
        mask.setBounds(0, 0, width, height);
        mask.draw(canvas);
        canvas.drawBitmap(source, 0, 0, mMaskingPaint);

        source.recycle();

        return result;
    }

    @Override
    public String key() {
        return "AvatarTransformation(maskId=" +
                mContext.getResources().getResourceEntryName(mMaskId) + ")";
    }

    private final static class Utils {

        public static Drawable getMaskDrawable(Context context, int maskId) {
            Drawable drawable;
            drawable = context.getDrawable(maskId);

            if (drawable == null) {
                throw new IllegalArgumentException("maskId is invalid");
            }

            return drawable;
        }
    }
}
