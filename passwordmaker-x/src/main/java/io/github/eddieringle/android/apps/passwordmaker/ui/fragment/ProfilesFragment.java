package io.github.eddieringle.android.apps.passwordmaker.ui.fragment;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;

import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import butterknife.Views;
import io.github.eddieringle.android.apps.passwordmaker.R;
import io.github.eddieringle.android.apps.passwordmaker.core.PMChromeProfile;
import io.github.eddieringle.android.apps.passwordmaker.core.PMProfile;
import io.github.eddieringle.android.apps.passwordmaker.ui.activity.BaseActivity;
import io.github.eddieringle.android.apps.passwordmaker.ui.activity.MainActivity;
import io.github.eddieringle.android.apps.passwordmaker.ui.activity.ProfileEditActivity;
import io.github.eddieringle.android.apps.passwordmaker.ui.adapter.BaseListAdapter;
import io.github.eddieringle.android.apps.passwordmaker.util.FileUtils;
import io.github.eddieringle.android.apps.passwordmaker.util.GsonUtils;
import io.github.eddieringle.android.apps.passwordmaker.util.XmlSerilizerUtil;

public class ProfilesFragment extends ListFragment {

    private static final String TAG = ProfilesFragment.class.getSimpleName();

    private ActionMode mActionMode;

    private ArrayList<PMProfile> mProfiles = new ArrayList<PMProfile>();

    private ProfilesListAdapter mAdapter;

    private static final int FILE_SELECT_CODE = 0;

