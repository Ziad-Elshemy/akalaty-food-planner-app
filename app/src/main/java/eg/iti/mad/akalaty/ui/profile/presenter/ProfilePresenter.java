package eg.iti.mad.akalaty.ui.profile.presenter;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import eg.iti.mad.akalaty.auth.firestore.FirestoreUtils;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.profile.view.IViewProfileFragment;
import eg.iti.mad.akalaty.ui.search.view.IViewSearchFragment;
import eg.iti.mad.akalaty.utils.SharedPref;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        Log.i(TAG, "uploadDataToFirestore: userId " + userId);
        disposables.add(
                _repo.getAllStoredFavMeals()
                        .subscribeOn(Schedulers.io())
                        .take(1)
                        .flatMapCompletable(mealList -> {
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            CollectionReference userFavoritesRef = firestore.collection("users").document(userId).collection("favorites");
                            Log.i(TAG, "uploadDataToFirestore: "+mealList);
                            return Completable.create(emitter -> {
                                userFavoritesRef.get().addOnSuccessListener(querySnapshot -> {
                                    WriteBatch batch = firestore.batch();
                                    for (QueryDocumentSnapshot document : querySnapshot) {
                                        batch.delete(document.getReference());
                                    }
                                    Log.i(TAG, "uploadDataToFirestore: "+batch);
                                    batch.commit().addOnSuccessListener(aVoid -> emitter.onComplete())
                                            .addOnFailureListener(emitter::onError);
                                }).addOnFailureListener(emitter::onError);
                            }).andThen(
                                    Observable.fromIterable(mealList)
                                            .flatMapCompletable(meal -> Completable.create(emitter -> {
                                                Log.i(TAG, "uploadDataToFirestore: "+emitter);
                                                userFavoritesRef.document(meal.getIdMeal()).set(meal)
                                                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                                                        .addOnFailureListener(emitter::onError);
                                            }))
                            );
                        })
                        .subscribe(() -> {
                            _view.showOnUploadSuccess("Favorites uploaded successfully");
                            Log.i(TAG, "Favorites uploaded successfully to Firestore");
                        }, throwable -> {
                            _view.showOnUploadFailure("Error uploading favorites");
                            Log.i(TAG, "Error uploading favorites", throwable);
                        })
        );
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
                            _view.showOnDownloadSuccess("Favorites fetched and stored successfully");
                            Log.i(TAG, "Favorites stored in Room successfully");
                        }, throwable -> {
                            _view.showOnDownloadFailure("Error fetching or storing favorites");
                            Log.e(TAG, "Error fetching or storing favorites", throwable);
                        })
        );
    }

    @Override
    public void logout() {
        disposables.add(
                _repo.deleteAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            _view.showOnLogoutSuccess("All favorites deleted from Room on logout");
                            Log.i(TAG, "All favorites deleted from Room on logout");
                        }, throwable -> {
                            _view.showOnLogoutFailure("Error deleting favorites on logout"+throwable.getLocalizedMessage());
                            Log.e(TAG, "Error deleting favorites on logout", throwable);
                        })
        );
    }

    public void clearDisposables() {
        disposables.clear();
    }
}
