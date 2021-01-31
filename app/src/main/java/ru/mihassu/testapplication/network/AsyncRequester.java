package ru.mihassu.testapplication.network;

import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncRequester {
    private OnResponseComplete listener;


    public AsyncRequester(OnResponseComplete listener) {
        this.listener = listener;
    }

    // Интерфейс обратного вызова. Метод onCompleted вызывается по окончании загрузки страницы
    public interface OnResponseComplete {
        void onComplete(String content);
    }


    //Запрос страницы
    public void run(String url) {
        Requester requester = new Requester(listener);
        requester.execute(url);
    }

    //Новый поток с использованием AsyncTask
    static class Requester extends AsyncTask<String, Void, String> {

        OnResponseComplete listener;


        Requester(OnResponseComplete listener) {
            this.listener = listener;
        }

        // Выполняется по завершении работы в потоке UI
        @Override
        protected void onPostExecute(String s) {
            listener.onComplete(s);
        }

        // Выполняется в фоновом потоке
        @Override
        protected String doInBackground(String... strings) {
            return getContent(strings[0]);
        }

        // Получение данных при отправке запроса
        private String getContent(String url) {

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder(); //Строитель
            builder.url(url); //Указываем адрес сервера
            Request request = builder.build(); //Построить запрос

            //Выполнить запрос
            try (Response response = client.newCall(request).execute()) {

                //Ответ сервера
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

}
