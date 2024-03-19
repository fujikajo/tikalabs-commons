package de.tikalabs.commons.csv.utils;

import de.tikalabs.commons.csv.recordprocessor.ListRecordProcessor;
import de.tikalabs.commons.csv.recordprocessor.ProgressCallback;
import de.tikalabs.commons.csv.recordprocessor.RecordProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public abstract class AbstractCSVParser<T> {

    private final String filePath;
    private CSVParser csvParser;
    private List<String> headerNames;
    private int desiredHeaderIndex;
    private RecordProcessor<T> recordProcessor; // Verarbeitungslogik
    private ProgressCallback progressCallback;

    protected AbstractCSVParser(String filePath) {
        this.filePath = filePath;
        this.setDesiredHeaderIndex(0);
        this.recordProcessor = new ListRecordProcessor<T>();
    }

    protected AbstractCSVParser(String filePath, int headerIndex) {
        this.filePath = filePath;
        this.setDesiredHeaderIndex(headerIndex);
        this.recordProcessor = new ListRecordProcessor<T>();
    }

    protected AbstractCSVParser(String filePath, RecordProcessor<T> recordProcessor) {
        this.filePath = filePath;
        this.setDesiredHeaderIndex(0);
        this.recordProcessor = recordProcessor;
    }

    protected AbstractCSVParser(String filePath, RecordProcessor<T> recordProcessor, int headerIndex) {
        this.filePath = filePath;
        this.setDesiredHeaderIndex(headerIndex);
        this.recordProcessor = recordProcessor;
    }

    // Setter für den Callback
    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void parseCSV() {
        int i = 0;


        for (CSVRecord csvRecord : getCsvParser()) {

            if (recordProcessor != null) {
                recordProcessor.process(this.parseRecord(csvRecord));
                System.out.println("Datensatz: " + i);
            }
            i++;
            /*if (progressCallback != null) {
                progressCallback.updateProgress(i, totalRecords);
            }*/
        }

    }

    public int getDesiredHeaderIndex() {
        return this.desiredHeaderIndex;
    }

    public void setDesiredHeaderIndex(int headerIndex) {
        this.desiredHeaderIndex = headerIndex;
    }

    private void handleFileNotFound(FileNotFoundException e) {
        e.printStackTrace();
        // Behandlung der Ausnahme bei Datei nicht gefunden
    }

    private void handleIOException(IOException e) {
        e.printStackTrace();
        // Behandlung der allgemeinen Ein- / Ausgabefehler
    }

    public List<String> getHeaderNames() {
        return getCsvParser().getHeaderNames();
    }

    protected abstract T parseRecord(CSVRecord csvRecord);

    public CSVParser getCsvParser() {

        if (this.csvParser == null) {
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(filePath));
                if (this.getDesiredHeaderIndex() > 0) {
                    for (int i = 0; i < this.getDesiredHeaderIndex(); i++)
                        br.readLine();
                }
                this.csvParser = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(br);
                return this.csvParser;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        return csvParser;

    }

    public void setCsvParser(CSVParser csvParser) {
        this.csvParser = csvParser;
    }
}
