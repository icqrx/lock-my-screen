package my.com.widget;


import my.com.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SpanThree extends Fragment{
	
	@SuppressWarnings("unused")
	private FragmentActivity fa;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fa = super.getActivity();
		View three = inflater.inflate(R.layout.span_three, container, false);
		return three;
	}

}
