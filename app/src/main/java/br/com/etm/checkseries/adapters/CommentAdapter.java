package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.domains.Comment;

/**
 * Created by EDUARDO_MARGOTO on 13/04/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Comment> comments;
    private MyViewHolder mvh;

    public static int POSITION_LONGCLICK = -1;

    public CommentAdapter(Context commentActivity, List<Comment> comments) {
        this.layoutInflater = (LayoutInflater) commentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = commentActivity;
        this.comments = comments;

    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View view = layoutInflater.inflate(R.layout.item_comment, parent, false);
        mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.MyViewHolder holder, final int position) {
        final Comment comment = comments.get(position);
        if (position == comments.size() - 1) {
            holder.line.setVisibility(View.INVISIBLE);
        }
        Picasso.with(context).load("https://lh6.googleusercontent.com/" + comment.getImage_url())
                .placeholder(R.drawable.loading_animation_black)
                .resize(48, 48)
                .error(R.drawable.no_user)
                .into(holder.imv_user);

        if (comment.getSpoiler().equals("true")) {
            holder.txv_comment.setText(context.getResources().getString(R.string.app_alert_spoiler));
            holder.txv_comment.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.txv_comment.setText(comment.getComment());
            holder.txv_comment.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_dark));
        }


        holder.txv_date.setText(comment.getDate_comment());
        holder.txv_nome_user.setText(comment.getName_user());

        holder.rl_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getSpoiler().equals("true")) {
                    holder.txv_comment.setText(comment.getComment());
                    holder.txv_comment.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_dark));
                }
            }
        });
        holder.rl_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                POSITION_LONGCLICK = holder.getAdapterPosition();

                v.showContextMenu();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txv_nome_user, txv_date, txv_comment;
        ImageView imv_user;
        RelativeLayout rl_view;
        View line;

        public MyViewHolder(View itemView) {
            super(itemView);

            line = (View) itemView.findViewById(R.id.line);
            imv_user = (ImageView) itemView.findViewById(R.id.imv_user);
            rl_view = (RelativeLayout) itemView.findViewById(R.id.rl_view);
            txv_comment = (TextView) itemView.findViewById(R.id.txv_comment);
            txv_date = (TextView) itemView.findViewById(R.id.txv_date);
            txv_nome_user = (TextView) itemView.findViewById(R.id.txv_nome_user);

        }
    }
}
