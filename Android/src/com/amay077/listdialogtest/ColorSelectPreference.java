package com.amay077.listdialogtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ColorSelectPreference extends Preference {
	final Integer[] DEFINE_COLORS = { Color.BLUE, Color.GREEN, Color.RED };
	View _widgetView;
	private int _selectedColor;
	
	static class ColorListAdapter extends ArrayAdapter<Integer> {
		public ColorListAdapter(Context context, Integer[] colors) {
			super(context, android.R.layout.simple_list_item_1, colors);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView)super.getView(position, convertView, parent);
			view.setBackgroundColor(getItem(position));
			view.setTextColor(Color.TRANSPARENT); // 色値が表示されないように隠す
			return view;
		}
	}

	public ColorSelectPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWidgetLayoutResource(R.layout.pref_colorselect_widget);
	}
	
	@Override
	protected void onClick() {
		ColorListAdapter adapter = new ColorListAdapter(getContext(), DEFINE_COLORS);
		new AlertDialog.Builder(getContext())
    	.setTitle(getTitle())
    	.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int color = DEFINE_COLORS[which];
				setColor(color);
				updateColor(color);
			}
		}).show();
	}
	
	private void setColor(int color) {
		final boolean changed = color != _selectedColor;
		_selectedColor = color;
		persistInt(color);
		
		if (changed) {
			notifyChanged();
		}
	}
	public int getColor() {
		return _selectedColor;
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		_widgetView = view.findViewById(R.id.my_widget);
		_selectedColor = getPersistedInt(-1);
		if (_selectedColor != -1) {
			updateColor(_selectedColor);
		}
	}
	
	private void updateColor(int color) {
		if (_widgetView == null) {
			return;
		}
		_widgetView.setBackgroundColor(color);
	}
}
