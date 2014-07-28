package my.com.adapter;


import java.util.List;

import my.com.R;
import my.com.manager.PInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class CustomAdapter extends ArrayAdapter{

    private int resource;
    private LayoutInflater inflater;
    @SuppressWarnings("unused")
	private Context context;

    @SuppressWarnings("unchecked")
	public CustomAdapter ( Context ctx, int resourceId, List apps) {

        super( ctx, resourceId, apps );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }

    @SuppressLint("ViewHolder")
	@Override
    public View getView ( int position, View convertView, ViewGroup parent ) {

        convertView = (LinearLayout) inflater.inflate( resource, null );

        PInfo app = (PInfo) getItem(position);

        TextView txtName = (TextView) convertView.findViewById(R.id.textView1);
        txtName.setText(app.appname);

        ImageView imageCity = (ImageView) convertView.findViewById(R.id.imageView_floating);
        imageCity.setImageDrawable(app.icon);
        return convertView;
    }
}