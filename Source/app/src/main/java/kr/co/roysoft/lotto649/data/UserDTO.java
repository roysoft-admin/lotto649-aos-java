package kr.co.roysoft.lotto649.data;

import java.io.Serializable;

public class UserDTO implements Serializable {
    public int id;
    public String name;
    public String nickname;
    public String created_at;
    public String uid;
    public String google_token;
    public String apple_token;
    public String kakao_token;
    public String naver_token;
    public int drawing_count;
    public int is_push_win_number;
    public int font_size;
}
