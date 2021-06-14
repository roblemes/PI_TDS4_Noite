package br.edu.opet.pi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import br.edu.opet.pi.data.DBManager;

public class DialogAddProject extends AppCompatDialogFragment {
    private EditText editTextProjectName;
    private DBManager dbManager;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_project,null);

        builder.setView(view).setTitle("Add Project")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String project_name = editTextProjectName.getText().toString();
                        dbManager.insert_project(project_name);

                    }
                });
        editTextProjectName = view.findViewById(R.id.edit_project_name);
        return builder.create();
    }

}
