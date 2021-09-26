package com.example.parserexcelbellsoftmavenfx.Parser;

import com.example.parserexcelbellsoftmavenfx.Errors.Errno;
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

    public Parser(String fileName) {
        this.fileName = fileName;

        if(this.checkFormat()) {
            this.setState(STATE_OK, "");
        }
        else{
            this.setState(this.STATE_FORMAT_FILE_IS_NOT_SUPPORTED, "");
        }
    }

    public Parser(String fileName, int listNumber) {
        this.fileName = fileName;
        this.listNumber = listNumber;

        if(this.checkFormat()) {
            setState(STATE_OK, "");
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

    public int getErrnoNumber(){ return this.errnoNumber; }

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
            return;
        }

        if(!checkFormat()){
            this.setState(this.STATE_FORMAT_FILE_IS_NOT_SUPPORTED, "");
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
            }
            finally {
                if (this.workbookX != null) {
                    this.setState(STATE_OK, "");
                }
            }
        }
        else {
            if (!this.file.exists()) {
                this.setState(this.STATE_ERROR_FILE_IS_NOT_FOUND, "");
            }
            else{
                this.setState(this.STATE_ERROR_FILE_CANNOT_BE_OPEN, "");
            }
        }
    }

    public void focusingOnSheet(){

        if(this.workbookX == null) {
            this.setState(STATE_IS_NOT_OPEN_WORKBOOK, "");
            return;
        }

        if(this.workbookX.getNumberOfSheets() <= listNumber) {
            this.setState(this.STATE_SHEET_IS_NOT_FOUND, "");
            return;
        }

        this.sheetX = this.workbookX.getSheetAt(listNumber);
    }

    public void closeFile() throws IOException {

        if(file == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return;
        }

        try {
            this.fileInputStream.close();
            this.fileInputStream = null;
            this.file = null;
        }
        catch (Exception ex){
            this.setState(this.STATE_OTHER_EXCEPTION, ex.getMessage());
        }
        finally {
            if(fileInputStream == null){
                this.setState(this.STATE_OK, "");
            }
        }
    }

    public int getLastRowNumber(){
        if(file == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
        }

        if(sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
        }

        this.setState(this.STATE_OK, "");
        return this.sheetX.getLastRowNum();
    }

    public ArrayList<String> getLineStringValues(){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<String> getLineStringValues(int cellFinal, int row){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<String> getLineStringValues(int cellStart, int cellFinal, int row){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<String> getColumnStringValues(){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<String> getColumnStringValues(int rowFinal, int column){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<String> getColumnStringValues(int columnStart, int rowFinal, int column){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(int rowFinish, int cellFinish){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(int rowFinish, int cellFinish, int rowStart, int cellStart){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<ArrayList<String>> getColumnsStringValues(){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<ArrayList<String>> getColumnsStringValues(int rowFinish, int cellFinish){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public ArrayList<ArrayList<String>> getColumnsStringValues(int rowFinish, int cellFinish, int rowStart, int cellStart){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return list;
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
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

        return list;
    }

    public String getStringValue(){

        XSSFCell result = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return "";
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            return "";
        }

        resultRow = sheetX.getRow(this.row);

        if(resultRow == null){
            return "";
        }

        result = resultRow.getCell(this.cell);

        return (result != null)? result.toString() : "";
    }

    public String getStringValue(int row, int cell){

        XSSFCell result = null;
        XSSFRow resultRow = null;

        if(this.workbookX == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            return "";
        }

        if(this.sheetX == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            return "";
        }

        resultRow = sheetX.getRow(row);

        if(resultRow == null){
            return "";
        }

        result = resultRow.getCell(cell);

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
            return;
        }
    }
}
