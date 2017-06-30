package com.tsing.nested.nesteddemo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    final static String TAG = "ItemFragment";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String INDEX = "INDEX";
    private final MyViewPager vp;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private int index = 0;
    private OnListFragmentInteractionListener mListener;
    private float downY;
    private int[] consumed = new int[2];
    private int[] offsetInWindow = new int[2];

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment(MyViewPager vp) {
        this.vp = vp;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount, int index, MyViewPager vp) {
        ItemFragment fragment = new ItemFragment(vp);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            index = getArguments().getInt(INDEX);
        }
    }

    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            ViewCompat.setNestedScrollingEnabled(recyclerView, false);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            datas.clear();
            for (int x = 0; x < 15; x++) {
                datas.add("x ");
            }
            QuickAdapter mQuickAdapter = new QuickAdapter();
            TextView header = new TextView(this.getContext());
            header.setText("header\nheader\nheader");
            mQuickAdapter.addHeaderView(header);
            TextView footer = new TextView(this.getContext());
            footer.setText("footer\nfooter\nfooter");
            mQuickAdapter.addFooterView(footer);

            recyclerView.setAdapter(mQuickAdapter);
//            mQuickAdapter.disableLoadMoreIfNotFullPage();
//            BaseQuickAdapter
//            recyclerView.onStartNestedScroll()
//            recyclerView.setOnTouchListener(new MyOnTouchListener());
        }
        vp.setObjectForPosition(view, index);
        return view;
    }

    List<String> datas = new ArrayList<>();

    public class QuickAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public QuickAdapter() {


            super(R.layout.fragment_item, datas);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, String item) {
            viewHolder.setText(R.id.id, item + "...  ")
                    .setText(R.id.content, item + "=" + viewHolder.getLayoutPosition())
//                    .linkify(R.id.tweetText)
            ;
//            Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) viewHolder.getView(R.id.iv));
        }
    }

    /**
     * 这个方法通过pointerId获取pointerIndex,然后获取Y
     */
    private float getPointerY(MotionEvent event, int pointerId) {
        final int pointerIndex = MotionEventCompat.findPointerIndex(event, pointerId);
        if (pointerIndex < 0) {
            return -1;
        }
        return MotionEventCompat.getY(event, pointerIndex);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }

    public class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ViewParent parent = recyclerView.getParent();
            if (parent instanceof ViewPager) {
                ViewPager viewPager = (ViewPager) parent;
                int top = viewPager.getTop();

            }
            final int actionMasked = MotionEventCompat.getActionMasked(event);

            // 取第一个接触屏幕的手指Id
            final int pointerId = MotionEventCompat.getPointerId(event, 0);
            switch (actionMasked) {
                case MotionEvent.ACTION_DOWN:

                    // 取得当前的Y，并赋值给lastY变量
                    downY = getPointerY(event, pointerId);
                    // 找不到手指，放弃掉这个触摸事件流
                    if (downY == -1) {
                        return false;
                    }

                    // 通知父View，开始滑动
                    recyclerView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                    break;
                case MotionEvent.ACTION_MOVE:

                    // 获得当前手指的Y
                    final float pointerY = getPointerY(event, pointerId);

                    // 找不到手指，放弃掉这个触摸事件流
                    if (pointerY == -1) {
                        return false;
                    }

                    // 计算出滑动的偏移量
                    float deltaY = pointerY - downY;

                    Log.d(TAG, String.format("downY = %f", deltaY));

                    Log.d(TAG, String.format("before dispatchNestedPreScroll, deltaY = %f", deltaY));

                    // 通知父View, 子View想滑动 deltaY 个偏移量，父View要不要先滑一下，然后把父View滑了多少，告诉子View一下
                    // 下面这个方法的前两个参数为在x，y方向上想要滑动的偏移量
                    // 第三个参数为一个长度为2的整型数组，父View将消费掉的距离放置在这个数组里面
                    // 第四个参数为一个长度为2的整型数组，父View在屏幕里面的偏移量放置在这个数组里面
                    // 返回值为 true，代表父View有消费任何的滑动.
                    if (recyclerView.dispatchNestedPreScroll(0, (int) deltaY, consumed, offsetInWindow)) {

                        // 偏移量需要减掉被父View消费掉的
                        deltaY -= consumed[1];
                        Log.d(TAG, String.format("after dispatchNestedPreScroll , deltaY = %f", deltaY));

                    }

                    // 上面的 (int)deltaY 会造成精度丢失，这里把精度给舍弃掉
                    if (Math.floor(Math.abs(deltaY)) == 0) {
                        deltaY = 0;
                    }

                    // 这里移动子View，下面的min,max是为了控制边界，避免子View越界
//                    recyclerView.setY(Math.min(Math.max(recyclerView.getY() + deltaY, 0), ((View) recyclerView.getParent()).getHeight() - recyclerView.getHeight()));


                    break;
            }
            return true;
        }
    }
}
