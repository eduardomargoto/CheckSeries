package br.com.etm.checkseries.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.CommentAdapter;
import br.com.etm.checkseries.deprecated.daos.FirebaseCore;
import br.com.etm.checkseries.deprecated.domains.Comment;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Profile;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.utils.UtilsEntitys;

/**
 * Created by EDUARDO_MARGOTO on 13/04/2016.
 */
public class CommentActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private Button btn_enviar;
    private CheckBox ckb_spoiler;
    private EditText edt_comment;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Comment> comments;
    private Serie serie;
    private Episode episode;
//    private ProgressBar pbComments;

    private boolean REMOVED_ADDED_COMMENT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_comment);
        comments = new ArrayList<Comment>();
        Intent it = getIntent();
        if (it != null) {
            serie = (Serie) it.getSerializableExtra("serie");
            episode = (Episode) it.getSerializableExtra("episode");
        }

        btn_enviar = (Button) findViewById(R.id.btn_enviar);
        ckb_spoiler = (CheckBox) findViewById(R.id.ckb_spoiler);
        edt_comment = (EditText) findViewById(R.id.edt_comment);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_commets);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle(this.getResources().getString(R.string.app_act_comments));

        if (episode != null && serie != null) {
            mToolbar.setSubtitle(episode.getNumEpisodeFormatted() + " - " + serie.getName()); // serie name;
        } else if (serie != null)
            mToolbar.setSubtitle(serie.getName()); // serie name;

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshComments();

                // Stop refresh animation
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshComments();
        CommentAdapter commentAdapter = new CommentAdapter(this, comments);
        recyclerView.setAdapter(commentAdapter);
        registerForContextMenu(recyclerView);
        btn_enviar.setOnClickListener(click_btn_enviar);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.i("LOG-AMBISERIES", "CommentActivity - onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);

        Comment c = comments.get(CommentAdapter.POSITION_LONGCLICK);

        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_options_comment, menu);
        if (Profile.getInstance().getId().equals("")) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }
        if (!c.getId_user().equals(Profile.getInstance().getId()))
            menu.getItem(0).setVisible(false);


        menu.setHeaderTitle(this.getResources().getString(R.string.app_act_comment));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final int position = CommentAdapter.POSITION_LONGCLICK;
        System.out.println("position: " + position);
        switch (item.getItemId()) {
//            case R.id.it_options_denounce:
//                try {
//                    UtilsEntitys.createAlertDialog(this,
//                            getResources().getString(R.string.app_act_comments), getResources().getString(R.string.app_dialog_comment_denounce_confirmation),
//                            getResources().getString(R.string.app_dialog_denouncebutton), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
////                                    Toast.makeText(CommentActivity.this, "Denounce position: " + position, Toast.LENGTH_SHORT).show();
//                                    dialog.dismiss();
//                                }
//                            },
//                            getResources().getString(R.string.app_dialog_cancel)).create().show();
//                } catch (Exception e) {
//                    Log.i("LOG-EXCEPTION", e.toString());
//                }
//
//                break;
            case R.id.it_options_remove:
                try {
                    UtilsEntitys.createAlertDialog(this,
                            getResources().getString(R.string.app_act_comments), getResources().getString(R.string.app_dialog_comment_remove_confirmation),
                            getResources().getString(R.string.app_dialog_removebutton), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    removeComment(comments.get(position));
                                    dialog.dismiss();
                                }
                            },
                            getResources().getString(R.string.app_dialog_cancel)).create().show();
                } catch (Exception e) {
                    Log.i("LOG-EXCEPTION", e.toString());
                }
                break;
        }

        return true;
    }

    View.OnClickListener click_btn_enviar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Comment c = new Comment();

            c.setSerieID(serie.getId().toString());

            if (episode != null)
                c.setEpisodeID("" + episode.getId());


            if (!edt_comment.getText().toString().isEmpty()) {
                c.setComment(edt_comment.getText().toString());
            } else {
                Toast.makeText(CommentActivity.this, getResources().getString(R.string.app_msg_comment_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            if (ckb_spoiler.isChecked()) {
                c.setSpoiler("true");
            } else {
                c.setSpoiler("false");
            }

            if (!Profile.getInstance().getId().equals("")) {
                c.setId_user(Profile.getInstance().getId());
                c.setName_user(Profile.getInstance().getName());
                c.setImage_url(Profile.getInstance().getImageUrl());

                comments.add(0, c);
                recyclerView.getAdapter().notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
                edt_comment.setText("");
                ckb_spoiler.setChecked(false);


                REMOVED_ADDED_COMMENT = true;
                // adicionar no firebase
                if (episode == null)
                    FirebaseCore.getInstance().child("comments-series").child(serie.getId().toString()).child(c.getId()).setValue(c);
                else
                    FirebaseCore.getInstance().child("comments-episodes").child("" + episode.getId()).child(c.getId()).setValue(c);


            } else {
                Toast.makeText(CommentActivity.this, getResources().getString(R.string.app_msg_comment_nouser), Toast.LENGTH_SHORT).show();
            }

        }
    };


    public void removeComment(Comment comment) {
        String child = "comments-series";
        String child_2 = serie.getId().toString();
        if (episode != null) {
            child = "comments-episodes";
            child_2 = "" + episode.getId();
        }
        REMOVED_ADDED_COMMENT = true;
        FirebaseCore.getInstance().child(child).child(child_2).child(comment.getId()).removeValue();
        comments.remove(comment);
        recyclerView.getAdapter().notifyItemRemoved(CommentAdapter.POSITION_LONGCLICK);

        Toast.makeText(this, getResources().getString(R.string.app_msg_comment_removed), Toast.LENGTH_SHORT).show();
    }

    public void refreshComments() {
//        Log.d("COMMENT-ID-PROF", "ID_PROFILE: " + Profile.getInstance().getId());
        String child = "comments-series";
        String child_2 = serie.getId().toString();
        if (episode != null) {
            child = "comments-episodes";
            child_2 = "" + episode.getId();
        }

        FirebaseCore.getInstance().child(child).child(child_2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("LOG", "onDataChange()");

                if (!REMOVED_ADDED_COMMENT) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Comment c = ds.getValue(Comment.class);
                        for (int i = 0; i < comments.size(); i++) {
                            if (c.getId().equals(comments.get(i).getId())) {
                                comments.set(i, c);
                                c = null;
                                break;
                            }
                        }
                        if (c != null)
                            comments.add(0, c);
                    }

                    if (comments.size() > 0)
                        recyclerView.getAdapter().notifyDataSetChanged();

                    REMOVED_ADDED_COMMENT = false;
                }

            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("LOG", "onCancelled()");
                Log.e("ERROR-COMMENTS", error.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
