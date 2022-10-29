package org.disroot.disrootapp.utils;

import org.disroot.disrootapp.R;
import org.disroot.disrootapp.ui.MainActivity;

/**
 * Created by jackson on 02/11/15.
 */
public class Constants {
    public static final long SPLASH_SCREEN_DURATION = 3000;
    public static final long SPLASH_SCREEN_INTERVAL = 1000;

    public static final String URL_DisApp_FORUM = "https://forum.disroot.org/";
    public static final String URL_DisApp_CRYPTPAD = "https://cryptpad.disroot.org/";
    public static final String URL_DisApp_BIN = "https://bin.disroot.org";
    public static final String URL_DisApp_UPLOAD = "https://upload.disroot.org";
    public static final String URL_DisApp_SEARX = "https://search.disroot.org";
    public static final String URL_DisApp_BOARD = "https://board.disroot.org";
    public static final String URL_DisApp_USER = "https://user.disroot.org";
    public static final String URL_DisApp_HOWTO = "https://howto.disroot.org";
    public static final String URL_DisApp_K9HELP = "https://howto.disroot.org/en/tutorials/email/clients/mobile/k9";
    public static final String URL_DisApp_XMPPHELP = "https://howto.disroot.org/en/tutorials/chat/mobile/android/conversations";
    public static final String URL_DisApp_SEARXHELP = "https://disroot.org/services/search";
    public static final String URL_DisApp_STATEXMPP = "xmpp:state@chat.disroot.org?join";
    public static final String URL_DisApp_STATEMATRIX = "https://matrix.to/#/#state:disroot.org";
    public static final String URL_DisApp_STATESOCIAL = "https://hub.disroot.org/channel/disroot_state";
    public static final String URL_DisApp_STATENEWS = "https://state.disroot.org/subscribe";
    public static final String URL_DisApp_STATERSS = "https://state.disroot.org/atom";


    public static final String URL_LICENSE = "https://www.gnu.org/licenses/lgpl-3.0.en.html";
    public static final String URL_DISROOT = "https://disroot.org";
    public static final String URL_FDROID = "https://f-droid.org/FDroid.apk";
    public static final String URL_DIO = "https://github.com/renancunha33/DiolinuxApp";
    public static final String URL_TRANSLATE = "https://git.disroot.org/Disroot/disapp";
    public static final String URL_SOURCE = "https://git.disroot.org/Disroot/disapp";
    public static final String URL_BUGS = "https://git.disroot.org/Disroot/disapp/issues";
    public static final String URL_XMPP = "xmpp:disroot@chat.disroot.org?join";
    public static final String URL_SUPPORT = "mailto:support@disroot.org";
    public static final String URL_DisApp_CLOUDHELP = "https://howto.disroot.org/en/tutorials/cloud/clients/mobile/android";
    public static final String URL_DisApp_FORUMHELP = "https://howto.disroot.org/en/tutorials/forum";
    public static final String URL_DisApp_PADHELP = "https://howto.disroot.org/en/tutorials/office/pads/padland";
    public static final String URL_DisApp_CRYPTPADHELP = "https://howto.disroot.org/it/tutorials/office/cryptpad";
    public static final String URL_DisApp_BINHELP = "https://howto.disroot.org/en/tutorials/office/bin";
    public static final String URL_DisApp_UPLOADHELP = "https://howto.disroot.org/en/tutorials/office/lufi";
    public static final String URL_DisApp_BOARDHELP = "https://howto.disroot.org/en/tutorials/project_board";
    public static final String URL_DisApp_NOTESHELP = "https://howto.disroot.org/en/tutorials/cloud/clients/mobile/android/using-notes";
    public static final String URL_DisApp_CALLSHELP = "https://disroot.org/en/services/calls";
    public static final String URL_DisApp_GITHELP = "https://howto.disroot.org/en/contribute/git";
    public static final String URL_DisApp_DISAPP = "https://f-droid.org/en/packages/org.disroot.disrootapp/";

    public static final String k9 = "com.fsck.k9";
    public static final String nc = "com.nextcloud.client";
    public static final String Diaspora = "com.github.dfa.diaspora_android";
    public static final String Conversations = "eu.siacs.conversations";
    public static final String PixArt = "de.pixart.messenger";
    public static final String Padland = "com.mikifus.padland";
    public static final String NotesApp = "it.niedermann.owncloud.notes";
    public static final String CallsApp = "org.jitsi.meet";
    public static final String GitApp = "org.mian.gitnex";

    public static final int[] buttonIDs = new int[] {R.id.MailBtn, R.id.CloudBtn, R.id.ForumBtn,R.id.ChatBtn,R.id.PadBtn,R.id.CryptpadBtn,R.id.BinBtn,R.id.UploadBtn,R.id.SearxBtn,R.id.BoardBtn,R.id.CallsBtn,R.id.NotesBtn,R.id.GitBtn,R.id.UserBtn,R.id.StateBtn,R.id.HowToBtn,R.id.AboutBtn};


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;//file upload
    public static final int INPUT_FILE_REQUEST_CODE = 1;//file upload
    public static final int FILECHOOSER_RESULTCODE = 1;//file upload
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String components = "https://status.disroot.org/index.json";
}
