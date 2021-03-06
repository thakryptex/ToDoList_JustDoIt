package ru.kryptex.todo_sitepoint;

import android.support.v7.app.ActionBarActivity;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

// чисто для поддержки support v7


public abstract class ListSupportActivity extends ActionBarActivity {

    private ListView mListView;

    protected ListView getListView() {
        if (mListView == null) {
            mListView = (ListView) findViewById(android.R.id.list);
        }
        return mListView;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    protected ListAdapter getListAdapter() {
        ListAdapter adapter = getListView().getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
        } else {
            return adapter;
        }
    }
}