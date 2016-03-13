package com.aqqxa.plugin;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTextAligner {

    String text;
    String regex;
    List<String> lines;
    int maxLength;

    public RegexTextAligner(String text, String regex) {
        this.text = text;
        this.regex = regex;
        this.lines = Arrays.asList(text.split("\n", -1));
    }

    public String alignText() {
        return StringUtils.join(alignLines(buildLinesWithIndices()), "\n");
    }

    private List<String> alignLines(List<Line> linesWithIndices) {
        List<String> lines = new ArrayList<>();
        for (Line linesWithIndice : linesWithIndices) {
            lines.add(linesWithIndice.injectSpaces());
        }
        return lines;
    }

    private List<Line> buildLinesWithIndices() {
        maxLength = 0;

        List<Line> linesWithIndices = new ArrayList<Line>();
        for (String line : lines) {
            String[] parts = line.split(regex);
            if (parts.length < 2) {
                linesWithIndices.add(new Line(line, null, null));
                continue;
            }
            Line l = new Line(parts[0].replaceAll("\\s+$", ""), parts[1].trim(), regex);
            int len = l.start.length();
            if (len > maxLength) {
                maxLength = len;
            }
            linesWithIndices.add(l);
        }
        return linesWithIndices;
    }

    class Line {
        String start;
        String end;
        String sign;

        Line(String start, String end, String sign) {
            this.start = start;
            this.end = end;
            this.sign = sign;
        }

        String injectSpaces() {
            if(end == null){
                return start;
            }
            return start + sign + StringUtils.leftPad("", maxLength - start.length() + 1) + end;
        }
    }
}
