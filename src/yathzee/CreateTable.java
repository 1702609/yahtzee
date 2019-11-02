package yathzee;

import java.util.HashMap;
import java.util.List;

public class CreateTable
    {
    public static final String[] option = {"Ones","Twos","Threes","Fours"};

    public static void main(String[] args)
        {
        drawTable();

        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        hmap.put("Ones",4);
        updateScore(hmap);
        }

        private static void drawTable()
            {
            String leftAlignFormat = createFormat();
            String seperator = createSeperator();
            String title = createTitle();
            System.out.format(seperator);
            System.out.format(title);
            System.out.format(seperator);
            System.out.format(leftAlignFormat, "Ones",'N','N','N');
            System.out.format(leftAlignFormat, "Twos",'N','N','N');
            System.out.format(leftAlignFormat, "Threes",'N','N','N');
            System.out.format(leftAlignFormat, "Fours",'N','N','N');
            System.out.format(seperator);
            }

        public static void updateScore(HashMap<String, Integer> hmap)
            {

            }

        private static String createTitle()
        {
        String title = "| Score           | P1   ";
        for (int i = 0; i < 2; i++)
            {
            String temp = "P"+String.valueOf(i);
            title +="| "+temp+"   ";
            }
        title+="|%n";
        return title;
        }

        private static String createSeperator()
            {
            String seperator = "+-----------------+------+";
            for (int i = 0; i < 2; i++)
                {
                seperator +="------+";
                }
            seperator +="%n";
            return seperator;
            }

        private static String createFormat()
        {
        String leftAlignFormat = "| %-15s | %-4s";
        for (int i = 0; i < 2; i++)
            {
            leftAlignFormat +=" | %-4s";
            }
        leftAlignFormat+=" |%n";
        return leftAlignFormat;
        }

    }
