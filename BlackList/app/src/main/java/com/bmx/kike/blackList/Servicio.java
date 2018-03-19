package com.bmx.kike.blackList;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;

import com.bmx.kike.blackList.Model.DaoContactos;
import com.bmx.kike.blackList.Pojos.Contacto;

import java.util.List;


public class Servicio extends Service {

    @Override
    public void onCreate() {

    }

    hilo h;

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess) {

        try {
            if (h == null) {
                h = new hilo();
                h.start();
            }
        } catch (Exception err) {

        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            if (h.isAlive()) {
                h.stop();
            }
        } catch (Exception err) {

        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public class hilo extends Thread {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            while (true) {


                try {

                    DaoContactos dao = new DaoContactos(getBaseContext());
                    List<Contacto> lista = dao.getContactos();

                    for (int i = 0; i < lista.size(); i++) {
                        Uri CALLLOG_URI = Uri.parse("content://call_log/calls");
                        getApplicationContext().getContentResolver().delete(CALLLOG_URI, CallLog.Calls.NUMBER + "=?", new String[]{lista.get(i).getTelefono()});


                     /*
                        String queryString = "NUMBER="+lista.get(i).getTelefono();
                        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        getContentResolver().delete(android.provider.CallLog.Calls.CONTENT_URI,  "NUMBER="+lista.get(i).getTelefono(), null);
*/
                    }

                } catch (Exception e) {

                }
            }
        }

        public void deleteSMS(String number) {
            try {

                Uri uriSms = Uri.parse("content://sms/inbox");
                Cursor c = getContentResolver().query(uriSms,
                        new String[]{"_id", "thread_id", "address",
                                "person", "date", "body"}, null, null, null);

                if (c != null && c.moveToFirst()) {
                    do {
                        long id = c.getLong(0);
                        long threadId = c.getLong(1);
                        String address = c.getString(2);
                        String body = c.getString(5);

                        if (address.equals(number)) {
                            getContentResolver().delete(
                                    Uri.parse("content://sms/" + id), null, null);
                        }
                    } while (c.moveToNext());
                }
            } catch (Exception e) {

            }

        }
    }

}
