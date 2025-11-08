package kr.co.roysoft.lotto649.data;

import java.io.Serializable;
import java.util.Date;

public class NumberDTO implements Serializable {
    public int id;
    public int user_id;
    public String user_nickname;
    public int n1;
    public int n2;
    public int n3;
    public int n4;
    public int n5;
    public int n6;
    public int input_type;
    public Date created_at;
    public Date deleted_at;
    public int is_used;
    public String date;
    public int result;

    public String getDateString(){
        if(date == null || date.equals("")){
            return "";
        }
        else{
            return date;
        }
    }

    public String getCreatedAtString(){
        if(created_at == null){
            return "";
        }
        else{
            try{
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(created_at);
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }
    }

    public int getYear(){
        if(date == null || date.equals("")){
            return 0;
        }
        else{
            try {
                String[] parts = date.split("-");
                if(parts.length > 0){
                    return Integer.parseInt(parts[0]);
                }
                return 0;
            } catch (Exception e){
                return 0;
            }
        }
    }

    public int getMonth(){
        if(date == null || date.equals("")){
            return 0;
        }
        else{
            try {
                String[] parts = date.split("-");
                if(parts.length > 1){
                    return Integer.parseInt(parts[1]);
                }
                return 0;
            } catch (Exception e){
                return 0;
            }
        }
    }

    public int getDay(){
        if(date == null || date.equals("")){
            return 0;
        }
        else{
            try {
                String[] parts = date.split("-");
                if(parts.length > 2){
                    return Integer.parseInt(parts[2]);
                }
                return 0;
            } catch (Exception e){
                return 0;
            }
        }
    }

    public String getResult(){
        if(result == -1){
            return "No win";
        }
        else if(result == 1){
            return "Jackpot Winner";
        }
        else if(result == 2){
            return "Second-Tier Prize";
        }
        else if(result == 3){
            return "Third-Tier Prize";
        }
        else if(result == 4){
            return "Fourth-Tier Prize";
        }
        else if(result == 5){
            return "Fifth-Tier Prize";
        }
        else if(result == 6){
            return "Sixth-Tier Prize";
        }
        else if(result == 7){
            return "Seventh-Tier Prize";
        }
        else if(result == 8){
            return "Eighth-Tier Prize";
        }
        else if(result == 9){
            return "Ninth-Tier Prize";
        }
        else{
            return "-";
        }
    }
}
