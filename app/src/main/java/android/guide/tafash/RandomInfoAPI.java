package android.guide.tafash;
//    TODO 4 CREATE ENDPOINTS INTERFACE

import retrofit2.Call;
import retrofit2.http.GET;

// ref  https://uselessfacts.jsph.pl/

public interface RandomInfoAPI {
    @GET("random.json?language=en")
    Call<RandomInfo> getInfoApi();
}
