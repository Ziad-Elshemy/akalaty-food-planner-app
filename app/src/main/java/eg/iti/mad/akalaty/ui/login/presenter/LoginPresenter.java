package eg.iti.mad.akalaty.ui.login.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import eg.iti.mad.akalaty.auth.MyFirebaseAuth;
import eg.iti.mad.akalaty.auth.OnLoginResponse;
import eg.iti.mad.akalaty.auth.firestore.FirestoreUtils;
import eg.iti.mad.akalaty.model.AppUser;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.MainActivity;
import eg.iti.mad.akalaty.ui.login.view.IViewLoginFragment;
import eg.iti.mad.akalaty.ui.meal_details.view.IViewMealDetailsFragment;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter implements ILoginPresenter {

    private static final String TAG = "LoginPresenter";
    IViewLoginFragment _view;
    MealsRepo _repo;
    MyFirebaseAuth mAuth;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LoginPresenter(IViewLoginFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
        mAuth = new MyFirebaseAuth();
    }


    public void addAccountToFirebase(String email, String password, OnLoginResponse onLoginResponse){
        mAuth.signInToFirebase(email,password,onLoginResponse);
    }

    @Override
    public void checkUserFromFirestore(String userId) {
        FirestoreUtils.signInWithFirestore(userId,
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        AppUser user = documentSnapshot.toObject(AppUser.class);
                        if (user == null){
                            //Toast.makeText(requireContext(), "User not in Firestore", Toast.LENGTH_SHORT).show();
                            _view.showOnUserLoginFailure("Can't find user in firestore");
                        }else {
                            downloadDataFromFirestore(user.getId());
                            _view.showOnUserLoginSuccess(user);
                        }
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getLocalizedMessage());
                        _view.showOnUserLoginFailure("Login Failed!");
                    }
                });
    }

    @Override
    public void downloadDataFromFirestore(String userId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference userFavoritesRef = firestore.collection("users").document(userId).collection("favorites");

        disposables.add(
                Single.fromCallable(() -> {
                            List<SingleMealItem> mealList = new ArrayList<>();
                            QuerySnapshot querySnapshot = Tasks.await(userFavoritesRef.get());

                            for (DocumentSnapshot document : querySnapshot) {
                                SingleMealItem meal = document.toObject(SingleMealItem.class);
                                mealList.add(meal);
                            }
                            return mealList;
                        })
                        .subscribeOn(Schedulers.io())
                        .flatMapCompletable(mealList ->
                                _repo.deleteAll()
                                        .andThen(_repo.insertAllFav(mealList)))
                        .subscribe(() -> {
                            _view.showOnDataFetchedFromFirestore();
                            Log.i(TAG, "Favorites stored in Room successfully");
                        }, throwable -> {
//                            _view.showOnDownloadFailure("Error fetching or storing favorites");
                            Log.e(TAG, "Error fetching or storing favorites", throwable);
                        })
        );
    }

}
