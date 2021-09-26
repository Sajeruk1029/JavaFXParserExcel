package com.example.parserexcelbellsoftmavenfx.Parser;

import com.example.parserexcelbellsoftmavenfx.DB.Database;
import com.example.parserexcelbellsoftmavenfx.Errors.Errno;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Parser extends Errno {
    private String fileName;

    private File file;
    private FileInputStream fileInputStream;

    private XSSFWorkbook workbookX;
    private XSSFSheet sheetX;

    private Iterator<Row> rowIterator;
    private Iterator<Cell> cellIterator;

    private int listNumber = 0;
    private int row = 0;
    private int cell = 0;

    private final Logger log;

    public Parser(String fileName) {
        this.fileName = fileName;

        log = LogManager.getLogger("LOGGER");

        if(this.checkFormat()) {
            this.setState(STATE_OK, "");
            log.info("Check format terminated has been successfully!");
        }
        else{
            this.setState(this.STATE_FORMAT_FILE_IS_NOT_SUPPORTED, "");
            log.error(this.getErrnoStr());
        }
    }

    public Parser(String fileName, int listNumber) {
        this.fileName = fileName;
        this.listNumber = listNumber;

        log = LogManager.getLogger("LOGGER");

        if(this.checkFormat()) {
            setState(STATE_OK, "");
            log.info("Check format terminated has been successfully!");
        }
        else{
            setState(STATE_FORMAT_FILE_IS_NOT_SUPPORTED, "");
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getListNumber(){
        return this.listNumber;
    }

    public int getRow(){
        return this.row;
    }

    public int getCell(){
        return this.cell;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setCell(int cell){
        this.cell = cell;
    }

    public void setListNumber(int listNumber){
        this.listNumber = listNumber;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    private boolean checkFormat(){
        return ((this.fileName.contains(".")) && (this.fileName.substring(this.fileName.lastIndexOf('.') + 1).equals("xlsx")));
    }

    public void openFile() throws IOException {

        if(this.file != null){
            this.setState(this.STATE_FILE_ALREADY_OPEN, "");
            log.error(this.getErrnoStr());
            return;
        }

        if(!checkFormat()){
            this.setState(this.STATE_FORMAT_FILE_IS_NOT_SUPPORTED, "");
            log.error(this.getErrnoStr());
            return;
        }

        this.file = new File(this.fileName);

        if((this.file.canRead() && this.file.exists())) {

            try {
                this.fileInputStream = new FileInputStream(this.file);

                this.workbookX = new XSSFWorkbook(this.fileInputStream);
            }
            catch (Exception ex){
                this.setState(STATE_OTHER_EXCEPTION, ex.getMessage());
                log.error(this.getErrnoStr());
            }
            finally {
                if (this.workbookX != null) {
                    this.setState(STATE_OK, "");
                    log.info("Excel file has been opened successfully!");
                }
            }
        }
        else {
            if (!this.file.exists()) {
                this.setState(this.STATE_ERROR_FILE_IS_NOT_FOUND, "");
                log.error(this.getErrnoStr());
            }
            else{
                this.setState(this.STATE_ERROR_FILE_CANNOT_BE_OPEN, "");
                log.error(this.getErrnoStr());
            }
        }
    }

    public void focusingOnSheet(){

        if(this.workbookX == null) {
            this.setState(STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return;
        }

        if(this.workbookX.getNumberOfSheets() <= listNumber) {
            this.setState(this.STATE_SHEET_IS_NOT_FOUND, "");
            log.error(this.getErrnoStr());
            return;
        }

        this.sheetX = this.workbookX.getSheetAt(listNumber);
        setState(STATE_OK, "");
        log.info("Focus has been installed on list of workbook excel file successfully!");
    }

    public void closeFile() throws IOException {

        if(file == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return;
        }

        try {
            this.fileInputStream.close();
            this.fileInputStream = null;
            this.file = null;
        }
        catch (Exception ex){
            this.setState(this.STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
        }
        finally {
            if(fileInputStream == null){
                this.setState(this.STATE_OK, "");
                log.info("Excel file has been closed!");
            }
        }
    }

    public int getLastRowNumber(){
        if(file == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return 0;
        }

        if(sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return 0;
        }

        this.setState(this.STATE_OK, "");
        log.info("Get last row of list workbook number!");
        return this.sheetX.getLastRowNum();
    }

    public ArrayList<String> getLineStringValues(){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= this.cell; ++counter){

            resultRow = sheetX.getRow(this.row);

            if (resultRow == null){
                list.add("");
                continue;
            }

            cell = resultRow.getCell(counter);

            list.add((cell != null)? cell.toString() : "");
        }

        log.info("Get line string values from list workbook!");
        return list;
    }

    public ArrayList<String> getLineStringValues(int cellFinal, int row){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= cellFinal; ++counter){

            resultRow = sheetX.getRow(row);

            if (resultRow == null){
                list.add("");
                continue;
            }

            cell = resultRow.getCell(counter);

            list.add((cell != null)? cell.toString() : "");
        }

        log.info("Get line string values from list workbook!");
        return list;
    }

    public ArrayList<String> getLineStringValues(int cellStart, int cellFinal, int row){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = cellStart; counter <= cellFinal; ++counter){

            resultRow = sheetX.getRow(row);

            if (resultRow == null){
                list.add("");
                continue;
            }

            cell = resultRow.getCell(counter);

            list.add((cell != null)? cell.toString() : "");
        }

        log.info("Get line string values from list workbook!");
        return list;
    }

    public ArrayList<String> getColumnStringValues(){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= this.row; ++counter){

            resultRow = sheetX.getRow(counter);

            if (resultRow == null){
                list.add("");
                continue;
            }

            cell = resultRow.getCell(this.cell);

            list.add((cell != null)? cell.toString() : "");
        }

        log.info("Get column string values from list workbook!");
        return list;
    }

    public ArrayList<String> getColumnStringValues(int rowFinal, int column){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= rowFinal; ++counter){

            resultRow = sheetX.getRow(counter);

            if (resultRow == null){
                list.add("");
                continue;
            }

            cell = resultRow.getCell(column);

            list.add((cell != null)? cell.toString() : "");
        }

        log.info("Get column string values from list workbook!");
        return list;
    }

    public ArrayList<String> getColumnStringValues(int columnStart, int rowFinal, int column){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = columnStart; counter <= rowFinal; ++counter){

            resultRow = sheetX.getRow(counter);

            if (resultRow == null){
                list.add("");
                continue;
            }

            cell = resultRow.getCell(column);

            list.add((cell != null)? cell.toString() : "");
        }

        log.info("Get column string values from list workbook!");
        return list;
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= this.row; ++counter)
        {
            listBuf.clear();

            for(int counter2 = 0; counter2 <= this.cell; ++counter2){

                resultRow = sheetX.getRow(counter);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter2);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Get lines string values from list workbook!");
        return list;
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(int rowFinish, int cellFinish){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= rowFinish; ++counter)
        {
            listBuf.clear();

            for(int counter2 = 0; counter2 <= cellFinish; ++counter2){

                resultRow = sheetX.getRow(counter);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter2);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Get lines string values from list workbook!");
        return list;
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(int rowFinish, int cellFinish, int rowStart, int cellStart){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = rowStart; counter <= rowFinish; ++counter)
        {
            listBuf.clear();

            for(int counter2 = cellStart; counter2 <= cellFinish; ++counter2){

                resultRow = sheetX.getRow(counter);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter2);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Get lines string values from list workbook!");
        return list;
    }

    public ArrayList<ArrayList<String>> getColumnsStringValues(){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= this.cell; ++counter)
        {
            listBuf.clear();

            for(int counter2 = 0; counter2 <= this.row; ++counter2){

                resultRow = sheetX.getRow(counter2);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Get columns string values from list workbook!");
        return list;
    }

    public ArrayList<ArrayList<String>> getColumnsStringValues(int rowFinish, int cellFinish){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= cellFinish; ++counter)
        {
            listBuf.clear();

            for(int counter2 = 0; counter2 <= rowFinish; ++counter2){

                resultRow = sheetX.getRow(counter2);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Get columns string values from list workbook!");
        return list;
    }

    public ArrayList<ArrayList<String>> getColumnsStringValues(int rowFinish, int cellFinish, int rowStart, int cellStart){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = cellStart; counter <= cellFinish; ++counter)
        {
            listBuf.clear();

            for(int counter2 = rowStart; counter2 <= rowFinish; ++counter2){

                resultRow = sheetX.getRow(counter2);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Get columns string values from list workbook!");
        return list;
    }

    public String getStringValue(){

        XSSFCell result = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return "";
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return "";
        }

        resultRow = sheetX.getRow(this.row);

        if(resultRow == null){
            return "";
        }

        result = resultRow.getCell(this.cell);

        log.info("Get string value from list workbook!");
        return (result != null)? result.toString() : "";
    }

    public String getStringValue(int row, int cell){

        XSSFCell result = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return "";
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return "";
        }

        resultRow = sheetX.getRow(row);

        if(resultRow == null){
            return "";
        }

        result = resultRow.getCell(cell);

        log.info("Get string value from list workbook!");
        return (result != null)? result.toString() : "";
    }

    public ArrayList<Integer> getNumbersCells(String template, ArrayList<String> headers){

        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int counter = 0; counter < template.split(":").length; ++counter){
            for(int counter2 = 0; counter2 < headers.size(); ++counter2) {
                if (template.split(":")[counter].equals(headers.get(counter2))) {
                    list.add(counter2);
                }
            }
        }

        return list;
    }

    public ArrayList<ArrayList<String>> getSelectedDataString(ArrayList<Integer> numbersColumn, ArrayList<ArrayList<String>> listData){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String>  listElement =  new ArrayList<String>();

            for(int counter = 0; counter < listData.size();  ++counter){
                listElement.clear();
                for(int counter2 = 0; counter2 < numbersColumn.size(); ++counter2){
                    listElement.add(listData.get(counter).get(numbersColumn.get(counter2)));
                }
                list.add((ArrayList<String>) listElement.clone());
            }

        return list;
    }

    public static void createExcelFile(String fileName, String sheetName, String headers, ArrayList<ArrayList<String>> data){
        File file = new File(fileName);

        final Logger log;

        log = LogManager.getLogger(Parser.class);

        int rowCounter = 0;

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow row = sheet.createRow(rowCounter);

        FileOutputStream fileOutputStream = null;

        for(int counter = 0; counter < headers.split(":").length; ++counter){
            row.createCell(counter).setCellValue(headers.split(":")[counter]);
        }

        ++rowCounter;

        for (int counter = 0; counter < data.size(); ++counter){
            row = sheet.createRow(rowCounter);
            for(int counter2 = 0; counter2 < data.get(counter).size(); ++counter2){
                row.createCell(counter2).setCellValue(data.get(counter).get(counter2));
            }
            ++rowCounter;
        }

        try{
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            return;
        }
        finally {
            log.info("Excel file has been created successfully!");
        }
    }
}
