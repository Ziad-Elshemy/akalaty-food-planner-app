package eg.iti.mad.akalaty.auth;

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
import eg.iti.mad.akalaty.utils.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import android.util.Pair;


public class FirebaseDataSource {

    private final FirebaseFirestore firestore;

    public FirebaseDataSource() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public Completable uploadData(String userId, List<SingleMealItem> favMeals, List<PlannedMeal> plannedMeals) {
        CollectionReference userFavoritesRef = firestore.collection(Constants.USER_COLLECTION_NAME)
                .document(userId).collection(Constants.FAVORITE_COLLECTION_NAME);

        CollectionReference userPlannedMealsRef = firestore.collection(Constants.USER_COLLECTION_NAME)
                .document(userId).collection(Constants.PLANNED_COLLECTION_NAME);

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
    }

    public Single<Pair<List<SingleMealItem>, List<PlannedMeal>>> downloadUserData(String userId) {
        CollectionReference userFavoritesRef = firestore.collection(Constants.USER_COLLECTION_NAME)
                .document(userId).collection(Constants.FAVORITE_COLLECTION_NAME);

        CollectionReference userPlannedMealsRef = firestore.collection(Constants.USER_COLLECTION_NAME)
                .document(userId).collection(Constants.PLANNED_COLLECTION_NAME);

        return Single.zip(
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
        );
    }



}

