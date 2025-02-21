package eg.iti.mad.akalaty.auth.firestore;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import eg.iti.mad.akalaty.Constants;
import eg.iti.mad.akalaty.model.AppUser;

public class FirestoreUtils {

    public static void addUserToFirestore(AppUser user,
                                          OnSuccessListener<Void> onSuccessListener,
                                          OnFailureListener onFailureListener){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection(Constants.USER_COLLECTION_NAME);
        DocumentReference userDoc = userCollection.document(user.getId());
        userDoc.set(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);


    }


    public static void signInWithFirestore(String userId,
                                           OnSuccessListener<DocumentSnapshot> onSuccessListener,
                                           OnFailureListener onFailureListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection(Constants.USER_COLLECTION_NAME);
        DocumentReference userDoc = userCollection.document(userId);
        userDoc.get()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}
