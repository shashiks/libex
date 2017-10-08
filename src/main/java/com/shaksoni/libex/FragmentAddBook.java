package com.shaksoni.libex;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shaksoni.libex.vision.ocrreader.OcrCaptureActivity;

/**
 * Created by shaksoni on 9/22/17.
 */

public class FragmentAddBook extends Fragment implements ISBNLookupResult {

    private String bookLookupRootUrl;
    private  View rootView;
    private Gson gson;
    private static final char URL_PARAM_SEPARATOR  = '&';

    private SEARCH_TYPES currentSearchType = SEARCH_TYPES.BY_ISBN;

    public static final  String PARAM_ID_NAME = "SCAN_FIELD_ID";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookLookupRootUrl = getResources().getString(R.string.google_book_api_url);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getArguments() != null && getArguments().getString("message") != null) {
            String message = (String) getArguments().getString("message");
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }

        rootView = inflater.inflate(R.layout.fragment_searchbook, container, false);

        final TextView txtParamISBN = (TextView) rootView.findViewById(R.id.txtSearchIsbn);
        final TextView txtParamTitle = (TextView) rootView.findViewById(R.id.txtSearchTitle);
        final TextView txtParamAuthor = (TextView) rootView.findViewById(R.id.txtSearchAuhtorName);

        Button butScanISBN = (Button) rootView.findViewById(R.id.lblSearchIsbn);
        Button butScanAuthor = (Button) rootView.findViewById(R.id.lblSearchAuthorName);
        Button butScanTitle = (Button) rootView.findViewById(R.id.lblSearchTitle);

        Button butIsbn = (Button) rootView.findViewById(R.id.btnSearchByIsbn);
        Button butTitleAuthor = (Button) rootView.findViewById(R.id.btnSearchByDetails);

        final RelativeLayout pnlIsbnSearch =  rootView.findViewById(R.id.pnlIsbnSearch);
        final RelativeLayout pnlTitleAuthorSearch =  rootView.findViewById(R.id.pnlDetailSearch);


        RadioGroup searchTypeOptions = (RadioGroup)rootView.findViewById(R.id.searchOptions);
        searchTypeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                if (rb.getId() == R.id.optIsbn) {
                    currentSearchType = SEARCH_TYPES.BY_ISBN;
                    pnlIsbnSearch.setVisibility(View.VISIBLE);
                    pnlTitleAuthorSearch.setVisibility(View.INVISIBLE);
                } else {
                    currentSearchType = SEARCH_TYPES.BY_TITLE_AUTHOR;
                    pnlIsbnSearch.setVisibility(View.INVISIBLE);
                    pnlTitleAuthorSearch.setVisibility(View.VISIBLE);
                }
            }
        });


        butIsbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if(activity != null && isAdded()) {
                    String value = txtParamISBN.getText().toString();
                    processBookDetails(currentSearchType, value);

                }
            }
        });

        butTitleAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if(activity != null && isAdded()) {
                    String title = txtParamTitle.getText().toString();
                    String author = txtParamAuthor.getText().toString();
                    processBookDetails(currentSearchType, title, author);

                }
            }
        });

        butScanISBN.setOnClickListener(new ScanClickListener(R.id.txtSearchIsbn));
        butScanAuthor.setOnClickListener(new ScanClickListener(R.id.txtSearchAuhtorName));
        butScanTitle.setOnClickListener(new ScanClickListener(R.id.txtSearchTitle));

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            int controlId = data.getIntExtra(PARAM_ID_NAME, R.id.lblSearchIsbn);
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                ((TextView)getActivity().findViewById(controlId)).setText(result);
//                ((TextView)getActivity().findViewById(R.id.txtSearchIsbn)).setText(result);
//                Toast.makeText(getContext(), "Scanned value " + result, Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "No value(s) scanned", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     *     Single listener to all clicks. The id of the control is sent to scanner
     *     which is retrieved from <tt>onActivityResult</tt> and that controls is populated with scanned value
     */
    private class ScanClickListener implements View.OnClickListener {

        int srcControlId;
        public ScanClickListener(int controlId) {
            srcControlId = controlId;
        }

        @Override
        public void onClick(View view) {
            Intent ocrIntent = new Intent( getActivity(), OcrCaptureActivity.class);
            ocrIntent.putExtra(PARAM_ID_NAME, srcControlId);
            startActivityForResult(ocrIntent, 1);
        }
    }


    public enum SEARCH_TYPES {

        BY_ISBN, BY_TITLE_AUTHOR
    }


    private void processBookDetails(SEARCH_TYPES searchType, String... params) {

        StringBuilder lookupUrl = new StringBuilder(bookLookupRootUrl).append(URL_PARAM_SEPARATOR)
                .append(getResources().getString(R.string.google_api_max_result_param))
                .append(URL_PARAM_SEPARATOR);


        switch (searchType) {
            case BY_ISBN:
                String bookISBNParam = getResources().getString(R.string.google_api_isbn_param);
                lookupUrl.append(bookISBNParam.replace("@BOOK_ISBN@", params[0]));
                break;
            case BY_TITLE_AUTHOR:
                String bookTitleAuthorParam = getResources().getString(R.string.google_api_title_author_param);
                lookupUrl.append(bookTitleAuthorParam
                            .replace("@BOOK_TITLE@", "\"".concat(params[0]).concat("\""))
                                .replace("@BOOK_AUTHOR@", "\"".concat(params[1]).concat("\"")));
                break;
        }

        String s = Uri.parse(lookupUrl.toString()).buildUpon().build().toString();

        AsyncBookDetailsReader mTask = new AsyncBookDetailsReader(currentSearchType, rootView, FragmentAddBook.this);
        mTask.execute(s);
    }



    @Override
    public void onSuccess(String bookInfo) {


        try {
            Book aBook = new JsonBookMarshaller().fromJson(bookInfo);
            FragmentBookDetails bookDetailsFragment = new FragmentBookDetails();
            Bundle params = new Bundle();
            params.putSerializable("book", aBook);
            bookDetailsFragment.setArguments(params);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, bookDetailsFragment).commit();

        } catch (Exception e) {
            String errMsg = null;
            if(e instanceof BookParseException) {
                errMsg = e.getMessage();
            } else {
                errMsg = "Coudn't read book details due to ::" + e.getMessage();
            }
            final String s = errMsg;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    @Override
    public void onFailure(final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Coudn't read details for book because :: " + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
