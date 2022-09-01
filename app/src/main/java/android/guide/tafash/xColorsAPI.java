package android.guide.tafash;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface xColorsAPI {
//ref : https://x-colors.herokuapp.com/
    //    TODO 4 CREATE ENDPOINTS INTERFACE
    @GET("random?number=2")
    Call<List<BackgroundColors>> getColors();
}
