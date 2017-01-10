package cn.kuwo.sing.phone4tv.view.activity;

import android.os.Bundle;
import android.util.Log;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.commons.util.CommonUtils;
import cn.kuwo.sing.phone4tv.view.fragment.OrderedFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class OrderedActivity extends SherlockFragmentActivity{
	OrderedFragment orderedFragment;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(android.R.anim.fade_in, R.anim.push_right_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordered);
		
		CommonUtils.setActionBarColor(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		orderedFragment = new OrderedFragment();
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.flFragmentOrdered, new OrderedFragment())
		.commit();
	}
}
