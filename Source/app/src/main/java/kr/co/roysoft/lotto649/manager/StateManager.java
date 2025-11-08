package kr.co.roysoft.lotto649.manager;

import kr.co.roysoft.lotto649.data.UserDTO;

public class StateManager {
    private static StateManager instance = new StateManager();

    private StateManager() {

    }

    public static StateManager getInstance() {
        return instance;
    }

    public UserDTO USER;
    public String LOTTO_DATE;
}
