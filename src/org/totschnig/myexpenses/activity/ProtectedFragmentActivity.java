/*   This file is part of My Expenses.
 *   My Expenses is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   My Expenses is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with My Expenses.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.totschnig.myexpenses.activity;

import org.totschnig.myexpenses.MyApplication;
import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.dialog.ContribDialogFragment;
import org.totschnig.myexpenses.dialog.DialogUtils;
import org.totschnig.myexpenses.dialog.MessageDialogFragment.MessageDialogListener;
import org.totschnig.myexpenses.model.ContribFeature.Feature;
import org.totschnig.myexpenses.util.Utils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Dialog;
import android.os.Bundle;

public abstract class ProtectedFragmentActivity extends SherlockFragmentActivity
    implements MessageDialogListener {
  private Dialog pwDialog;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
  }
  @Override
  protected void onPause() {
    super.onPause();
    MyApplication app = MyApplication.getInstance();
    if (app.isLocked && pwDialog != null)
      pwDialog.dismiss();
    else {
      app.setmLastPause();
    }
  }
  @Override
  protected void onDestroy() {
    super.onDestroy();
    MyApplication.getInstance().setmLastPause();
  }
  @Override
  protected void onResume() {
    super.onResume();
    MyApplication app = MyApplication.getInstance();
    if (app.shouldLock()) {
      if (pwDialog == null)
        pwDialog = DialogUtils.passwordDialog(this);
      DialogUtils.showPasswordDialog(this,pwDialog);
    }
  }
  public void showContribDialog(final Feature feature) {
    ContribDialogFragment.newInstance(feature).show(getSupportFragmentManager(),"CONTRIB");
  }

  public boolean dispatchCommand(int command, Object tag) {
    switch(command) {
    case R.id.CONTRIB_PLAY_COMMAND:
      Utils.viewContribApp(this);
      return true;
    }
    return false;
  }
  public void cancelDialog() {
    // TODO Auto-generated method stub
  }
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    switch(item.getItemId()) {
    case android.R.id.home:
      setResult(RESULT_CANCELED);
      finish();
      return true;
   default:
     return false;
    }
  }
}
