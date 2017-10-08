package com.shaksoni.libex;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaksoni on 9/22/17.
 */

public class FragmentSearch extends ListFragment implements SearchView.OnQueryTextListener{

    private ListView srchResultView;

    private List<Book> myBooksList = new ArrayList<Book>();

    private SearchResultAdapter srchResAdapter;

    private BookDAO bookDAO;


    public FragmentSearch() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookDAO = new BookDAO(getContext());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        srchResAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText))
            srchResAdapter.getFilter().filter(newText);
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        srchResultView = rootView.findViewById(R.id.list);
        createBooks();
        srchResAdapter = new SearchResultAdapter(container.getContext(), myBooksList, bookDAO);
        srchResultView.setAdapter(srchResAdapter);
        srchResultView.setTextFilterEnabled(true);


        srchResultView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                }
            }

        });

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager =
                (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.findbook).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener( this);

        /* USEFULL WAY TO ACCESS THE SEARCH CONTROL TEXTBOX FROM THE SEARCH VIEW MENU


        TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEARCH) {


                    return true;
                }
                return true;
            }
        });
        */

    }


        private List<Book> createBooks() {

           //limit this will help in future
            myBooksList = new BookDAO(getContext()).readAll(R.integer.bookSearchLimit);
            return  myBooksList;
    }


}
