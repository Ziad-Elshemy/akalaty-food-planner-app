package eg.iti.mad.akalaty.ui.profile.presenter;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.profile.view.IViewProfileFragment;
import eg.iti.mad.akalaty.utils.Constants;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import android.util.Pair;


public class ProfilePresenter implements IProfilePresenter {


    private final CompositeDisposable disposables = new CompositeDisposable();

    IViewProfileFragment _view;
    MealsRepo _repo;
    Context _Context;

    private static final String TAG = "ProfilePresenter";

    public ProfilePresenter(IViewProfileFragment _view, MealsRepo _repo,Context _context) {
        this._view = _view;
        this._repo = _repo;
        this._Context = _context;

    }


    @Override
    public void uploadDataToFirestore(String userId) {
        disposables.add(
                _repo.uploadDataToFirestore(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            _view.showOnUploadSuccess("Your data uploaded successfully");
                            Log.i(TAG, "Favorites & Planned Meals uploaded successfully to Firestore");
                        }, throwable -> {
                            _view.showOnUploadFailure("Error uploading your data");
                            Log.e(TAG, "Error uploading favorites & planned meals", throwable);
                        })
        );
    }


    @Override
    public void downloadDataFromFirestore(String userId) {
        disposables.add(
                _repo.downloadDataFromFirestore(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            _view.showOnDownloadSuccess("Your Data fetched successfully");
                            Log.i(TAG, "Favorites & Planned Meals stored in Room successfully");
                        }, throwable -> {
                            _view.showOnDownloadFailure("Error fetching your data");
                            Log.e(TAG, "Error fetching or storing Favorites & Planned Meals", throwable);
                        })
        );
    }


    @Override
    public void logout(Context context) {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN);

        disposables.add(
                Completable.mergeArray(
                                _repo.deleteAllFav(),
                                _repo.deleteAllPlanned()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                                    FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("google.com")) {
                                googleSignInClient.signOut().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.i(TAG, "Google Sign Out Successful");
                                    } else {
                                        Log.e(TAG, "Google Sign Out Failed", task.getException());
                                    }
                                });
                            } else {
                                Log.i(TAG, "User was not signed in with Google");
                            }

                            FirebaseAuth.getInstance().signOut();

                            _view.showOnLogoutSuccess("Logged out successfully, all data cleared");
                            Log.i(TAG, "Logged out successfully, all data cleared");
                        }, throwable -> {
                            _view.showOnLogoutFailure("Error during logout: " + throwable.getLocalizedMessage());
                            Log.e(TAG, "Error during logout", throwable);
                        })
        );
    }





    public void clearDisposables() {
        disposables.clear();
    }
}
