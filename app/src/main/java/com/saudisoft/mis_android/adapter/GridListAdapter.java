package com.saudisoft.mis_android.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.saudisoft.mis_android.Model.ItemsInOutH;
import com.saudisoft.mis_android.R;

import java.util.List;


/**
 * Created by sonu on 08/02/17.
 */

public class GridListAdapter extends BaseAdapter {
    private Context context;
    public ItemsInOutH currentItem;
    private LayoutInflater inflater;
    private boolean isListView;
    private List<ItemsInOutH> mItems;
    private SparseBooleanArray mSelectedItemsIds;

    public GridListAdapter(Context context, List<ItemsInOutH> List, boolean isListView) {

        this.context = context;
        this.setItems(List);
//        this.Listvoucher = List;
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);
        mSelectedItemsIds = new SparseBooleanArray();
    }
    public void setItems(List<ItemsInOutH> mItems) {
        this.mItems = mItems;
    }
    @Override
    public int getCount() {
        return mItems.size();
    }

//    @Override
//    public Object getItem(int i) {
//        return Listvoucher.get(i);
//    }
    @Override
    public ItemsInOutH getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }
    public List<ItemsInOutH> getItems() {
        return mItems;
    }

    @Override
//    public long getItemId(int position) {
//        return (getItems() != null && !getItems().isEmpty()) ? Integer.parseInt( getItems().get(position).getSerial()) : position;
//    }
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            //inflate the layout on basis of boolean
            if (isListView)
                view = inflater.inflate(R.layout.list_custom_row_layout, viewGroup, false);
            else
                view = inflater.inflate(R.layout.grid_custom_row_layout, viewGroup, false);


            viewHolder.voucher_serial = (TextView) view.findViewById(R.id.voucher_serial_txt);
            viewHolder.voucher_date = (TextView) view.findViewById(R.id.voucher_date_txt);
            viewHolder.voucher_type = (TextView) view.findViewById(R.id.voucher_type_txt);
            viewHolder.Trans_Code = (TextView) view.findViewById(R.id.trans_Code_txt);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox1);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();


        viewHolder.checkBox.setChecked(mSelectedItemsIds.get(position));

        // fill row data
        currentItem = getItem(position);
        if(currentItem != null ) {

            viewHolder.voucher_serial.setText(currentItem.getSerial());
            viewHolder.voucher_type.setText(String.valueOf(currentItem.getmTranstype().getTransType()+""));
            viewHolder.Trans_Code.setText(currentItem.getTransCode());
            viewHolder.voucher_date.setText(currentItem.getTransDate());

        }

//        viewHolder.checkBox.setChecked(mSelectedItemsIds.get(Integer.parseInt(currentItem.getSerial())));


        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(position, !mSelectedItemsIds.get(position));
            }
        });



        return view;
    }

    private class ViewHolder {

        private TextView voucher_serial;
        private TextView Trans_Code;
        private TextView voucher_date;
        private TextView voucher_type;

        private CheckBox checkBox;
    }


    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put( position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
