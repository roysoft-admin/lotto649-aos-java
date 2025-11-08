package kr.co.roysoft.lotto649.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.roysoft.lotto649.data.Lotto649DTO;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.data.TextDTO;
import kr.co.roysoft.lotto649.data.UserDTO;

public class DataManager {
    private static DataManager instance = new DataManager();

    private DataManager() {

    }

    public static DataManager getInstance() {
        return instance;
    }

    public boolean isDev = false;
    public ArrayList<TextDTO> Texts = new ArrayList<>();
    public UserDTO User;
    public ArrayList<Lotto649DTO> Lottos = new ArrayList<>();
    public ArrayList<NumberDTO> Numbers = new ArrayList<>();
    public NumberDTO number;
    public ArrayList<NumberDTO> PostNumbers = new ArrayList<>();
    public int id;

    public Date changeFormattedStrToDate(String str){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }
    public Date changeDateStrToDate(String str){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    public Date changeTimestampToDate(String str){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = null;
        try {
            date = format.parse(str);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    public String changeFormattedStrToDateStr(String str){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format2.format(format.parse(str));
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                return str.split(" ")[0];
            }
            catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return str;
    }

    public String changeFormattedStrToTimeStr(String str){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format2 = new SimpleDateFormat("HH:mm");
        try {
            return format2.format(format.parse(str));
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                return str.split(" ")[1];
            }
            catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return str;
    }

    public String changeDateToFormattedStr(Date date){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = null;
        try {
            str = format.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    public String changeDateToDateStr(Date date){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = null;
        try {
            str = format.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    public String changeFormattedStrToAfterStr(String str){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(str);
            // TODO after time
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
            return str;
        }
    }
}
