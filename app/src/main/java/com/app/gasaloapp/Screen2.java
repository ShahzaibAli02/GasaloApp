package com.app.gasaloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.gasaloapp.Interfaces.DataBaseResponse;
import com.app.gasaloapp.Model.Entry;
import com.app.gasaloapp.Model.Tank;
import com.app.gasaloapp.Utils.DataBaseManager;
import com.app.gasaloapp.databinding.ActivityScreen2Binding;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Screen2 extends AppCompatActivity {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 111;


    ActivityScreen2Binding binding;
    String QR;
    public  static  Tank tank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        tank=new Tank();

        binding=ActivityScreen2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.qrScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog=Util.getDialog(Screen2.this,R.layout.lyt_qr_pick);
                MaterialButton btnCamera=dialog.findViewById(R.id.btnCamera);
                MaterialButton btnGallery=dialog.findViewById(R.id.btnGallery);


                final View.OnClickListener onClickListener=new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        if(view==btnGallery)
                        {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK);
                            pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(pickIntent, 111);
                        }

                        if(view==btnCamera)
                        {
                            IntentIntegrator integrator = new IntentIntegrator(Screen2.this);
                            integrator.setOrientationLocked(false);
                            integrator.initiateScan();
                        }

                    }
                };

                btnCamera.setOnClickListener(onClickListener);
                btnGallery.setOnClickListener(onClickListener);
                dialog.show();


            }
        });

        binding.btnSubMit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Util.isEmpty( binding.etTank,binding.etLiters,binding.etTotalLiters) && QR!=null && !QR.isEmpty())
                {

                    updateLitres();

                    Entry entry=new Entry();
                    entry.person_id=QR;
                    new SharedPref(Screen2.this).saveQrCode(QR);
                    entry.now_refueling=binding.etLiters.getText().toString();
                    entry.total_literes=binding.etTotalLiters.getText().toString();
                    startActivity(new Intent(Screen2.this,Screen3.class).putExtra(Screen3.EXTRA_ENTRY_KEY,entry));
                }

            }
        });
    }

    private void updateLitres() {
        tank.litres=Integer.parseInt(binding.etTank.getText().toString());
        tank.litres-=Integer.parseInt(binding.etTotalLiters.getText().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        readTotalLitres();
    }

    private void readTotalLitres() {
        binding.etTank.setText("Loading....");
        DataBaseManager.readTotalLitres(new DataBaseResponse() {
            @Override
            public void onResponse(boolean isSuccess, String message, Object Data) {

                if(isSuccess && Data!=null)
                {
                    tank=(Tank) Data;
                    binding.etTank.setText(String.valueOf(tank.litres));
                }
                else
                {
                    tank=new Tank();
                    binding.etTank.setText("");
                    binding.etTank.setHint("Insert Total Litres");
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result1 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result1 != null) {

            if (result1.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {

                binding.txtQr.setText("QR : "+result1.getContents());
                QR=result1.getContents();
                Toast.makeText(Screen2.this,  "Successfully Scanner QR", Toast.LENGTH_SHORT).show();

            }

        }

        switch (requestCode) {
            //the case is because you might be handling multiple request codes here
            case 111:
                if (data == null || data.getData() == null) {
                    Log.e("TAG", "The uri is null, probably the user cancelled the image selection process using the back button.");
                    return;
                }
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null) {
                        Log.e("TAG", "uri is not a bitmap," + uri.toString());
                        return;
                    }
                    int width = bitmap.getWidth(), height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                  //  bitmap.recycle();
                  //  bitmap = null;
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    MultiFormatReader reader = new MultiFormatReader();
                    try {
                        Result result = reader.decode(bBitmap);
                        QR=result.getText();
                        binding.txtQr.setText("QR : "+result.getText());
                        Toast.makeText(Screen2.this,  "Successfully Scanner QR", Toast.LENGTH_SHORT).show();

                    } catch (Resources.NotFoundException e) {
                        Toast.makeText(this, "Error " +e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "decode exception", e);
                    } catch (NotFoundException e) {
                        Toast.makeText(this, "No QR CODE FOUND IN THIS IMAGE", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    Log.e("TAG", "can not open file" + uri.toString(), e);
                }
                break;

        }
    }
}