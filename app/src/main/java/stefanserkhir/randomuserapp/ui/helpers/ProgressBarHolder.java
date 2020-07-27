package stefanserkhir.randomuserapp.ui.helpers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import stefanserkhir.randomuserapp.R;

public class ProgressBarHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private ProgressBar mProgressBar;

    public ProgressBarHolder(@NonNull View itemView, Context context) {
        super(itemView);

        mProgressBar = itemView.findViewById(R.id.progress_bar);

        mContext = context;
    }

    public void bindProgressBarItem() {
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.MULTIPLY);
        mProgressBar.setIndeterminate(true);
    }
}