package cn.kuwo.sing.tv.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.sing.tv.R;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends Fragment {
	private static final String LOG_TAG = "ItemListFragment";

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = -1;

	private RadioButton rbOrderedMtv;

	private RadioButton rbPinyinOrder;

	private RadioButton rbMtvCategory;
	
	private RadioButton rbMtvCategoryOrder;

	private RadioButton rbSingerCategory;

	private RadioButton rbMore;

	private ScrollView navigationScrollView;
	
	private String mLastRbTag;

	public interface Callbacks {
		public void onItemSelected(String id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	public ItemListFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.item_list_activated_fragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	private void initView() {
		navigationScrollView = (ScrollView)getActivity().findViewById(R.id.navigationScrollView);
		rbOrderedMtv = (RadioButton) getActivity().findViewById(R.id.rbOrderedMtv);
		rbOrderedMtv.setOnFocusChangeListener(mOnFocusChangeListener);
		rbPinyinOrder = (RadioButton) getActivity().findViewById(R.id.rbPinyinOrder);
		rbPinyinOrder.setOnFocusChangeListener(mOnFocusChangeListener);
		rbMtvCategory = (RadioButton) getActivity().findViewById(R.id.rbMtvCategory);
		rbMtvCategory.setOnFocusChangeListener(mOnFocusChangeListener);
		rbMtvCategoryOrder = (RadioButton) getActivity().findViewById(R.id.rbMtvCategoryOrder);
		rbMtvCategoryOrder.setOnFocusChangeListener(mOnFocusChangeListener);
		rbSingerCategory = (RadioButton) getActivity().findViewById(R.id.rbSingerCategory);
		rbSingerCategory.setOnFocusChangeListener(mOnFocusChangeListener);
		rbMore = (RadioButton) getActivity().findViewById(R.id.rbMore);
		rbMore.setOnFocusChangeListener(mOnFocusChangeListener);
		
		mLastRbTag = (String) rbMtvCategory.getTag();
	}
	
	private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus) {
				if(mCallbacks == null)
					mCallbacks = (Callbacks) getActivity();
				
				switch (v.getId()) {
				case R.id.rbOrderedMtv:
					selectOrderedMtv();
					break;
				case R.id.rbPinyinOrder:
					selectPinyinOrder();
					break;
				case R.id.rbMtvCategory:
					if(mLastRbTag.equals(rbMtvCategoryOrder.getTag()))
						navigationScrollView.smoothScrollBy(0, -AppContext.SCREEN_HIGHT/5);
					selectMtvCategory();
					break;
				case R.id.rbMtvCategoryOrder:
					if(mLastRbTag.equals(rbMtvCategory.getTag()))
						navigationScrollView.smoothScrollBy(0, AppContext.SCREEN_HIGHT/5);
					selectMtvCategoryOrder();
					break;
				case R.id.rbSingerCategory:
					selectSingerCategory();
					break;
				case R.id.rbMore:
					selectUserFeedback();
					break;
				default:
					break;
				}
				mLastRbTag = (String) v.getTag();
			}
		}
	};
	
	private void selectOrderedMtv() {
		mCallbacks.onItemSelected("0");
		rbOrderedMtv.performClick();
	}
	
	private void selectPinyinOrder() {
		mCallbacks.onItemSelected("1");
		rbPinyinOrder.performClick();
	}
	
	private void selectMtvCategory() {
		mCallbacks.onItemSelected("2");
		rbMtvCategory.performClick();
	}
	
	private void selectMtvCategoryOrder() {
		mCallbacks.onItemSelected("3");
		rbMtvCategoryOrder.performClick();
	}
	
	private void selectSingerCategory() {
		mCallbacks.onItemSelected("4");
		rbSingerCategory.performClick();
	}
	
	private void selectUserFeedback() {
		mCallbacks.onItemSelected("5");
		rbMore.performClick();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	private void setActivatedPosition(int position) {
		
		switch (position) {
		case 0:
			RadioButton rbOrderedMtv = (RadioButton) getActivity().findViewById(R.id.rbOrderedMtv);
			rbOrderedMtv.requestFocus();
			rbOrderedMtv.performClick();
			break;
		case 1:
			RadioButton rbPinyinOrder = (RadioButton) getActivity().findViewById(R.id.rbPinyinOrder);
			rbPinyinOrder.requestFocus();
			rbPinyinOrder.performClick();
			break;
		case 2:
			RadioButton rbMtvCategory = (RadioButton) getActivity().findViewById(R.id.rbMtvCategory);
			int mtvItemPosition = getActivity().getIntent().getIntExtra("itemPosition", 0);
			if(mtvItemPosition == 0) {
				rbMtvCategory.requestFocus();
				rbMtvCategory.performClick();
			}
			break;
			
		case 3:
			RadioButton rbMtvCategoryOrder = (RadioButton) getActivity().findViewById(R.id.rbMtvCategory);
			int mtvOrderItemPosition = getActivity().getIntent().getIntExtra("itemPosition", 0);
			if(mtvOrderItemPosition == 0) {
				rbMtvCategoryOrder.requestFocus();
				rbMtvCategoryOrder.performClick();
			}
			break;
			
		case 4:
			RadioButton rbSingerCategory = (RadioButton) getActivity().findViewById(R.id.rbSingerCategory);
			int singerItemPosition = getActivity().getIntent().getIntExtra("itemPosition", 0);
			if(singerItemPosition == 0) {
				rbSingerCategory.requestFocus();
				rbSingerCategory.performClick();
			}
			break;
		case 5:
			RadioButton rbUserFeedback = (RadioButton) getActivity().findViewById(R.id.rbMore);
			rbUserFeedback.requestFocus();
			rbUserFeedback.performClick();
			break;
		default:
			break;
		}
	}

	public void setActivateOnItemClick(int id) {
		setActivatedPosition(id);
		mCallbacks.onItemSelected(id+"");
	}
}
