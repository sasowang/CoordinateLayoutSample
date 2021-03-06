package com.chris.coordinatorlayoutsample.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chris.coordinatorlayoutsample.R;
import com.chris.coordinatorlayoutsample.activity.BaseActivity;
import com.chris.coordinatorlayoutsample.activity.CustomActionBarActivity;
import com.chris.coordinatorlayoutsample.activity.CustomNavigationViewActivity;
import com.chris.coordinatorlayoutsample.activity.ItemDetailActivity;
import com.chris.coordinatorlayoutsample.activity.NormalNavigationViewActivity;
import com.chris.coordinatorlayoutsample.tool.LowerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 16/8/1.
 */
public class ItemFragment extends Fragment {

    private BaseActivity mActivity;

    public static ItemFragment newInstance(String s) {

        ItemFragment fragment = new ItemFragment();

        Bundle b = new Bundle();
        b.putString("TEMP_KEY", s);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NormalNavigationViewActivity) {
            mActivity = (NormalNavigationViewActivity) context;
        } else if (context instanceof CustomActionBarActivity) {
            mActivity = (CustomActionBarActivity) context;
        } else if (context instanceof CustomNavigationViewActivity) {
            mActivity = (CustomNavigationViewActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment, null);

        initialView(view);

        return view;
    }

    private void initialView(View v) {

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });


        String s = getArguments().getString("TEMP_KEY");

        final ViewPager mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        TextPagerAdapter adapter = new TextPagerAdapter(s);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mActivity.mTabs));

        mActivity.mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<String> list;

        Context context;

        public MyAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, viewGroup, false);
            return new BaseHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

//            MyUpperViewHolder myViewHolder = (MyUpperViewHolder) holder;
//
//            myViewHolder.tvText.setText(list.get(position));

            BaseHolder baseHolder = (BaseHolder) holder;

            ((TextView)baseHolder.getView(R.id.tvText)).setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyUpperViewHolder extends RecyclerView.ViewHolder {

            public TextView tvText;
            public RelativeLayout rlGroup;

            public MyUpperViewHolder(View itemView) {
                super(itemView);
                tvText = (TextView) itemView.findViewById(R.id.tvText);
                rlGroup = (RelativeLayout) itemView.findViewById(R.id.rlGroup);

                rlGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = getAdapterPosition();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailActivity.EXTRA_NAME, list.get(position));

                        context.startActivity(intent);
                    }
                });
            }
        }

        public class BaseHolder extends RecyclerView.ViewHolder {

            SparseArray<View> viewSparseArray;
            View itemView;

            public BaseHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                viewSparseArray = new SparseArray<>();
                setBaseHolderClickEvent(itemView);
            }

            public <T extends View>T getView(@IdRes int viewId){
                View view = viewSparseArray.get(viewId);
                if (view == null) {
                    view = itemView.findViewById(viewId);
                    viewSparseArray.put(viewId, view);

                    setSubLayoutClickEvent(itemView,view);
                }
                return (T) view;
            }

            public void setBaseHolderClickEvent(final View parent){

                parent.setTag(false);

                parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }

                        if((Boolean) parent.getTag()){
                            return;
                        }

                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailActivity.EXTRA_NAME, list.get(position));
                        context.startActivity(intent);
                    }
                });
            }

            public void setSubLayoutClickEvent(final View parent, View child){
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        parent.setTag(true);

                        Toast.makeText(context,"123",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    class TextPagerAdapter extends PagerAdapter {
        String s;

        public TextPagerAdapter(String s) {
            this.s = s;
        }

        @Override
        public int getCount() {
            return mActivity.mTabs.getTabCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "TAB " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mActivity.getLayoutInflater().inflate(R.layout.recycler_view_list,
                    container, false);
            container.addView(view);

            RecyclerView rvList = (RecyclerView) view.findViewById(R.id.rvList);

            rvList.setLayoutManager(new LinearLayoutManager(rvList.getContext()));

            List<String> list = new ArrayList<>();

            for (int i = 0; i < 40; i++) {
                list.add(s + " | " + mActivity.mTabs.getTabAt(position).getText().toString() + " | " + "position " + String.valueOf(i));
            }
            rvList.setAdapter(new MyAdapter(container.getContext(),list));

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
