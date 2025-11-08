package kr.co.roysoft.lotto649.manager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import kr.co.roysoft.lotto649.data.JsonDTO;

public class NetworkManager {
    private Context mContext;
//    public static String API_SERVER = "http://10.0.2.2:3030/lotto649"; // local
//    public static String API_SERVER = "https://roysoft.co.kr:3030/lotto649"; // dev
    public static String API_SERVER = "https://roysoft.co.kr/lotto649"; // prod
    public static final String REQUEST_GET_TEXTS = "GET /texts";
    public static final String REQUEST_POST_LOGIN = "POST /login";
    public static final String REQUEST_PUT_USERS_USER_ID = "PUT /users/:user_id";
    public static final String REQUEST_GET_USERS_USER_ID = "GET /users/:user_id";
    public static final String REQUEST_GET_LOTTO649S = "GET /lotto649es";
    public static final String REQUEST_GET_NUMBERS = "GET /numbers";
    public static final String REQUEST_GET_NUMBERS_MY = "GET /numbers my";
    public static final String REQUEST_GET_NUMBERS_AI = "GET /numbers ai";
    public static final String REQUEST_POST_NUMBERS = "POST /numbers";
    public static final String REQUEST_DELETE_NUMBERS_NUMBER_ID = "DELETE /numbers/:number_id";
    public static final String REQUEST_POST_RECOMMEND_AI_NUMBER = "POST /recommend-ai-number";

    public static final int PARAM_TYPE_NORMAL = 1;
    public static final int PARAM_TYPE_BODY = 2;

    public static final int ERROR_CODE_NO_MORE_COUNT = 1003;


