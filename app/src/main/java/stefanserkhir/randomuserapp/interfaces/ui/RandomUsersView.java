package stefanserkhir.randomuserapp.interfaces.ui;

public interface RandomUsersView {

    void updateUI(boolean wayUpdate);

    void onErrorFetchingData();

    void toggleListAndProgressBar(boolean toggle);
}
