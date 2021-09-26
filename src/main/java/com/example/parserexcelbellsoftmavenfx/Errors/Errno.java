package com.example.parserexcelbellsoftmavenfx.Errors;

public abstract class Errno {
    protected String otherException;

    protected int errnoNumber = 0;

    public final int STATE_OK = 0;
    public final int STATE_ERROR_FILE_IS_NOT_FOUND = -1;
    public final int STATE_ERROR_FILE_CANNOT_BE_OPEN = -2;
    public final int STATE_SHEET_IS_NOT_FOUND = -3;
    public final int STATE_IS_NOT_SELECTED_SHEET = -4;
    public final int STATE_IS_NOT_OPEN_WORKBOOK = -5;
    public final int STATE_OTHER_EXCEPTION = -6;
    public final int STATE_FORMAT_FILE_IS_NOT_SUPPORTED = -7;
    public final int STATE_FILE_ALREADY_OPEN = -8;
    public final int STATE_FILE_IS_NOT_OPEN = -9;
    public final int STATE_CONNECTION_ALREADY_OPEN = -10;
    public final int STATE_CONNECTION_IS_NOT_OPEN = -11;

    public int getErrnoNumber(){ return this.errnoNumber; }

    public String getErrnoStr() {
        return getErrnoStr(this.errnoNumber);
    }

    public String getErrnoStr(int errnoNumber) {
        switch (errnoNumber) {
            case 0: {
                return "Ok";
            }

            case -1: {
                return "File is not found";
            }

            case -2: {
                return "File cannot be open";
            }

            case -3: {
                return "Sheet is not found";
            }

            case -4: {
                return "Sheet is not selected";
            }

            case -5: {
                return "Workbook is not open";
            }

            case -6: {
                return otherException;
            }

            case -7: {
                return "Format file is not supported";
            }

            case -8:{
                return "File already open";
            }

            case -9:{
                return "File is not open";
            }

            case -10:{
                return "Connection already open";
            }

            case -11:{
                return "Connection is not open";
            }

            default: {
                return "Unknown error";
            }
        }
    }
    protected void setState(int state, String errorMsg){
        this.errnoNumber = state;
        this.otherException = errorMsg;
    }
}
