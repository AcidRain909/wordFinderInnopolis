package ru.innopolis.gr9;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main{

    public static final String SOURCES_DEST = "E://testSet//simpletest"; //Путь к вашим файлам
    public static final String[] WORDS = {"CREQIZATERRATOMORF", "starter", "ffdf", "wfrrf", "cdcd","dc"}; //Слова которые ищем
    public static final String RES = "E://searcherResult.txt"; //Куда пишем результат
    public static final SearchBase SEARCH_BASE = new SearchBase();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        SEARCH_BASE.getOccurencies(sourcesToArr(), WORDS, RES);

        //Создание потоков и отслеживание завершения
        System.out.println("Ожидание завершения потоков...");
        int nThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < nThreads; i++) {
            executorService.submit(new Searcher(SEARCH_BASE));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println("Все потоки завершены.");
            long now = System.currentTimeMillis();
            int delta = (int) (now - start) / 1000;
            System.out.println("Длительность: " + delta + " сек.");
        } catch (InterruptedException e) {

        }

    }

    public static String[] sourcesToArr(){
        File dir = new File(SOURCES_DEST);
        File[] arrFiles = dir.listFiles();
        int fileCount = arrFiles.length;

        String[] sources = new String[fileCount];
        for (int i = 0; i < arrFiles.length; i++) {
            sources[i] = arrFiles[i].toString();
        }
        return sources;
    }
}