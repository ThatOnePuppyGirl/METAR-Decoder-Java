package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println(DecodeFormat("CYRQ 242352Z AUTO 14010G200KT 060V160 1 1/2SM +RA VCTS BR FEW024 BKN029 OVC060 20/20 A3001"));
    }
    static boolean NA_FLAG;

    public static String DecodeFormat(String format) {
        String GENERAL_INFO_NONFORMAT = "This is the weather report for airport %s, sent out at %s UTC on the %s day of the month. This is a(n) %s message.\n";
        String[] tokens = format.split(" ");
        // airport code
        String airport = tokens[0];
        NA_FLAG = airport.startsWith("K");
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
        String weather_info = "The weather information is as follows:\n";
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
            if (tokens[i].indexOf("SM") != -1 || tokens[i + 1].indexOf("SM") != -1 && tokens[i].indexOf("KT") == -1 && tokens[i].indexOf("V") == -1) {
                if (tokens[i].indexOf("SM") == -1) {
                    // it's 1 1/2SM or something similar
                    weather_info += "Visiblity: " + tokens[i] + " " + tokens[i + 1] + " of visibility.\n";
                    i ++;
                    continue;
                }
                // it's 1SM or something similar
                if (tokens[i].startsWith("M")) {
                    tokens[i] = "<1/4SM";
                }
                weather_info += "Visibility: " + tokens[i] + " of visibility.\n";
                
                continue;
            }
            if (i == tokens.length - 1) {
                info_to_add_to_string = DecodeToken(tokens[i], tokens[i]);
            } else {
                info_to_add_to_string = DecodeToken(tokens[i], tokens[i + 1]);
            }
            weather_info += info_to_add_to_string;
        }
        return general_info + weather_info;
    }

    public static String DecodeToken(String token1, String token2) {
        System.out.println("Token: " + token1);
        // first, check for wind
        if (token1.endsWith("KT") || token1.endsWith("KMH") || token1.endsWith("MPS")) {
            // we've got wind data
            if (token1.startsWith("00000")) {
                // no wind
                return "Wind: Calm (0 knots)\n";
            }
            // we have wind
            String wind_direction_string = token1.substring(0, 3);
            if (wind_direction_string.equals("VRB")) {
                wind_direction_string = "variable";
            }

            String wind_speed_string = token1.substring(3, 6);
            System.out.println(wind_speed_string);
            if (wind_speed_string.endsWith("K") || wind_speed_string.endsWith("M") || wind_speed_string.endsWith("G")) {
                wind_speed_string = wind_speed_string.substring(0, 2);
            }
            String wind_speed_unit = token1.substring(token1.length() - 3);
            if (wind_speed_unit.endsWith("KT")) {
                wind_speed_unit = "knots";
            } else if (wind_speed_unit.endsWith("MPS")) {
                wind_speed_unit = "meters per second";
            }
            String wind_gust_speed = "no";
            if (token1.contains("G")) {
                int index = token1.indexOf("G");
                wind_gust_speed = token1.substring(index + 1, index + 4);
                if (wind_gust_speed.endsWith("K") || wind_gust_speed.endsWith("M")) {
                    wind_gust_speed = wind_gust_speed.substring(0, 2);
                }
                wind_gust_speed += " " + wind_speed_unit;
            }
            return "Wind: From " + wind_direction_string + " degrees @ " + wind_speed_string + " " + wind_speed_unit + ", with " + wind_gust_speed + " gusts.\n";
        }
        // next, check for wind variance
        if (token1.indexOf("V") == 3) {
            String[] tokens = token1.split("V");
            return "Wind Variance: From " + tokens[0] + " degrees to " + tokens[1] + " degrees.\n";
        }
        
        return new String();
    }
}