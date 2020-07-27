package stefanserkhir.randomuserapp.ui.helpers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.squareup.picasso.Picasso;

import stefanserkhir.randomuserapp.utils.AvatarTransformation;
import stefanserkhir.randomuserapp.R;
import stefanserkhir.randomuserapp.interfaces.ui.RepositoryItemView;

public class RandomUserHolder extends ViewHolder implements RepositoryItemView {
    private Context mContext;
    private TextView mUserNumberTextView;
    private ImageView mUserAvatarImageView;
    private TextView mUserFullNameTextView;

    public RandomUserHolder(@NonNull View itemView, Context context) {
        super(itemView);

        mUserNumberTextView = itemView.findViewById(R.id.user_number);
        mUserAvatarImageView = itemView.findViewById(R.id.user_avatar);
        mUserFullNameTextView = itemView.findViewById(R.id.user_full_name);

        mContext = context;
    }

    @Override
    public void setUserNumber(int number) {
        mUserNumberTextView.setText(mContext.getString(R.string.user_number, number + 1));
    }

    @Override
    public void setMask(String gender, String avatarURL) {
        int rDrawable = gender.equals("male") ?
                R.drawable.star_shape : R.drawable.heart_shape;
        Picasso.get()
                .load(avatarURL)
                .transform(new AvatarTransformation(mContext,
                        rDrawable))
                .placeholder(rDrawable)
                .into(mUserAvatarImageView);
    }

    @Override
    public void setUserFullName(String fullName) {
        mUserFullNameTextView.setText(fullName);
    }
}
