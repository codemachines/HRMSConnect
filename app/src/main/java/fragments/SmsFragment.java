package fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hrmsconnect.R;

public class SmsFragment extends Fragment {

    EditText edtpno,edtmsg;
    ImageView ivm,ivp;
    View view;

    SmsManager smsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_sms,null);

        edtpno=view.findViewById(R.id.edtpno);
        edtmsg=view.findViewById(R.id.edtmsg);

        ivp=view.findViewById(R.id.ivp);
        ivm=view.findViewById(R.id.ivm);

        smsManager=SmsManager.getDefault();

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},111);
        }else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},111);
        }

        ivp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        ivm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String message=edtmsg.getText().toString();

                if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                }else {
                    smsManager.sendTextMessage(edtpno.getText().toString(),null,edtmsg.getText().toString(),null,null);
                    Toast.makeText(getActivity(), "Sms Sent", Toast.LENGTH_SHORT).show();                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK){
            Uri contactData=data.getData();

            Cursor c= getActivity().getApplicationContext().getContentResolver().query(contactData,null,null,null,null);

            if (c.moveToFirst()){

                String phoneno="";

                String name=c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactid=c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                String hasPhone=c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")){
                    hasPhone="true";
                }else
                {
                    hasPhone="false";
                }

                if (Boolean.parseBoolean(hasPhone)){
                    Cursor phones=getActivity().getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"+"+ contactid,null,null);
                    while (phones.moveToNext()){
                        phoneno=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }
                    phones.close();

                    Log.e("response>>",phoneno);

                    edtpno.setText(phoneno);

                    smsmethod(name,phoneno);
                }
            }
            c.close();
        }
    }

    private void smsmethod(String name, String phoneno) {


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                    smsManager.sendTextMessage(edtpno.getText().toString(),null,edtmsg.getText().toString(),null,null);
                    Toast.makeText(getActivity(), "Sms Sent", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Sms not sent", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
}
