package kr.co.roysoft.lotto649.data;

import java.io.Serializable;

public class Lotto649DTO implements Serializable {
    public int id;
    public String date;
    public int n1;
    public int n2;
    public int n3;
    public int n4;
    public int n5;
    public int n6;
    public int bb;
    public String jackpot;
    public int winners;
    public String next_jackpot;
    public String next_date;
    public String gold_number;
    public String gold_jackpot;
    public String gold_nextjackpot;

    public String getDateString(){
        if(date == null || date.equals("")){
            return "";
        }
        else{
            return date;
        }
    }

    public String getNextDateString(){
        if(next_date == null || next_date.equals("")){
            return "";
        }
        else{
            return next_date;
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
}
