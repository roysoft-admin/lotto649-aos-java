package kr.co.roysoft.lotto649.manager;

import android.util.Log;

import com.google.gson.Gson;

import kr.co.roysoft.lotto649.data.JsonDTO;

public class ParseManager {
    private static ParseManager instance = new ParseManager();
    private Gson gson;
    private DataManager dataManager;
    private StateManager stateManager;

    private ParseManager() {
        dataManager = DataManager.getInstance();
        stateManager = StateManager.getInstance();
        gson = new Gson();
    }

    public static ParseManager getInstance() {
        return instance;
    }

    public JsonDTO parse(String json, String type){
        Log.d("test", "json:" + json);
        Log.d("test", "type:" + type);
        try{
            JsonDTO dto = gson.fromJson(json, JsonDTO.class);
            if(dto != null){
                settingToDataManager(dto, type);
                return dto;
            }
            else{
                JsonDTO jsonDto = new JsonDTO();
                jsonDto.message = "데이터를 가져오는데 실패하였습니다. 다시 시도해주세요.(1)";
                return jsonDto;
            }
        } catch (Exception e){
            e.printStackTrace();
            JsonDTO jsonDto = new JsonDTO();
            jsonDto.message = "데이터를 가져오는데 실패하였습니다. 다시 시도해주세요.(2)";
            return jsonDto;
        }
    }

    private void settingToDataManager(JsonDTO dto, String type){
        if (dto != null && dto.message == null) {
            if(type.equals(NetworkManager.REQUEST_POST_LOGIN)
                    || type.equals(NetworkManager.REQUEST_PUT_USERS_USER_ID)
                    || type.equals(NetworkManager.REQUEST_GET_USERS_USER_ID)){
                dataManager.User = dto.user;
                stateManager.USER = dto.user;
            }
            else if(type.equals(NetworkManager.REQUEST_GET_TEXTS)){
                dataManager.Texts = dto.texts;
            }
            else if(type.equals(NetworkManager.REQUEST_GET_LOTTO649S)){
                Log.d("test", "dto.lotto649s:" + dto.lotto649es);
                dataManager.Lottos = dto.lotto649es;
            }
            else if(type.equals(NetworkManager.REQUEST_GET_NUMBERS)
                    || type.equals(NetworkManager.REQUEST_GET_NUMBERS_MY)
                    || type.equals(NetworkManager.REQUEST_GET_NUMBERS_AI)){
                dataManager.Numbers = dto.numbers;
            }
            else if(type.equals(NetworkManager.REQUEST_POST_RECOMMEND_AI_NUMBER)){
                dataManager.User = dto.user;
                stateManager.USER = dto.user;
                dataManager.number = dto.number;
            }
            else if(type.equals(NetworkManager.REQUEST_DELETE_NUMBERS_NUMBER_ID)){
                dataManager.id = dto.id;
            }
        }
    }
}
