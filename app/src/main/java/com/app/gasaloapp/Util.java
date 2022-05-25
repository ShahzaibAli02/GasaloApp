package com.app.gasaloapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {




    public static  boolean  isEmpty(TextInputEditText...  editTexts)
    {
        for(EditText editText:editTexts)
        {
            if(editText.getText().toString().isEmpty())
            {
                editText.setError("Required Field");
                editText.requestFocus();
                return true;
            }

        }
        return false;

    }
    public  static  void  showCustomToast(Activity activity, String Message, boolean Error)
    {


        if(activity==null)
            return;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_message,null);

        CardView cardView = layout.findViewById(R.id.card);
        cardView.setCardBackgroundColor(Error? Color.parseColor("#ad0000"):Color.parseColor("#0c7a00"));
        TextView text = (TextView) layout.findViewById(R.id.txtMessage);
        text.setText(Message);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }


    public  static Dialog getDialog(Context context, int layout)
    {
        Dialog dialog=new Dialog(context);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }



}
