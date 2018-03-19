package com.bmx.kike.blackList;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bmx.kike.blackList.Model.DaoContactos;
import com.bmx.kike.blackList.Pojos.Contacto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private ViewStub stubList;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<Contacto> listacontactos;

    private Contacto contacto;

    private int operacion = 0;
    private int indice = 0;
    private EditText txtbuscar;


    String operaciones[] =
            new String[]
                    {"DESBLOQUEAR"};



    public void  btnList_click(){
        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setTitle("MENSAJE DE DESBLOQUEO DE CONTACTOS")
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
                new AlertDialog.Builder(getContext())
                        .setTitle("DESBLOQUEAR")
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage("ESTA SEGURO DE DESBLOQUEAR EL CONTACTO?")
                        .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DaoContactos dao = new DaoContactos(getContext());
                                dao.delete(listacontactos.get(indice).getID()+"");
                                setAdapters();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);


        stubList = (ViewStub) view.findViewById(R.id.stub_list);


        //inflar ViewStub antes de obtener la vista
        stubList.inflate();


        listView = (ListView) view.findViewById(R.id.mylistview);


        clic();


            setAdapters();
            botonflotante(view);

        return view;
    }

    public void clic(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                indice = i;
                btnList_click();
            }
        });
    }
    private void botonflotante(View view){
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operacion=1;
                Intent contactos = new Intent(getContext(), contactos.class);
                startActivity(contactos);
                setAdapters();
            }
        });

    }

    private void setAdapters() {
           DaoContactos daocontact = new DaoContactos(getContext());
        listacontactos = new ArrayList<>();
        listacontactos = daocontact.getContactos();

            listViewAdapter = new ListViewAdapter(getContext(), R.layout.list_item, daocontact.getContactos());
            listView.setAdapter(listViewAdapter);

    }


    AdapterView.OnItemLongClickListener onItemClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            indice = i;
          //  btnList_click();
            return false;
        }
    };



    public void insertar(Contacto contact){
        DaoContactos dao = new DaoContactos(getContext());
        if(dao.Insert(contact)>1){
          //  Toast.makeText(getContext(),getString(R.string.notainsertada)+"",Toast.LENGTH_LONG).show();
        }else{
           // Toast.makeText(getContext(),getString(R.string.notanoinsertada)+"",Toast.LENGTH_LONG).show();
        }
    }





    public void eliminar(String id){
        DaoContactos dao = new DaoContactos(getContext());
        if(dao.delete(id)>1){
          //  Toast.makeText(getContext(),getString(R.string.notanoeliminada)+"",Toast.LENGTH_LONG).show();
        }else{
            setAdapters();
           // Toast.makeText(getContext(),getString(R.string.notaeliminada)+"",Toast.LENGTH_LONG).show();
        }
    }

    public String fecha(){
        final Calendar c= Calendar.getInstance();
        int dia=c.get(Calendar.DAY_OF_MONTH);
        int mes=c.get(Calendar.MONTH)+1;
        int year=c.get(Calendar.YEAR);
        return year+"/"+mes+"/"+dia;
    }
    public String horasistema(){
        final Calendar c= Calendar.getInstance();
        int h=c.get(Calendar.HOUR_OF_DAY);
        int minutos=c.get(Calendar.MINUTE);
        return h+":"+minutos;
    }

    public String validar(EditText txt,EditText txt1){
         String inconvenientes="";
        if(txt.getText().toString().length()==0){
            inconvenientes+="A";
        }
        if(txt1.getText().toString().length()==0){
            inconvenientes+="A";
        }

        if(inconvenientes.length()>0) {
           // Toast.makeText(getContext(), getString(R.string.doscamposobligatorios)+"", Toast.LENGTH_SHORT).show();
        }

        return inconvenientes;
    }

    @Override
    public void onResume(){
        super.onResume();
        setAdapters();
    }





}//
