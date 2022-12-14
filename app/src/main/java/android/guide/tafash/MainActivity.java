package android.guide.tafash;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// ads
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {
    //    TODO 5 : WRITE DOWN THE URL'S FOR BOTH INFO AND BACKGROUND COLORS
    public static final String BASE_INFO_URL = "https://uselessfacts.jsph.pl/";
    public static final String BASE_COLOR_URL = "https://x-colors.herokuapp.com/api/";
    TextView textView;
    Button button;
    RelativeLayout relativeLayout;
    Retrofit retrofitInfo, retrofitColor;
    xColorsAPI colorsAPI;
    RandomInfoAPI randomInfoAPI;
    int[] gradiantColors;
    GradientDrawable gd;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.BtnView);
        relativeLayout = findViewById(R.id.relativeLayout);

        //      TODO 6 : Creating the Retrofit instance
        retrofitInfo = new Retrofit.Builder()
                .baseUrl(BASE_INFO_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitColor = new Retrofit.Builder()
                .baseUrl(BASE_COLOR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRandomInfo();
                getBackground();


                gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,gradiantColors);
                gd.setCornerRadius(0f);
                relativeLayout.setBackgroundDrawable(gd);

            }
        });

//        ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



    public void getRandomInfo(){

//        TODO 7 : Accessing the API
        randomInfoAPI = retrofitInfo.create(RandomInfoAPI.class);
//        TODO 8 : CALL THE METHODS OF INTERFACE

        Call<RandomInfo> call = randomInfoAPI.getInfoApi();

//        TODO 9 : RUN API asynchronously
//        enqueue mean running the call in background thread rather than the main thread
//        , so out main activity won't freeze and crash. this is all done by retrofit enqueue method.
        call.enqueue(new Callback<RandomInfo>() {
            @Override
            public void onResponse(Call<RandomInfo> call, Response<RandomInfo> response) {
//                check internet response code.
                if (!response.isSuccessful()) {
                    textView.setText("code: " + response.code());
                    return;
                }
//                get the response as a json
                RandomInfo InfoList = response.body();
                textView.setText(InfoList.getInfo());
            }

            @Override
            public void onFailure(Call<RandomInfo> call, Throwable t) {
                textView.setText("fail : " + t.getMessage());
            }
        });
    }


    public void getBackground() {
        colorsAPI = retrofitColor.create(xColorsAPI.class);
        Call<List<BackgroundColors>> call = colorsAPI.getColors();


//        enqueue mean running the call in background thread rather than the main thread
//        , so out main activity won't freeze and crash. this is all done by retrofit enqueue method.
        call.enqueue(new Callback<List<BackgroundColors>>() {
            @Override
            public void onResponse(Call<List<BackgroundColors>> call, Response<List<BackgroundColors>> response) {
                if (!response.isSuccessful()) {
                    textView.setText("code: " + response.code());
                    return;
                }
                gradiantColors = new int[2];
                List<BackgroundColors> colorsList = response.body();

                gradiantColors[0] = Color.parseColor(colorsList.get(0).getColorCode());
                gradiantColors[1] = Color.parseColor(colorsList.get(1).getColorCode());
            }

            @Override
            public void onFailure(Call<List<BackgroundColors>> call, Throwable t) {
                textView.setText("fail : " + t.getMessage());
            }
        });
    }


}