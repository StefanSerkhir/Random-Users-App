package stefanserkhir.randomuserapp.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.squareup.picasso.Picasso;

import stefanserkhir.randomuserapp.utils.AvatarTransformation;
import stefanserkhir.randomuserapp.R;
import stefanserkhir.randomuserapp.interfaces.views.RepositoryItemView;

public class RandomUserHolder extends ViewHolder implements RepositoryItemView {
    private Activity mActivity;
    private TextView mUserNumberTextView;
    private ImageView mUserAvatarImageView;
    private TextView mUserFullNameTextView;

    public RandomUserHolder(@NonNull View itemView, Activity activity) {
        super(itemView);

        mUserNumberTextView = itemView.findViewById(R.id.user_number);
        mUserAvatarImageView = itemView.findViewById(R.id.user_avatar);
        mUserFullNameTextView = itemView.findViewById(R.id.user_full_name);

        mActivity = activity;
    }

    @Override
    public void setNumber(int number) {
        mUserNumberTextView.setText(mActivity.getString(R.string.user_number, number + 1));
    }

    @Override
    public void setMask(String gender, String avatarURL) {
        int rDrawable = gender.equals("male") ?
                R.drawable.star_shape : R.drawable.heart_shape;
        Picasso.get()
                .load(avatarURL)
                .transform(new AvatarTransformation(mActivity,
                        rDrawable))
                .placeholder(rDrawable)
                .into(mUserAvatarImageView);
    }

    @Override
    public void setName(String name) {
        mUserFullNameTextView.setText(name);
    }
}
