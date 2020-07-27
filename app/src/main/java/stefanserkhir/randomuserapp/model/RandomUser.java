package stefanserkhir.randomuserapp.model;

public class RandomUser {
    private String mGender;
    private String mTitle;
    private String mFirstName;
    private String mLastName;
    private String mAvatarURL;

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getAvatarURL() {
        return mAvatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        mAvatarURL = avatarURL;
    }

    public String getFullName() {
        return mTitle + ", " + mFirstName + " " + mLastName;
    }
}
