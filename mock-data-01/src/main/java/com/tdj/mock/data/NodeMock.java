package com.tdj.mock.data;

import com.tdj.mock.data.util.DateUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@EnableAutoConfiguration
//@ComponentScan("com.tdj.mock")
public class NodeMock {

    public static void main(String[] args) {

        File file_node = new File("D:\\IdeaProjects\\code-examples\\mock-data-01\\src\\main\\resources\\graph_data_node.csv");
        mock(file_node);

    }

    public static void mock(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> lines = new ArrayList<String>();
        for (int i = 10000000; i <= 30000000; i++) {
            lines.add(i + "");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            lines.clear();
        }
    }
}
