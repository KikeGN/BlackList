package com.bmx.kike.blackList;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bmx.kike.blackList.Model.DaoContactos;
import com.bmx.kike.blackList.Pojos.Contacto;

import java.util.ArrayList;
import java.util.List;

public class contactos extends AppCompatActivity {

    private ListView listView1;
    ArrayAdapter<Contacto> adaptador;
    private List<Contacto> contactossistema;
    private int indice=0;

    TextView textView;

    String operaciones[] =
            new String[]
                    {"BLOQUEAR"};

    public void  btnList_click(){
        AlertDialog dialog =
                new AlertDialog.Builder(contactos.this)
                        .setTitle("MENSAJE DE BLOQUEO DE UN CONTACTO")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(operaciones[which].equalsIgnoreCase(operaciones[0])){

                                    confirmacion();
                                }

                                dialog.dismiss();
                            }
                        })
                        .create();

        dialog.show();
    }

    public void confirmacion(){

        AlertDialog dialog =
                new AlertDialog.Builder(contactos.this)
                        .setTitle("BLOQUEAR")
                        .setIcon(android.R.drawable.stat_sys_warning)
                        .setMessage("CONFIRMAR PARA BLOQUEAR?")
                        .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            DaoContactos contactos = new DaoContactos(getBaseContext());

                            Contacto contact = new Contacto();
                            contact.setContacto(contactossistema.get(indice).getContacto());
                            contact.setTelefono(contactossistema.get(indice).getTelefono());
                            contact.setBlockLlamadas("S");
                            contact.setBlockmensajes("S");
                            contactos.Insert(contact);
                            Toast.makeText(getBaseContext(),"CONTACTO BLOQUEADO",Toast.LENGTH_LONG).show();
                            finish();



                            }
                        })
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Toast.makeText(getContext(),"Cancelar",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();

        dialog.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        solicitarPermisos();

        getSupportActionBar().setTitle("Contactos");

        textView = (TextView) findViewById(R.id.action_search);

        try {

            contactossistema = new ArrayList<Contacto>();
            listView1 = (ListView) findViewById(R.id.listacontactos);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        indice = i;
                        btnList_click();

                       //confirmacion();
                    }catch (Exception err){
                        Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

   obtenerDatos();

        }catch (Exception err){
            Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
        }
    }



    public void obtenerDatos(){
        Cursor mCursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[] { ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE },
                ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                        + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL", null,
                ContactsContract.Data.DISPLAY_NAME + " ASC");
        startManagingCursor(mCursor);
// Setup the list
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, // context
                android.R.layout.simple_list_item_2, // Layout for the rows
                mCursor, // cursor
                new String[] { ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER }, // cursor
// fields
                new int[] { android.R.id.text1, android.R.id.text2 } // view
// fields
        );

        listView1.setAdapter(adapter);

        if (mCursor.moveToFirst()) {
            do {
                String contacto=mCursor.getString(1);
                String numero = mCursor.getString(2);
                contactossistema.add(new Contacto(contacto,numero,"S","S"));
            } while (mCursor.moveToNext());
        }


    }



    private SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);


        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("Buscar...");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
*/


        return true;

    }

public void solicitarPermisos()
{
    int resquestCode = 1;
    // Here, thisActivity is the current activity
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    resquestCode );

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CONTACTS},
                    resquestCode );

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }
}

}
