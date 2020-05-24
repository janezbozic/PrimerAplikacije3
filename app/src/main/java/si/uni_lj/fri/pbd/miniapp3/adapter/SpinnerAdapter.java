package si.uni_lj.fri.pbd.miniapp3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import si.uni_lj.fri.pbd.miniapp3.R;

//Adapter for spinner
public class SpinnerAdapter extends BaseAdapter {

    private LinkedList<String> recipeList;
    private LayoutInflater mInflater;

    public SpinnerAdapter (Context context, LinkedList<String> rList){
        super();
        mInflater = LayoutInflater.from(context);
        recipeList = rList;
    }


    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Setting view for every spinner item (every ingredient)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.spinnerValue = (TextView) convertView.findViewById(R.id.text_view_spinner_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.spinnerValue.setText(recipeList.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView spinnerValue; //spinner ingredient
    }
}
