package pazzaglia.it.moviedb.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.utils.Constant;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar _toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(Constant.EXTRA_MOVIE, getIntent().getParcelableExtra(Constant.EXTRA_MOVIE));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detailmovie, fragment)
                    .commit();
        }
    }

}
