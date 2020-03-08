package dev.berlitz.carname.integration.response;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.algorithmia.Algorithmia;
import com.algorithmia.AlgorithmiaClient;
import com.algorithmia.algo.AlgoResponse;
import com.algorithmia.algo.Algorithm;
import com.google.gson.Gson;

import java.util.Objects;

import dev.berlitz.carname.AsyncTaskHandler;

public class CarMakeAndModel extends AsyncTask<String, Void, CarResponse[]> {

    private AsyncTaskHandler<Void, CarResponse[]> asyncTaskHandler;

    public CarMakeAndModel(AsyncTaskHandler asyncTaskHandler) {
        this.asyncTaskHandler = asyncTaskHandler;
    }

    @Override
    protected CarResponse[] doInBackground(String... strings) {
        if (Objects.isNull(strings)) return null;

        String input = strings[0];
        AlgorithmiaClient client = Algorithmia.client("");
        Algorithm algo = client.algo("LgoBE/CarMakeandModelRecognition/0.4.7");
        algo.setTimeout(300L, java.util.concurrent.TimeUnit.SECONDS); //optional
        try {
            AlgoResponse result = algo.pipe(input);
            if (result.isSuccess()) {
                return new Gson().fromJson(result.asJsonString(), CarResponse[].class);
            } else {
                throw new Exception(result.asJsonString());
            }
        } catch (Exception e) {
            final Exception exception = e;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    asyncTaskHandler.handleError(exception);
                }
            });
            this.cancel(true);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        asyncTaskHandler.handlePreExecute();
    }

    @Override
    protected void onPostExecute(CarResponse[] carResponse) {
        super.onPostExecute(carResponse);
        asyncTaskHandler.handlePostExecute(carResponse);
    }
}
