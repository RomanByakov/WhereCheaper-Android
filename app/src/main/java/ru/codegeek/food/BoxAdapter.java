package ru.codegeek.food;

/**
 * Created by Роман on 06.12.2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BoxAdapter extends BaseAdapter {

    LayoutInflater lInflater;
    Context mContext;
    Price[] prices;

    public BoxAdapter(Context context, Price[] prices){
        mContext = context;
        this.prices = prices;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return prices.length;
    }

    @Override
    public Object getItem(int position) {
        return prices[position];
    }

    @Override
    public long getItemId(int position) {
        return prices[position].id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.price_item, parent, false);
        }
        TextView tvShop = (TextView)view.findViewById(R.id.tvShop);
        TextView tvPrice = (TextView)view.findViewById(R.id.tvPrice);

        Price price =(Price) getItem(position);

        tvShop.setText(price.Shop.name);
        tvPrice.setText(price.price);

        return view;
    }
}