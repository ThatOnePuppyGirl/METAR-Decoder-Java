package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println(DecodeFormat("METAR KABC 121755Z AUTO 21016G24KT 180V240 1SM R11/P6000FT -RA BR BKN015 OVC025 06/04 A2990 RMK AO2 PK WND 20032/25 WSHFT 1715 VIS 3/4V1 1/2 VIS 3/4 RWY11 RAB07 CIG 013V017 CIG 017 RWY11 PRESFR SLP125 P0003 60009 T00640036 10066 21012 58033 TSNO $"));
    }


    public static String DecodeFormat(String format) {
        String GENERAL_INFO_NONFORMAT = "This is the weather report for airport %s, sent out at %s UTC on the %s day of the month. This is a(n) %s message.\n";
        String[] tokens = format.split(" ");
        boolean NA_FLAG = false;
        // check for METAR format
        if (!(tokens[0].equals("METAR"))) {
            return "Not a valid METAR string.";
        }
        // airport code, as well as NA flag setting
        String airport = tokens[1];
        if (tokens[1].startsWith("K") || tokens[1].startsWith("C")) {
            NA_FLAG = true;
        }
        // timestamp, get day of month and time UTC
        String timestamp = tokens[2];
        String day = timestamp.substring(0, 2);
        String time_utc = timestamp.substring(2, 6);
        String day_string = day + (day.equals("1") ? "st" : (day.equals("2")? "nd" : (day.equals("3") ? "rd" : "th")));
        String time_string = time_utc.substring(0, 2) + ":" + time_utc.substring(2);
        
        // we start seeing optional arguments now, so we need an index counter
        int current = 3;

        // report type may not be there, so we need to check if it is
        String type_string = "manual";
        if (tokens[current].equals("AUTO") || tokens[current].equals("COR")) {
            if (tokens[current].equals("AUTO")) {
                type_string = "automatic";
            } else {
                type_string = "correction";
            }
            current++;
        }

        // next token should be wind information, which is always there
        String wind_info = tokens[current];
        current++;
        String true_degrees = wind_info.substring(0, 3);
        String wind_speed = wind_info.substring(3, 5);
        String gusts = "no";
        if (wind_info.substring(5, 6).equals("G")) {
            gusts = wind_info.substring(6, 8) + " knot";
        }
        String wind_info_string = String.format("The wind is heading at %s degrees, at a speed of %s knots with %s gusts.", true_degrees, wind_speed, gusts);
        
        // now we look for varience, if it is there we will increment and then add to wind info
        if (tokens[current].contains("V")) {
            
        }

        String general_info = String.format(GENERAL_INFO_NONFORMAT, airport, time_string, day_string, type_string);
        String weather_info = String.format("blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah");
        return general_info + wind_info_string;
    }
}