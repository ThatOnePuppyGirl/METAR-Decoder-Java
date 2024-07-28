package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println(DecodeFormat("KABC 121755Z 21016G24KT 180V240 1SM R11/P6000FT -RA BR BKN015 OVC025 06/04 A2990 RMK AO2 PK WND 20032/25 WSHFT 1715 VIS 3/4V1 1/2 VIS 3/4 RWY11 RAB07 CIG 013V017 CIG 017 RWY11 PRESFR SLP125 P0003 60009 T00640036 10066 21012 58033 TSNO $"));
    }


    public static String DecodeFormat(String format) {
        String GENERAL_INFO_NONFORMAT = "This is the weather report for airport %s, sent out at %s UTC on the %s day of the month. This is a(n) %s message.\n";
        String[] tokens = format.split(" ");
        // airport code
        String airport = tokens[0];
        // timestamp, get day of month and time UTC
        String timestamp = tokens[1];
        String day = timestamp.substring(0, 2);
        String time_utc = timestamp.substring(2, 6);
        String day_string = day + (day.equals("1") ? "st" : (day.equals("2")? "nd" : (day.equals("3") ? "rd" : "th")));
        String time_string = time_utc.substring(0, 2) + ":" + time_utc.substring(2);
        
        // we start seeing optional arguments now, so we need an index counter
        int current = 2;
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
        String general_info = String.format(GENERAL_INFO_NONFORMAT, airport, time_string, day_string, type_string);
        // we now need to loop over the remaining tokens and convert them
        // until we reach the end of the tokens array or we find "RMK"
        for (int i = current; i < tokens.length - 1; i++) {
            // we're not interested in tokens after "RMK"
            if (tokens[i].equals("RMK")) {
                break;
            }
            // we must check for every possible token now...
            // this will be done using a function that takes the token (and the next)
            // we send the next token in the rare case of a visibility being in the format of "# #/#SM"
            // otherwise we won't use the next token
            String info_to_add_to_string;
            if (i == tokens.length - 1) {
                info_to_add_to_string = DecodeToken(tokens[i], tokens[i]);
            } else {
                info_to_add_to_string = DecodeToken(tokens[i], tokens[i + 1]);
            }
        }
        String weather_info = String.format("blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah");
        return general_info;
    }

    public static String DecodeToken(String token1, String token2) {



        return new String();
    }
}