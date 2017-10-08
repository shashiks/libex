package com.shaksoni.libex;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaksoni on 9/22/17.
 */

public class SearchResultAdapter extends ArrayAdapter<Book>{


    private Context mContext;
    private List<Book> dataList = null;
    private List<Book> fullList = null;
    private Filter bookFilter = null;
    private BookDAO bookDAO;


    public SearchResultAdapter(Context mContext, List<Book> data, BookDAO bookDAO) {

        super(mContext,R.layout.srch_result_row, data);
        this.mContext = mContext;
        this.dataList = data;
        this.fullList  = data;
        this.bookDAO  = bookDAO;
        bookFilter  = new BookFilter();

    }


    /**
     * This is called by system to render the list of book items.
     * The method is called for each panel once which contains one book info.
     * The controls and view are cached in the holder object
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Book aBook = dataList.get(position);
        ViewHolder viewHolder = null;
        View listItem = convertView;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.srch_result_row, parent, false);
            viewHolder.bookTitle = (TextView) convertView.findViewById(R.id.txtSrchBookTitle);
            viewHolder.bookAuthor = (TextView) convertView.findViewById(R.id.txtSrchBookAuthor);
            viewHolder.rating = (RatingBar) convertView.findViewById(R.id.wgtSrchRatings);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bookTitle.setText(aBook.getTitle());
        viewHolder.bookAuthor.setText(aBook.getAllAuthors());
        viewHolder.rating.setRating(aBook.getAverageRating());
        viewHolder.rating.setNumStars(5);
        return convertView;
    }


    @Override
    public Filter getFilter() {

        return bookFilter;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void clear() {

        super.clear();
    }

    private class BookFilter extends  Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults oReturn = new FilterResults();
                ArrayList<Book> results = new ArrayList<Book>();
                if (TextUtils.isEmpty(constraint)) {
                    List<Book> allBooks = bookDAO.readAll(R.integer.bookSearchLimit);
                    oReturn.count = allBooks.size();
                    oReturn.values = allBooks;
                } else {
                    List<Book> resultList = new ArrayList<>();
                    String qry = constraint.toString().toLowerCase();
                    for (final Book b : dataList) {
                        if (b.getTitle().toLowerCase().contains(qry)) {
                            resultList.add(b);
                        }
                    }
                    oReturn.count = resultList.size();
                    oReturn.values = resultList;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                dataList = (ArrayList<Book>) results.values; // has the filtered values

                if (dataList != null) {
                    clear();
                    int count = dataList.size();
                    for (int i = 0; i < count; i++) {
                        Book pkmn = (Book) dataList.get(i);
                        add(pkmn);
                    }

                    notifyDataSetChanged(); // notifies the data with new filtered values
                }
            }
    }


    // View lookup cache
    private static class ViewHolder {
        TextView bookTitle, bookAuthor;
        RatingBar rating;
    }


}
