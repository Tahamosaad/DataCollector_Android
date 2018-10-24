package com.saudisoft.mis_android.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saudisoft.mis_android.Model.Company;
import com.saudisoft.mis_android.R;

import java.util.List;

public class ListCompaniesAdapter extends BaseAdapter {
	
	public static final String TAG = "ListCompnaiesAdapter";
	
	private List<Company> mItems;
	private LayoutInflater mInflater;
	private SparseBooleanArray mSelectedItemsIds;
	public ListCompaniesAdapter(Context context, List<Company> listCompanies) {
		this.setItems(listCompanies);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
	}

	@Override
	public Company getItem(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
	}

	@Override
	public long getItemId(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if(v == null) {
			v = mInflater.inflate(R.layout.list_item_company, parent, false);
			holder = new ViewHolder();
			holder.txtCompanyName = (TextView) v.findViewById(R.id.txt_server_name);
			holder.txtAddress = (TextView) v.findViewById(R.id.txt_branch_code);
			holder.txtPhoneNumber = (TextView) v.findViewById(R.id.txt_phone_number);
			holder.txtWebsite = (TextView) v.findViewById(R.id.txt_database);

			v.setTag(holder);
		}
		else {
			holder = (ViewHolder) v.getTag();
		}
		
		// fill row data
		Company currentItem = getItem(position);
		if(currentItem != null) {
			holder.txtCompanyName.setText(currentItem.getName());
			holder.txtAddress.setText(currentItem.getAddress());
			holder.txtWebsite.setText(currentItem.getWebsite());
			holder.txtPhoneNumber.setText(currentItem.getPhoneNumber());
		}

		return v;
	}

	public List<Company> getItems() {
		return mItems;
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
			mSelectedItemsIds.put(position, true);
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
	public void setItems(List<Company> mItems) {
		this.mItems = mItems;
	}

	class ViewHolder {
		TextView txtCompanyName;
		TextView txtWebsite;
		TextView txtPhoneNumber;
		TextView txtAddress;

	}

}
