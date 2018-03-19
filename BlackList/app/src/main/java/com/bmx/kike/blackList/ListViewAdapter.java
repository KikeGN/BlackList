package com.bmx.kike.blackList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmx.kike.blackList.Pojos.Contacto;

import java.util.List;


public class ListViewAdapter extends ArrayAdapter<Contacto> {
    public ListViewAdapter(Context context, int resource, List<Contacto> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        final Contacto contacto = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtcontacto = (TextView) v.findViewById(R.id.lbcontacto);
        TextView txtnumero = (TextView) v.findViewById(R.id.lbnumero);


        txtcontacto.setText("Contracto:"+contacto.getContacto());
        txtnumero.setText("Telefono:"+contacto.getTelefono());


        return v;
    }
}
