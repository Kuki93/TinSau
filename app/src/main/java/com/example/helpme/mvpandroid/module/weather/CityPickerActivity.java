package com.example.helpme.mvpandroid.module.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.CityFilterRecyclerAdapter;
import com.example.helpme.mvpandroid.base.BaseActivity;
import com.example.helpme.mvpandroid.entity.weather.AllCityCode;
import com.example.helpme.mvpandroid.utils.FileUtils;
import com.example.helpme.mvpandroid.widget.DividerItemDecoration;
import com.example.helpme.mvpandroid.widget.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityPickerActivity extends BaseActivity {
    
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.fl_city)
    FlowLayout mCityFlowLayout;
    @BindView(R.id.fl_attraction)
    FlowLayout mAttractionFlowLayout;
    
    long[] cityIds = {101010100, 101020100, 101210101, 101190101, 101190401, 101200101, 101280601, 101270101, 101040100,
            101030100, 101110101, 101280101, 101130109, 101291401, 101251505, 101240203, 101290201, 101070201, 101221001,
            101271906, 101251101, 101291301};
    String[] cityNames = {"北京", "上海", "杭州", "南京", "苏州", "武汉", "深圳", "成都", "重庆", "天津", "西安", "广州", "天池", "丽江",
            "凤凰", "庐山", "大理", "大连", "黄山", "九寨沟", "张家界", "香格里拉"};
    private List<AllCityCode> mAllCityCodes;
    private List<AllCityCode> mSuggestList;
    private CityFilterRecyclerAdapter mFilterRecyclerAdapter;
    private String text;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorWhite));
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text");
        }
        setTitle("城市查询");
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(null);
        mSuggestList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //添加Android自带的分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1.5f));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        
        mFilterRecyclerAdapter = new CityFilterRecyclerAdapter(R.layout.item_city_suggestion, mSuggestList);
        //   mRecyclerView.setAdapter(mFilterRecyclerAdapter);
        mFilterRecyclerAdapter.bindToRecyclerView(mRecyclerView);
        mFilterRecyclerAdapter.setEmptyView(R.layout.layout_empty_view);
        
        mFilterRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                backOnCity(mSuggestList.get(position).getCountyname(), mSuggestList.get(position).getAreaid());
            }
        });
        ArrayList<AllCityCode> cityCodes = getIntent().getParcelableArrayListExtra(GlobalConfig.CITY_ADDKEY);
        // 关键字集合
        List<AllCityCode> allCityCodes = new ArrayList<>();
        for (int i = 0; i < cityIds.length; i++) {
            AllCityCode mCityCode = new AllCityCode(cityIds[i], cityNames[i]);
            if (cityCodes != null) {
                for (AllCityCode cityCode : cityCodes) {
                    if (cityCode.getAreaid() == cityIds[i]) {
                        mCityCode.setCheck(true);
                        break;
                    }
                }
            }
            allCityCodes.add(mCityCode);
        }
        
        
        mCityFlowLayout.setFlowLayout(allCityCodes.subList(0, 12), new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(AllCityCode mAllCityCode) {
                if (mAllCityCode == null) {
                    Snackbar.make(mToolbar, "该地区天气已添加！", Snackbar.LENGTH_SHORT).show();
                } else {
                    backOnCity(mAllCityCode.getCountyname(), mAllCityCode.getAreaid());
                }
            }
        });
        mAttractionFlowLayout.setFlowLayout(allCityCodes.subList(13, 22), new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(AllCityCode mAllCityCode) {
                if (mAllCityCode == null) {
                    Snackbar.make(mToolbar, "该地区天气已添加！", Snackbar.LENGTH_SHORT).show();
                } else {
                    backOnCity(mAllCityCode.getCountyname(), mAllCityCode.getAreaid());
                }
            }
        });
        
        
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("text", text);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        mAllCityCodes = new Gson().fromJson(FileUtils.getJson("meizu_city.json", CityPickerActivity.this), new
                TypeToken<List<AllCityCode>>() {
                }.getType());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //通过MenuItem得到SearchView
        SearchView searchView = (SearchView) searchItem.getActionView();
        EditText textView = searchView.findViewById(R.id.search_src_text);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                mSuggestList.clear();
                if (newText.length() > 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (isExistCN(newText)) {
                        String cn = filterCN(newText);
                        for (AllCityCode allCityCode : mAllCityCodes) {
                            if (allCityCode.getCountyname().contains(cn) || cn.contains(allCityCode.getCountyname())) {
                                mSuggestList.add(allCityCode);
                            }
                        }
                    } else {
                        newText = newText.toLowerCase();
                        filterData(splitSpell(newText), newText);
                    }
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
                mFilterRecyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });
        if (text != null && text.length() > 0) {
            textView.setText(text);
            textView.setSelection(text.length());
        } else {
            searchView.setQueryHint("搜索要添加的城市");
        }
        return super.onCreateOptionsMenu(menu);
    }
    
    private void backOnCity(String cityName, long cityId) {
        Intent intent = new Intent();
        intent.putExtra("cityName", cityName);
        intent.putExtra("cityId", cityId);
        Log.d("rytreyr", "backOnCity" + cityName + cityId);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
    
    void filterData(List<String> inputs, String newText) {
        int n = inputs.size();
        for (AllCityCode allCityCode : mAllCityCodes) {
            if (allCityCode.getFisrtPy().contains(newText)) {
                mSuggestList.add(allCityCode);
            } else {
                String[] split = allCityCode.getPinYin().split(",");
                int m = split.length;
                boolean isAdd = true;
                for (int i = 0; i < m; i++) {
                    if (i == n)
                        break;
                    if (!(inputs.get(i).equals(split[i].charAt(0) + "") || inputs.get(i).equals(split[i]))) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd)
                    mSuggestList.add(allCityCode);
            }
        }
    }
    
    private List<String> splitSpell(String s) {
        String regEx = "[^aoeiuv]?h?[iuv]?(ai|ei|ao|ou|er|ang?|eng?|ong|a|o|e|i|u|ng|n)?";
        int tag;
        List<String> tokenResult = new LinkedList<>();
        for (int i = s.length(); i > 0; i = i - tag) {
            Pattern pat = Pattern.compile(regEx);
            Matcher matcher = pat.matcher(s);
            matcher.find();
            tokenResult.add(matcher.group());
            tag = matcher.end() - matcher.start();
            s = s.substring(tag);
        }
        return tokenResult;
    }
    
    private boolean isExistCN(String s) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(s);
        return m.find();
    }
    
    private String filterCN(String chin) {
        chin = chin.replaceAll("[^(\\u4e00-\\u9fa5)]", "");
        return chin;
    }
    
    @Override
    public void onSwipeBackLayoutExecuted() {
        setResult(RESULT_CANCELED);
        super.onSwipeBackLayoutExecuted();
    }
}