    private static NetworkManager instance = new NetworkManager();

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        return instance;
    }

    public void init(Context context){
        mContext = context;
        if(DataManager.getInstance().isDev){
            API_SERVER = "http://roysoft.co.kr:3030"; // dev
        }
    }

    private String base64encode(String str){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
            }
            else{
                return android.util.Base64.encodeToString(str.getBytes(StandardCharsets.UTF_8), android.util.Base64.DEFAULT);
            }
        }
        catch (Exception e){
            return str;
        }
    }

    public synchronized void request(final NetworkListener listener, final String type, HashMap<String, String> map, int paramType) {
        listener.networkStart();
        AndroidHttpClient httpClient = null;
        httpClient = new AndroidHttpClient(API_SERVER);
        httpClient.setMaxRetries(3);

        httpClient.addHeader("Content-Type", "application/json;charset=UTF-8");

        String method = type.split(" ")[0];
        String url = "";
        for(int i = 0 ; i < type.split(" ")[1].split(":").length ; i++){
            String str = type.split(" ")[1].split(":")[i];
            if(i == 0){
                url += str;
            }
            else{
                String[] strArray = str.split("/");
                if(map != null) {
                    String value = map.get(strArray[0]);
                    if (strArray.length > 1) {
                        url += value;
                        for(int j = 1 ; j < strArray.length ; j++){
                            url += "/" + strArray[j];
                        }
                    } else {
                        url += value;
                    }
                }
            }
        }

        Log.d("test", "method: " + method);
        Log.d("test", "url: " + url);
        ParameterMap params = new ParameterMap();
        map.forEach((key, value) -> {
            String str = null;
            try {
                str = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                str = value;
            }
            Log.d("test", key + " : " + value + " -> " + str);
            params.put(key, str);
        });

        if(method.equals("POST")){
            if(paramType == PARAM_TYPE_NORMAL){
                httpClient.post(url, params, new AsyncCallback() {
                    public void onComplete(HttpResponse httpResponse) {
                        successResponse(listener, type, httpResponse);
                    }

                    public void onError(Exception e) {
                        e.printStackTrace();
                        failResponse(listener, type, e);
                    }
                });
            }
            else if(paramType == PARAM_TYPE_BODY){
                HashMap<String, Object> paramBody = new HashMap<>();
                for(String key : map.keySet()){
                    String value = map.get(key);
                    try{
                        paramBody.put(key, Integer.parseInt(value));
                    }catch (Exception e){
                        paramBody.put(key, value);
                    }
                }

                if(type.equals(REQUEST_POST_NUMBERS)){
                    // NumberDTO 객체들의 date 필드를 yyyy-MM-dd 형식의 문자열로 변환
                    java.util.ArrayList<java.util.HashMap<String, Object>> numbersForPost = new java.util.ArrayList<>();
                    for(kr.co.roysoft.lotto649.data.NumberDTO number : DataManager.getInstance().PostNumbers){
                        java.util.HashMap<String, Object> numberMap = new java.util.HashMap<>();
                        numberMap.put("id", number.id);
                        numberMap.put("user_id", number.user_id);
                        numberMap.put("n1", number.n1);
                        numberMap.put("n2", number.n2);
                        numberMap.put("n3", number.n3);
                        numberMap.put("n4", number.n4);
                        numberMap.put("n5", number.n5);
                        numberMap.put("n6", number.n6);
                        numberMap.put("input_type", number.input_type);
                        numberMap.put("created_at", number.getCreatedAtString());
                        numberMap.put("deleted_at", number.deleted_at != null ? number.getCreatedAtString() : null);
                        numberMap.put("is_used", number.is_used);
                        numberMap.put("date", number.getDateString()); // yyyy-MM-dd 형식으로 변환
                        numberMap.put("result", number.result);
                        numbersForPost.add(numberMap);
                    }
                    paramBody.put("numbers", numbersForPost);
                }

                Gson gson = new Gson();
                Log.d("test", "POST param : " + gson.toJson(paramBody));
                httpClient.post(url, "application/json", gson.toJson(paramBody).getBytes(StandardCharsets.UTF_8), new AsyncCallback() {
                    public void onComplete(HttpResponse httpResponse) {
                        successResponse(listener, type, httpResponse);
                    }

                    public void onError(Exception e) {
                        e.printStackTrace();
                        failResponse(listener, type, e);
                    }
                });
            }
        }
        else if(method.equals("PUT")){
            HashMap<String, Object> paramBody = new HashMap<>();
            for(String key : map.keySet()){
                String value = map.get(key);
                try{
                    paramBody.put(key, Integer.parseInt(value));
                }catch (Exception e){
                    paramBody.put(key, value);
                }
            }
            Gson gson = new Gson();
            httpClient.put(url, "application/json", gson.toJson(paramBody).getBytes(StandardCharsets.UTF_8), new AsyncCallback() {
                public void onComplete(HttpResponse httpResponse) {
                    successResponse(listener, type, httpResponse);
                }

                public void onError(Exception e) {
                    e.printStackTrace();
                    failResponse(listener, type, e);
                }
            });
        }
        else if(method.equals("DELETE")){
            httpClient.delete(url, params, new AsyncCallback() {
                public void onComplete(HttpResponse httpResponse) {
                    successResponse(listener, type, httpResponse);
                }

                public void onError(Exception e) {
                    e.printStackTrace();
                    failResponse(listener, type, e);
                }
            });
        }
        else if(method.equals("GET")){
            httpClient.get(url, params, new AsyncCallback() {
                public void onComplete(HttpResponse httpResponse) {
                    successResponse(listener, type, httpResponse);
                }

                public void onError(Exception e) {
                    e.printStackTrace();
                    failResponse(listener, type, e);
                }
            });
        }
    }

    private void successResponse(final NetworkListener listener, final String type, final HttpResponse httpResponse){
        Log.d("test", "code : " + httpResponse.getStatus());
        Log.d("test", "response : " + httpResponse.getBodyAsString());
        JsonDTO dto = ParseManager.getInstance().parse(httpResponse.getBodyAsString(), type);
        dto.type = type;
        dto.code = httpResponse.getStatus();
        if (dto.code == 200) {
            if(dto.error != null){
                listener.onError(dto);
            }
            else {
                listener.onComplete(dto);
            }
        } else {
            listener.onError(dto);
        }
        listener.networkEnd();
    }

    private void failResponse(final NetworkListener listener, final String type, final Exception e){
        Log.d("test", "error : " + e.getMessage());
        JsonDTO dto = new JsonDTO();
        dto.message = "네트워크 연결에 실패하였습니다. 다시 시도해주세요.";
        dto.type = type;
        listener.onError(dto);
        listener.networkEnd();
    }
}
