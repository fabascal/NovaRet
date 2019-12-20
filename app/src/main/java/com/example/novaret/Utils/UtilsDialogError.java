package com.example.novaret.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novaret.MainActivity;
import com.example.novaret.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class UtilsDialogError {

    public static void showAlert(Context context, String message)
    {
        new MaterialAlertDialogBuilder(context,R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle(R.string.dialog_confirm_title)
                .setMessage(message)
                .setNegativeButton(R.string.dialog_confirm, null)
                .show();
    }
    public static void showAlertPrinter(Context context, String message)
    {
        new MaterialAlertDialogBuilder(context,R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle(R.string.dialog_confirm_title_printer)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_inicio,((dialog, which) -> {
                    Intent intent= new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }))
                .setNegativeButton(R.string.dialog_reimpresion, null)
                .show();
    }


}
