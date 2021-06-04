package com.tdj.mock.data;

import com.tdj.mock.data.util.DateUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//@EnableAutoConfiguration
//@ComponentScan("com.tdj.mock")
public class EdgeMock {

    public static void main(String[] args) {

        File file_06 = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_06.csv");
        mock(file_06, "2020-06-01", "2020-06-30");

        File file_07 = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_07.csv");
        mock(file_07, "2020-07-01", "2020-07-31");

        File file_08 = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_08.csv");
        mock(file_08, "2020-08-01", "2020-08-31");

        File file_09 = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_09.csv");
        mock(file_09, "2020-09-01", "2020-09-30");

        File file_10 = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_10.csv");
        mock(file_10, "2020-10-01", "2020-10-31");

        File file_11 = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_11.csv");
        mock(file_11, "2020-11-01", "2020-11-30");

        File file_12 = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_12.csv");
        mock(file_12, "2020-12-01", "2020-12-31");
    }

    public static void mock(File file, String startTime, String endTime) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> lines = new ArrayList<String>();
        long s = System.currentTimeMillis();
        for (int i = 1; i <= 200000000; i++) {
            StringBuilder line = new StringBuilder();
            int fromUserId = getRandomValue(10000000, 30000000);
            int toUserId = getRandomValue(10000000, 30000000, fromUserId);
            line.append(fromUserId);
            line.append(",");
            line.append(toUserId);
            line.append(",");
            line.append(DateUtils.randomDate(startTime, endTime).getTime());
            lines.add(line.toString());
            if (lines.size() == 10000) {
                try {
                    FileUtils.writeLines(file, lines, true);
                    lines.clear();
                    System.out.println(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (lines.size() > 0) {
            try {
                FileUtils.writeLines(file, lines, true);
                lines.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("cost: " + (System.currentTimeMillis() - s));
    }

    public static int getRandomValue(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    public static int getRandomValue(int min, int max, int notEqualUserId) {
        Random random = new Random();
        int s = 0;
        do {
            s = random.nextInt(max) % (max - min + 1) + min;
        } while (s == notEqualUserId);
        return s;
    }
}
