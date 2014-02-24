using System;
using Android.Preferences;
using Android.Views;
using Android.Graphics;
using Android.Widget;
using Android.Content;
using Android.Util;
using Android.App;

namespace ColorPickerSample
{
    public class ColorSelectPreference : Preference
    {
        readonly Color[] DEFINE_COLORS = { Color.Blue, Color.Green, Color.Red };
        View _widgetView;

        Color _selectedColor;
        public Color Color
        {
            get { return _selectedColor; }
            set 
            {
                var changed = value != _selectedColor;
                _selectedColor = value;
                PersistInt(value.ToArgb());
                
                if (changed) 
                {
                    NotifyChanged();
                }
            }
        }

        class ColorListAdapter : ArrayAdapter<Color>
        {
            public ColorListAdapter(Context context, Color[] colors) 
                : base(context, Android.Resource.Layout.SimpleListItem1, colors)
            {
            }

            public override View GetView(int position, View convertView, ViewGroup parent)
            {
                var view = base.GetView(position, convertView, parent) as TextView;
                view.SetBackgroundColor(new Color(GetItem(position)));
                view.SetTextColor(Color.Transparent); // 色値が表示されないように隠す
                return view;
            }
        }

        public ColorSelectPreference(Context context, IAttributeSet attrs) : base(context, attrs)
        {
        this.WidgetLayoutResource = ColorPickerSample.Resource.Layout.PrefColorselectWidget;
        }
        
        protected override void OnBindView(View view)
        {
            base.OnBindView(view);
            _widgetView = view.FindViewById<View>(ColorPickerSample.Resource.Id.my_widget);
            var colorValue = GetPersistedInt(-1);
            if (colorValue != -1) {
                this.Color = new Color(colorValue);
                UpdateColor(this.Color);
            }
        }

        protected override void OnClick()
        {
            var adapter = new ColorListAdapter(this.Context, DEFINE_COLORS);
            new AlertDialog.Builder(this.Context)
                .SetTitle(this.Title)
                .SetAdapter(adapter, 
                (s, e) =>
                {
                    var color = DEFINE_COLORS [e.Which];
                    this.Color = color;
                    UpdateColor(color);
                })
                .Show();
        }
        
        private void UpdateColor(Color color) 
        {
            if (_widgetView == null) {
                return;
            }
            _widgetView.SetBackgroundColor(color);
        }    
    }
}