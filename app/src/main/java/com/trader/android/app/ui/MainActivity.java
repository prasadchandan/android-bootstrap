

package com.trader.android.app.ui;

import android.accounts.AccountsException;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.trader.android.app.BootstrapServiceProvider;
import com.trader.android.app.R;
import com.trader.android.app.events.NavItemSelectedEvent;
import com.trader.android.app.util.Ln;
import com.trader.android.app.util.UIUtils;
import com.github.kevinsawicki.wishlist.Toaster;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link com.trader.android.app.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    @Inject protected BootstrapServiceProvider serviceProvider;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;
    private Drawer.Result result = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
        ButterKnife.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        setupToolbar();

//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintResource(R.color.primary_dark);
//        tintManager.setStatusBarTintEnabled(true);

        if(!isTablet()) {

//            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//            drawerToggle = new ActionBarDrawerToggle(
//                    this,                                        /* Host activity */
//                    drawerLayout,                                /* DrawerLayout object */
//                    R.drawable.ic_drawer,                        /* nav drawer icon to replace 'Up' caret */
//                    R.string.navigation_drawer_open,             /* "open drawer" description */
//                    R.string.navigation_drawer_close) {          /* "close drawer" description */
//
//                /** Called when a drawer has settled in a completely closed state. */
//                public void onDrawerClosed(View view) {
//                    getActionBar().setTitle(title);
//                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                }
//
//                /** Called when a drawer has settled in a completely open state. */
//                public void onDrawerOpened(View drawerView) {
//                    getActionBar().setTitle(drawerTitle);
//                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                }
//            };

            // Set the drawer toggle as the DrawerListener
//            drawerLayout.setDrawerListener(drawerToggle);

//            navigationDrawerFragment = (NavigationDrawerFragment)
//                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
//
//            // Set up the drawer.
//            navigationDrawerFragment.setup(R.id.navigation_drawer,
//                                           (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

            ///
            result = new Drawer()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withActionBarDrawerToggle(true)
                    .withHeader(R.layout.header)
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
                            new PrimaryDrawerItem().withName("Game").withIcon(FontAwesome.Icon.faw_gamepad),
                            new PrimaryDrawerItem().withName("Custom").withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                            new SectionDrawerItem().withName("Section Header"),
                            new SecondaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog),
                            new SecondaryDrawerItem().withName("Help").withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
                            new SecondaryDrawerItem().withName("Open Source").withIcon(FontAwesome.Icon.faw_github).withBadge("12").withIdentifier(1),
                            new SecondaryDrawerItem().withName("Contact").withIcon(FontAwesome.Icon.faw_bullhorn)
                    )
                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {
                            Toast.makeText(MainActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {
                            Toast.makeText(MainActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                            if (drawerItem instanceof Nameable) {
                                Toaster.showShort(MainActivity.this,MainActivity.this.getString(((Nameable) drawerItem).getNameRes()));
                            }

                            if (drawerItem instanceof Badgeable) {
                                Badgeable badgeable = (Badgeable) drawerItem;
                                if (badgeable.getBadge() != null) {
                                    //note don't do this if your badge contains a "+"
                                    int badge = Integer.valueOf(badgeable.getBadge());
                                    if (badge > 0) {
                                        result.updateBadge(String.valueOf(badge - 1), position);
                                    }
                                }
                            }
                        }
                    })
                    .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                            if (drawerItem instanceof SecondaryDrawerItem) {
                                Toast.makeText(MainActivity.this, MainActivity.this.getString(((SecondaryDrawerItem) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    })
                    .build();

            //disable scrollbar
            result.getListView().setVerticalScrollBarEnabled(false);
            ///
        }
        checkAuth();
    }

    private void setupToolbar() {
        // As we're using a Toolbar, we should retrieve it and set it
        // to be our ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bootstrap");
//        getSupportActionBar().setSubtitle("Demo Application");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
//            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            //drawerToggle.onConfigurationChanged(newConfig);
        }
    }


    private void initScreen() {
        if (userHasAuthenticated) {

            Ln.d("Foo");
            final FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new CarouselFragment())
                    .commit();
        }

    }

    private Boolean isAuthed() throws IOException, AccountsException {
        return (serviceProvider.getService(this) != null);
    }

    private Observable<Boolean> checkAuthObservable() {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                try {
                    return Observable.just(isAuthed());
                } catch(IOException e) {
                    return Observable.error(e);
                } catch(AccountsException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    private void checkAuth() {

        AndroidObservable.bindActivity(this, checkAuthObservable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Ln.e(e);
                        Toaster.showLong(MainActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean isAuthed) {
                        userHasAuthenticated = true;
                        initScreen();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        //if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
        if (!isTablet()) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
            case R.id.timer:
                navigateToTimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
        startActivity(i);
    }

    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
            case 1:
                // Timer
                navigateToTimer();
                break;
        }
    }
}
