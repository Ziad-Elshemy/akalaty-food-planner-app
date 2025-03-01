package eg.iti.mad.akalaty.ui.login.presenter;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.auth.MyFirebaseAuth;
import eg.iti.mad.akalaty.auth.OnLoginResponse;
import eg.iti.mad.akalaty.auth.firestore.FirestoreUtils;
import eg.iti.mad.akalaty.model.AppUser;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.login.view.IViewLoginFragment;
import eg.iti.mad.akalaty.utils.Constants;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
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


    @Override
    public void addAccountToFirebase(String email, String password, OnLoginResponse onLoginResponse){
        mAuth.signInToFirebase(email,password,onLoginResponse);
    }

    @Override
    public void signInWithGoogle(Context context, CredentialManager credentialManager) {
        launchSignInCredentialManager(context,credentialManager);
    }

    public void launchSignInCredentialManager(Context context, CredentialManager credentialManager) {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(context.getString(R.string.client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                context,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignIn(result.getCredential());
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        if (e.getLocalizedMessage().contains("Cannot find a matching credential")) {
//                            loginView.navigateToSignUp();
                            _view.showOnUserLoginFailureWithGoogle("Cannot find a matching credential");
                        } else {
//                            loginView.showError("Error signing in. Please try again.");
                            _view.showOnUserLoginFailureWithGoogle("Error signing in. Please try again.");
                        }
                    }
                }
        );
    }

    private void handleSignIn(Credential credential) {
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (customCredential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                Bundle credentialData = customCredential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
                mAuth.firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken(), _view);
            } else {
//                loginView.showError("Credential is not of type Google ID!");
                _view.showOnUserLoginFailureWithGoogle("Credential is not of type Google ID!");
            }
        }
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

        CollectionReference userFavoritesRef = firestore.collection(Constants.USER_COLLECTION_NAME).document(userId).collection(Constants.FAVORITE_COLLECTION_NAME);
        CollectionReference userPlannedMealsRef = firestore.collection(Constants.USER_COLLECTION_NAME).document(userId).collection(Constants.PLANNED_COLLECTION_NAME);

        disposables.add(
                Single.zip(
                                Single.fromCallable(() -> {
                                    List<SingleMealItem> favMeals = new ArrayList<>();
                                    QuerySnapshot favSnapshot = Tasks.await(userFavoritesRef.get());
                                    for (DocumentSnapshot document : favSnapshot) {
                                        SingleMealItem meal = document.toObject(SingleMealItem.class);
                                        favMeals.add(meal);
                                    }
                                    return favMeals;
                                }).subscribeOn(Schedulers.io()),

                                Single.fromCallable(() -> {
                                    List<PlannedMeal> plannedMeals = new ArrayList<>();
                                    QuerySnapshot plannedSnapshot = Tasks.await(userPlannedMealsRef.get());
                                    for (DocumentSnapshot document : plannedSnapshot) {
                                        PlannedMeal meal = document.toObject(PlannedMeal.class);
                                        plannedMeals.add(meal);
                                    }
                                    return plannedMeals;
                                }).subscribeOn(Schedulers.io()),

                                (favMeals, plannedMeals) -> new Pair<>(favMeals, plannedMeals)
                        )
                        .subscribeOn(Schedulers.io())
                        .flatMapCompletable(data ->
                                Completable.concatArray(
                                        _repo.deleteAllFav(),
                                        _repo.deleteAllPlanned(),
                                        _repo.insertAllFav(data.first),
                                        _repo.insertAllPlanned(data.second)
                                )
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            _view.showOnDataFetchedFromFirestore();
                            Log.i(TAG, "Favorites & Planned Meals stored in Room successfully");
                        }, throwable -> {
                            Log.e(TAG, "Error fetching or storing Favorites & Planned Meals", throwable);
                        })
        );
    }

}
