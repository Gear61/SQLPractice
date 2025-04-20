package randomappsinc.com.sqlpractice.dialogs;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.utils.TutorialServer;

public class LibraryDialog {

    public interface Listener {
        void openWebPage(String helpUrl);
    }

    private final Listener lessonClickListener;
    private MaterialDialog dialog;

    public LibraryDialog(Context context, Listener listener) {
        this.lessonClickListener = listener;
        this.dialog = new MaterialDialog.Builder(context)
                .title(R.string.library)
                .items(TutorialServer.get().getLessonsArray())
                .itemsCallback((
                        dialog,
                        itemView,
                        position,
                        text
                ) -> lessonClickListener.openWebPage(text.toString()))
                .positiveText(R.string.close)
                .build();
    }

    public void show() {
        dialog.show();
    }
}
