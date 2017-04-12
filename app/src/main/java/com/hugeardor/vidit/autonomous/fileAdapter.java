package com.hugeardor.vidit.autonomous;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vidit on 9/4/17.
 */

public class fileAdapter extends BaseAdapter {


    private Context mcontext;
    private List<file> mfilelist;
    //constructore


    public fileAdapter(Context mcontext, List<file> mfilelist) {
        this.mcontext = mcontext;
        this.mfilelist = mfilelist;
    }

    @Override
    public int getCount() {
        return mfilelist.size();
    }

    @Override
    public Object getItem(int position) {
        return mfilelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mcontext, R.layout.file_list, null);
        TextView tfn = (TextView)v.findViewById(R.id.file_name);
        TextView lm = (TextView)v.findViewById(R.id.mod_date);
        TextView  td = (TextView)v.findViewById(R.id.date);

        // set values for these text views
        tfn.setText(mfilelist.get(position).getF_n());
       td.setText(mfilelist.get(position).getfd());


        v.setTag(mfilelist.get(position));


        return v;
    }
}
