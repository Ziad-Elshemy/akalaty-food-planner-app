//package eg.iti.mad.akalaty.database;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Base64;
//
//import androidx.room.TypeConverter;
//
//import java.nio.ByteBuffer;
//
//public class Converters {
//    @TypeConverter
//    public String bitmapToBase64(Bitmap bitmap) {
//        // create a ByteBuffer and allocate size equal to bytes in the bitmap
//        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getRowBytes());
//        // copy all the pixels from bitmap to byteBuffer
//        bitmap.copyPixelsToBuffer(byteBuffer);
//        // convert byte buffer into byteArray
//        byte[] byteArray = byteBuffer.array();
//        // convert byteArray to Base64 String with default flags
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
//
//    @TypeConverter
//    public Bitmap base64ToBitmap(String base64String) {
//        // convert Base64 String into byteArray
//        byte[] byteArray = Base64.decode(base64String, Base64.DEFAULT);
//        // byteArray to Bitmap
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//    }
//}
//
//
