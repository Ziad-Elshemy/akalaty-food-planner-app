package eg.iti.mad.akalaty.ui.profile.presenter;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
        Log.i(TAG, "uploadDataToFirestore: userId " + userId);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference userFavoritesRef = firestore.collection(Constants.USER_COLLECTION_NAME)
                .document(userId).collection(Constants.FAVORITE_COLLECTION_NAME);

        CollectionReference userPlannedMealsRef = firestore.collection(Constants.USER_COLLECTION_NAME)
                .document(userId).collection(Constants.PLANNED_COLLECTION_NAME);

        disposables.add(
                Single.zip(
                                _repo.getAllStoredFavMeals().firstOrError().subscribeOn(Schedulers.io()),
                                _repo.getAllStoredPlannedMeals().firstOrError().subscribeOn(Schedulers.io()),
                                (favMeals, plannedMeals) -> new Pair<>(favMeals, plannedMeals)
                        )
                        .flatMapCompletable(data -> {
                            List<SingleMealItem> favMeals = data.first;
                            List<PlannedMeal> plannedMeals = data.second;

                            return Completable.create(emitter -> {
                                Task<Void> deleteFavsTask = userFavoritesRef.get()
                                        .continueWithTask(task -> {
                                            WriteBatch batch = firestore.batch();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                batch.delete(document.getReference());
                                            }
                                            return batch.commit();
                                        });

                                Task<Void> deletePlannedMealsTask = userPlannedMealsRef.get()
                                        .continueWithTask(task -> {
                                            WriteBatch batch = firestore.batch();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                batch.delete(document.getReference());
                                            }
                                            return batch.commit();
                                        });

                                Tasks.whenAllSuccess(deleteFavsTask, deletePlannedMealsTask)
                                        .addOnSuccessListener(aVoid -> {
                                            Completable favUpload = Observable.fromIterable(favMeals)
                                                    .flatMapCompletable(meal -> Completable.create(innerEmitter -> {
                                                        userFavoritesRef.document(meal.getIdMeal()).set(meal)
                                                                .addOnSuccessListener(a -> innerEmitter.onComplete())
                                                                .addOnFailureListener(innerEmitter::onError);
                                                    }));

                                            Completable plannedUpload = Observable.fromIterable(plannedMeals)
                                                    .flatMapCompletable(meal -> Completable.create(innerEmitter -> {
                                                        userPlannedMealsRef.document(meal.getMeal().getIdMeal()).set(meal)
                                                                .addOnSuccessListener(a -> innerEmitter.onComplete())
                                                                .addOnFailureListener(innerEmitter::onError);
                                                    }));

                                            Completable.mergeArray(favUpload, plannedUpload)
                                                    .subscribe(emitter::onComplete, emitter::onError);
                                        })
                                        .addOnFailureListener(emitter::onError);
                            });
                        })
                        .subscribe(() -> {
                            _view.showOnUploadSuccess("Favorites & Planned Meals uploaded successfully");
                            Log.i(TAG, "Favorites & Planned Meals uploaded successfully to Firestore");
                        }, throwable -> {
                            _view.showOnUploadFailure("Error uploading favorites & planned meals");
                            Log.e(TAG, "Error uploading favorites & planned meals", throwable);
                        })
        );
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
                            _view.showOnDownloadSuccess("Favorites & Planned Meals fetched and stored successfully");
                            Log.i(TAG, "Favorites & Planned Meals stored in Room successfully");
                        }, throwable -> {
                            _view.showOnDownloadFailure("Error fetching or storing Favorites & Planned Meals");
                            Log.e(TAG, "Error fetching or storing Favorites & Planned Meals", throwable);
                        })
        );
    }



    @Override
    public void logout() {
        disposables.add(
                Completable.mergeArray(
                                _repo.deleteAllFav(),
                                _repo.deleteAllPlanned()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            _view.showOnLogoutSuccess("All favorites and planned meals deleted from Room on logout");
                            Log.i(TAG, "All favorites and planned meals deleted from Room on logout");
                        }, throwable -> {
                            _view.showOnLogoutFailure("Error deleting data on logout: " + throwable.getLocalizedMessage());
                            Log.e(TAG, "Error deleting data on logout", throwable);
                        })
        );
    }


    public void clearDisposables() {
        disposables.clear();
    }
}
