package ru.innopolis.gr9;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SearchBase implements Occurencies {
    private static final int PART_SIZE = 10_000_000;
    private static int fileIndex = 0;
    private static int skipStart = 0;
    boolean thisFileEnds = false;
    private static int stopPos = PART_SIZE;

    String[] sources;
    String[] words;
    String res;

    @Override
    public void getOccurencies(String[] sources, String[] words, String res) {
        this.sources = sources;
        this.words = words;
        this.res = res;
    }

    public synchronized String getNextSource() throws IOException {
            if (fileIndex < sources.length) {
                String filePath = sources[fileIndex];
                String filePart = getFilePart(filePath);

                System.out.println(fileIndex + "/" + sources.length);

                return filePart;
            } else {
                return null;
            }
    }

    public synchronized String getFilePart(String filePath) throws IOException {
            File file = new File(filePath);
            InputStream is = new FileInputStream(file);
            long length = file.length();

            int offset = 0;
            int numRead = 0;
            int available = is.available();

            int len = 0;
            thisFileEnds = false;
            if (available < stopPos) {
                len = available - skipStart;
                thisFileEnds = true;
            } else {
                len = stopPos - skipStart;
            }

            System.out.println(available - stopPos);

            byte[] filePart = new byte[len];

            is.skip(skipStart);

            while (offset < len && (numRead = is.read(filePart, 0, len)) >= 0) {
                offset += numRead;
            }

            is.close();

            StringBuilder filePartStringBuilder = new StringBuilder(new String(filePart, "UTF-8"));
            String filePartString;
            int lastPoint = filePartStringBuilder.lastIndexOf(".") + 1;
            skipStart += lastPoint;
            stopPos = skipStart + PART_SIZE;
            filePartString = filePartStringBuilder.substring(0, lastPoint).trim();

            if (thisFileEnds) {
                skipStart = 0;
                stopPos = PART_SIZE;
                fileIndex++;
            }

            return filePartString;
    }
}
