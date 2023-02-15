package com.app.gasaloapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.widget.Toast;

import com.app.gasaloapp.Model.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class XlsSheetManager extends ContextWrapper
{


    HSSFWorkbook workbook;
    HSSFSheet sheet;
    int rowCount=1;
    public XlsSheetManager(Context base)
    {
        super(base);
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Sheet No 1");
    }
    public void  addHeader()
    {

        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLUE.index);
        font.setBoldweight((short) 35);

        //Identificativo persona
        //Data rifornimento
        //Litri riforniti
        //itri progressivi
        //Note
        //IMAGE URL


        HSSFFont font2 = workbook.createFont();
        font2.setColor(HSSFColor.BLACK.index);
        font2.setBoldweight((short) 35);

        HSSFRow row = sheet.createRow(rowCount);
        final HSSFCellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font2);
        HSSFCell cellA=row.createCell(0);
        cellA.setCellStyle(cellStyle);
        cellA.setCellValue("DATE");

        cellA=row.createCell(1);
        cellA.setCellStyle(cellStyle);
        cellA.setCellValue(new SimpleDateFormat("dd-MM-yyyy hh:mm:aa", Locale.ENGLISH).format(new Date()));



        int columnindex=-1;
        HSSFRow rowA = sheet.createRow(++rowCount);
        HSSFCell cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("Identificativo persona");

        cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("Data rifornimento");

        cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("Litri riforniti");


        cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("Litri progressivi");


        cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("Price");


        cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("Total Price");


        cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("Note");



        cell=rowA.createCell(++columnindex);
        cell.getCellStyle().setFont(font);
        cell.setCellValue("IMAGE URL");

    }
    public  void  addRow(Entry entry)
    {
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight((short) 35);
        final HSSFCellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);

        int columnindex=-1;
        HSSFRow rowA = sheet.createRow(++rowCount);

        HSSFCell cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entry.person_id);

        cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entry.date);

        cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entry.total_literes);


        cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entry.now_refueling);


        cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entry.price);

        double totalLiteres=0.0;
        double totalPrice=0.0;
        if(notEmptyAndNull(entry.total_literes))
            totalLiteres = Double.parseDouble(entry.total_literes);

        totalPrice=totalLiteres*entry.price;

        cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(totalPrice);


        cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entry.note);



        cell=rowA.createCell(++columnindex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entry.image);
    }
    boolean notEmptyAndNull(String str)
    {
        return str!=null && !str.isEmpty();
    }
    public  String save()
    {

        FileOutputStream fos = null;
        String file_name="gasaloapp_"+new Random().nextInt(1000)+".xls";
        File file=null ;
        try {
            File str_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            file = new File(str_path,  file_name);
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        Toast.makeText(this, "Excel Sheet Saved At : "+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        return file.getAbsolutePath();
    }

}
