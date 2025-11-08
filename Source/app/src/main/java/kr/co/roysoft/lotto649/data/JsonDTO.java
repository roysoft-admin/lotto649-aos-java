package kr.co.roysoft.lotto649.data;

import java.util.ArrayList;

public class JsonDTO {
    public String message;
    public String type;
    public int code;

    // DTOs
    public TextDTO text;
    public ArrayList<TextDTO> texts;
    public UserDTO user;
    public ArrayList<NumberDTO> numbers;
    public NumberDTO number;
    public ArrayList<Lotto649DTO> lotto649es;
    public Lotto649DTO lotto649;
    public ErrorDto error;

    public int id;

}
