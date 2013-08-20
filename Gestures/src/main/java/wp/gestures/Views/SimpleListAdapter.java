package wp.gestures.Views;

import wp.gestures.Models.BubbleCloud;
import wp.gestures.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orrieshannon on 2013-08-17.
 */
public class SimpleListAdapter extends ArrayAdapter<String> {
    private Context context;
    public SimpleListAdapter(Context context, ArrayList<String> storyGroups) {
        super(context, R.layout.bubble_list_item, storyGroups);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BubbleCloud v = (BubbleCloud)inflater.inflate(R.layout.bubble_list_item, null);
        ((TextView) v.findViewById(R.id.title)).setText(getItem(position));
        v.addBubble(BubbleCloud.CloudPosition.TM, context.getResources().getDrawable(R.drawable.circley), "Share", null);
        v.addBubble(BubbleCloud.CloudPosition.ML, context.getResources().getDrawable(R.drawable.circley), "Archive", null);
        v.addBubble(BubbleCloud.CloudPosition.MR, context.getResources().getDrawable(R.drawable.circley), "Delete", null);
        return v;
    }
}
