package akiniyalocts.imgurapiexample.activities;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.util.Random;

import akiniyalocts.imgurapiexample.imgurmodel.ImageResponse;
import akiniyalocts.imgurapiexample.imgurmodel.Upload;
import akiniyalocts.imgurapiexample.services.UploadService;
import akiniyalocts.imgurapiexample.utils.ResCallback;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SantaHelper {

    Activity act;

    public SantaHelper(Activity act) {
        this.act = act;

    }

    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent


    public void pic(Uri file) {

        Cursor cursor = null;
        String filePath = file.getPath();

        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);
        uploadImage();
    }

    public void uploadImage() {
    /*
      Create the @Upload object
     */
        if (chosenFile == null) return;
        createUpload(chosenFile);

    /*
      Start upload
     */
        new UploadService(act).Execute(upload, new UiCallback());
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = "asdfdas" + new Random().nextInt();
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            ((ResCallback) act).resCall(imageResponse.data.link);
        }

        @Override
        public void failure(RetrofitError error) {
            ((ResCallback) act).resCall("");
        }
    }
}