    private MenuItem importItem;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getBus().register(this);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new MultiChoiceModeImpl());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.profiles, menu);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        PMProfile profile = mProfiles.get(position);
        final Intent editProfile = new Intent(getBaseActivity(), ProfileEditActivity.class);
        editProfile.putExtra("profile", GsonUtils.toJson(profile));
        getBaseActivity().startActivityForResult(editProfile, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_profile) {
            final Intent newProfile = new Intent(getBaseActivity(), ProfileEditActivity.class);
            PMProfile emptyProfile = PMProfile.createDefault();
            emptyProfile.setProfileName("");
            newProfile.putExtra("profile", GsonUtils.toJson(emptyProfile));
            getBaseActivity().startActivityForResult(newProfile, 1);
            return true;
        } else if (item.getItemId() == R.id.action_import_profile) {
            importItem = item;
            showFileChooser();
        }
        return super.onOptionsItemSelected(item);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void loadProfiles(List<PMProfile> profileList) {
        mProfiles.clear();
        mProfiles.addAll(profileList);
        mAdapter = new ProfilesListAdapter(getBaseActivity());
        mAdapter.addAll(mProfiles);
        setListAdapter(mAdapter);
    }

    @Subscribe
    public void receiveUpdatedProfiles(MainActivity.UpdatedProfilesEvent event) {
        if (event != null && event.profileList != null) {
            loadProfiles(event.profileList);
        }
    }

    private void startProgressAction() {

        if (importItem != null) {
            importItem.setActionView(R.layout.indeterminate_progress_action);
        }
    }

    private void stopProgressAction() {
        if (importItem != null) {
            importItem.setActionView(null);
        }
    }

    private boolean saveImportedProfiles(List<PMProfile> profiles) {
        if(profiles !=null && profiles.size() > 0) {

            getBaseActivity().getPrefsEditor()
                    .putString("profiles", GsonUtils.toJson(profiles))
                    .commit();
            getBaseActivity().setResult(Activity.RESULT_OK);
            return true;
        }
        return false;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.choose_dialog)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), getString(R.string.install_file_manager),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(getActivity(), uri);
                    if (path != null) {
                        PMChromeProfileXmlParser task = new PMChromeProfileXmlParser();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new File(path));
                        } else {
                            task.execute(new File(path));
                        }
                    } else {

                        Toast.makeText(getActivity(), "Couldn't fetch the file", Toast.LENGTH_LONG).show();
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class MultiChoiceModeImpl implements AbsListView.MultiChoiceModeListener {

        int checkedCount = 0;

        HashMap<Integer, Boolean> checkMap = new HashMap<Integer, Boolean>();

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                boolean checked) {
            if (checked) {
                checkedCount++;
            } else {
                checkedCount--;
            }
            checkMap.put(position, checked);
            mAdapter.setNewSelection(position, checked);
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.profiles_cab, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                final ArrayList<PMProfile> trimList = new ArrayList<PMProfile>();
                int i = 0;
                for (PMProfile p : mProfiles) {
                    if (!(checkMap.get(i) != null && checkMap.get(i))) {
                        trimList.add(p);
                    }
                    i++;
                }
                if (trimList.isEmpty()) {
                    trimList.add(PMProfile.createDefault());
                    Toast.makeText(getBaseActivity(), R.string.toast_profile_list_default,
                            Toast.LENGTH_SHORT).show();
                }
                getBaseActivity().getPrefsEditor()
                        .putString("profiles", GsonUtils.toJson(trimList))
                        .commit();
                getBaseActivity().getBus().post(new NeedProfileListRefreshEvent());
                mAdapter.clearSelection();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    }

    class ProfilesListAdapter extends BaseListAdapter<PMProfile> {

        public ProfilesListAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PMProfile item = getItem(position);
            holder.profileName.setText(item.getProfileName());

            convertView.setBackgroundColor(Color.TRANSPARENT);
            if (isPositionChecked(position)) {
                convertView.setBackgroundResource(R.drawable.checked_background_passwordmaker);
            }

            return convertView;
        }

        class ViewHolder {

            @InjectView(android.R.id.text1)
            TextView profileName;

            public ViewHolder(View v) {
                Views.inject(this, v);
            }
        }
    }

    public static class NeedProfileListRefreshEvent {

    }

    private class PMChromeProfileXmlParser extends AsyncTask<File, Void, Boolean> {

        PMChromeProfile profiles;

        @Override
        protected void onPreExecute() {
            startProgressAction();
        }

        @Override
        protected Boolean doInBackground(File... file) {
            try {
                String profileXml = Files.toString(file[0], Charsets.UTF_8);
                profiles = XmlSerilizerUtil.createGson(true)
                        .fromXml(profileXml, PMChromeProfile.class);
                if(profiles !=null && profiles.mProfileList.size() > 0) {

                    TypeToken<List<PMProfile>> listToken = new TypeToken<List<PMProfile>>() {
                    };

                    String listJson = getBaseActivity().getPrefs().getString("profiles", "");

                    List<PMProfile> pmProfiles = GsonUtils.fromJson(listJson, listToken.getType());

                    for(PMChromeProfile.Profile p : profiles.mProfileList) {

                        PMProfile pm = new PMProfile();
                        pm.setCharacterSet(p.getCharacterSet());
                        pm.setHashAlgorithm(p.getHashAlgorithm());
                        pm.setL33tLevel(p.getL33tLevel());
                        pm.setModifier(p.getModifier());
                        pm.setL33tOrder(p.getL33tOrder());
                        pm.setPasswordLength(p.getPasswordLength());
                        pm.setPasswordPrefix(p.getPasswordPrefix());
                        pm.setPasswordSuffix(p.getPasswordSuffix());
                        pm.setUsername(p.getUsername());
                        pm.setProfileName(p.getProfileName());
                        pm.setUseUrlDomain(p.getUseUrlDomain());
                        pm.setUseUrlOther(p.getUseUrlOther());
                        pm.setUseUrlProtocol(p.getUseUrlProtocol());
                        pm.setUseUrlSubdomain(p.getUseUrlSubdomain());
                        pmProfiles.add(pm);
                    }
                    return saveImportedProfiles(pmProfiles);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            stopProgressAction();
            getBaseActivity().getBus().post(new NeedProfileListRefreshEvent());
        }
    }

}
