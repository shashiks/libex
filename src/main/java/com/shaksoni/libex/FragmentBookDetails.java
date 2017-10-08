package com.shaksoni.libex;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shaksoni on 9/27/17.
 */

public class FragmentBookDetails extends Fragment implements ImageByUrlResult{


    private  View rootView;
    private ImageView imgBookCover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Book book = (Book) getArguments().getSerializable("book");

        rootView = inflater.inflate(R.layout.fragment_bookdetails, container, false);

        final TextView txtBookDetailTitle = (TextView) rootView.findViewById(R.id.txtBookDetailTitle);
        final TextView txtBookDetailAuthor = (TextView) rootView.findViewById(R.id.txtBookDetailAuthor);
        final TextView txtPublisher = (TextView) rootView.findViewById(R.id.txtBookDetailPublisher);
        final TextView txtPublishYear = (TextView) rootView.findViewById(R.id.txtBookDetailPublishYear);
        final TextView txtNumPages = (TextView) rootView.findViewById(R.id.txtBookDetailNumPages);
        final TextView txtAvgRating = (TextView) rootView.findViewById(R.id.txtBookDetailAvgRating);
        final TextView txtMyRating = (TextView) rootView.findViewById(R.id.txtBookDetailMyRating);

         imgBookCover = (ImageView) rootView.findViewById(R.id.imgBookDetailsCover);
         ImageByUrlTask imageTask = new ImageByUrlTask(book.getThumbnailUrl(), this);
        imageTask.execute();

        Button btnSaveBookDetails = (Button) rootView.findViewById(R.id.btnSaveBookDetails);

        Button btnCancelBookDetails = (Button) rootView.findViewById(R.id.btnCancelBookDetails);

        txtBookDetailTitle.setText(book.getTitle());
        txtBookDetailAuthor.setText(book.getAllAuthors());
        txtPublisher.setText(book.getPublisher());
        txtPublishYear.setText(new Integer(book.getYearOfPublish()).toString());
        txtNumPages.setText(new Integer(book.getNumPages()).toString());
        txtAvgRating.setText(new Float(book.getAverageRating()).toString());
        txtMyRating.setText("0");// some default value which may be updated later


        btnCancelBookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAddBook fgAddBook = new FragmentAddBook();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fgAddBook).commit();
            }
        });

        btnSaveBookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String saveResult = storeBook(book);
                FragmentAddBook fgAddBook = new FragmentAddBook();
                Bundle params = new Bundle();
                params.putString("message", saveResult);
                fgAddBook.setArguments(params);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fgAddBook).commit();
            }
        });


        return rootView;
    }

    private String storeBook(Book book) {

        BookDAO dao = new BookDAO(getContext());
        return dao.save(book);


    }

    @Override
    public void onSuccess(final Bitmap downloadedImage) {
        if(downloadedImage != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgBookCover.setImageBitmap(downloadedImage);

                }
            });

        }
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getContext(), "Error looking up Book Cover image ", Toast.LENGTH_LONG).show();

    }
}
