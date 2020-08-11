package stefanserkhir.randomuserapp.interfaces.presenter;

import stefanserkhir.randomuserapp.interfaces.views.RandomUsersView;
import stefanserkhir.randomuserapp.interfaces.views.RepositoryItemView;

public interface RandomUsersPresenter {

    void onAttachView(RandomUsersView view);

    void setLoading(boolean isLoading);

    boolean isLoading();

    void onUpdatingList(boolean refreshOrUploading);

    void onBindRepositoryItemViewAtPosition(int position, RepositoryItemView itemView);

    int getRepositoryItemsCount();

    void onDetachView();
}
