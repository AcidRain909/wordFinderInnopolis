package ru.innopolis.gr9;

import java.io.FileWriter;
import java.io.IOException;

public class Searcher implements Runnable {

    SearchBase searchBase;

    Searcher(SearchBase searchBase) {
        this.searchBase = searchBase;
    }

    String filePart;

    @Override
    public void run() {
        getFilePart();

        while (filePart != null) {
            System.out.println(Thread.currentThread().getName());
            String[] proposals = filePart.split("\\.");
            for (String proposal :
                    proposals) {
                if (checkProposal(proposal)) {
                    writeToFile(proposal);
                }
            }

            getFilePart();
        }
    }

    public void getFilePart() {
        synchronized (searchBase) {
            try {
                filePart = searchBase.getNextSource();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkProposal(String proposal) {
            String[] words = searchBase.words;
            String[] wordsInSentense = proposal.split(" ");
            for (String word : words) {
                for (String wordInSen : wordsInSentense) {
                    if (word.toLowerCase().equals(wordInSen.toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;
        }


    public void writeToFile(String proposal) {
            try (FileWriter writer = new FileWriter(searchBase.res, true)) {
                writer.write(proposal + ". ");
                writer.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

