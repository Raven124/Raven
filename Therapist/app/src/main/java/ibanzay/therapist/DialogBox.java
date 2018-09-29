package ibanzay.therapist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DialogBox extends android.support.v4.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.about, null);
        builder.setView(view);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(true);
        // Остальной код
        return builder.create();

    }
}
