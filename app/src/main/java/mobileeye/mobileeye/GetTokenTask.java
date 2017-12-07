
        package mobileeye.mobileeye;

/**
 * Created by Tomasz on 14.11.2017.
 */

        import android.accounts.Account;
        import android.app.Activity;
        import android.os.AsyncTask;

        import com.google.android.gms.auth.GoogleAuthException;
        import com.google.android.gms.auth.GoogleAuthUtil;
        import com.google.android.gms.auth.UserRecoverableAuthException;

        import java.io.IOException;

/**
 * Created by MG on 04-06-2016.
 */
public class GetTokenTask extends AsyncTask<Void, Void, Void> {
    Activity mActivity;
    String mScope;
    Account mAccount;
    int mRequestCode;

    GetTokenTask(Activity activity, Account account, String scope, int requestCode) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mAccount = account;
        this.mRequestCode = requestCode;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String token = fetchToken();
            if (token != null) {
                ((ObjectRecognitionActivity)mActivity).onTokenReceived(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken() throws IOException {
        String accessToken;
        try {
            accessToken = GoogleAuthUtil.getToken(mActivity, mAccount, mScope);
            GoogleAuthUtil.clearToken (mActivity, accessToken); // used to remove stale tokens.
            accessToken = GoogleAuthUtil.getToken(mActivity, mAccount, mScope);
            return accessToken;
        } catch (UserRecoverableAuthException userRecoverableException) {
            mActivity.startActivityForResult(userRecoverableException.getIntent(), mRequestCode);
        } catch (GoogleAuthException fatalException) {
            fatalException.printStackTrace();
        }
        return null;
    }
}
    RAW Paste Data



        create new paste  /  dealsnew!  /  api  /  trends  /  syntax languages  /  faq  /  tools  /  privacy  /  cookies  /  contact  /  dmca  /  scraping  /  go
        Site design & logo © 2017 Pastebin; user contributions (pastes) licensed under cc by-sa 3.0 -- Dedicated Server Hosting by Steadfast

        Top
        xSign Up For FreeCircleCI 2.0 is freaking fast. We reduced builds from minutes to 12 seconds.